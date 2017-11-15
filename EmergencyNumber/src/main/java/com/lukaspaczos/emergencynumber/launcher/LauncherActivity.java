package com.lukaspaczos.emergencynumber.launcher;

import android.Manifest;
import android.accounts.NetworkErrorException;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.lukaspaczos.emergencynumber.MainActivity;
import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.communication.network.api.numbers.EmergencyNumbersApi;
import com.lukaspaczos.emergencynumber.communication.network.api.numbers.EmergencyNumbersPOJO;
import com.lukaspaczos.emergencynumber.util.NetworkUtils;
import com.lukaspaczos.emergencynumber.util.Pref;
import com.lukaspaczos.emergencynumber.util.ResizeAnimation;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LauncherActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static final int PERMISSIONS_REQUEST_CODE = 2536;
    private static final String[] permissionsArray = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS};

    private Intent startIntent;

    private View contentFrame;
    private ProgressBar mProgressView;
    private ImageView logo;
    private Animation shrinkAnimation;
    private Animation growAnimation;
    private boolean isShrunk = false;
    private boolean canBack = false;

    private String fetchedEmergencyNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        contentFrame = findViewById(R.id.content_frame);
        mProgressView = (ProgressBar) findViewById(R.id.progress);

        startIntent = getIntent();

        logo = (ImageView) findViewById(R.id.logo);
        int sizeBig = getResources().getDimensionPixelSize(R.dimen.logo_big);
        int sizeSmall = getResources().getDimensionPixelSize(R.dimen.logo_small);
        shrinkAnimation = new ResizeAnimation(logo, sizeBig, sizeBig, sizeSmall, sizeSmall);
        shrinkAnimation.setFillAfter(true);
        growAnimation = new ResizeAnimation(logo, sizeSmall, sizeSmall, sizeBig, sizeBig);
        growAnimation.setFillAfter(true);

        for (String permission : permissionsArray) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        permissionsArray,
                        PERMISSIONS_REQUEST_CODE
                );
                break;
            }
        }

        if (savedInstanceState == null) {
            startStart();
        }

        showProgress(true);
        fetchEmergencyNumbersData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int result : grantResults) {
            if (result == android.content.pm.PackageManager.PERMISSION_DENIED) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setCancelable(false);
                dialog.setTitle(R.string.no_permissions_title);
                dialog.setMessage(R.string.no_permissions_message);
                dialog.setPositiveButton(R.string.no_permissions_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                dialog.setNeutralButton(R.string.no_permissions_retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                LauncherActivity.this,
                                permissionsArray,
                                PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                dialog.show();

                return;
            }
        }
    }

    private void verifiedInit() {
        final String name = Pref.getString(Pref.NAME, "");
        final String number = Pref.getString(Pref.EMERGENCY_PHONE_NUMBER, "");

        if (!name.isEmpty() && !number.isEmpty()) {
            if (number.equals(fetchedEmergencyNumber)
                    || fetchedEmergencyNumber.isEmpty()
                    || !Pref.getBoolean(Pref.SHOW_NUMBER_CHANGED_DIALOG, true)) {
                startMainActivity();
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setCancelable(false);
                dialog.setTitle(R.string.init_update_number_dialog_title);
                dialog.setMessage(String.format(Locale.getDefault(),
                        getString(R.string.init_update_number_dialog_msg),
                        number,
                        fetchedEmergencyNumber));
                dialog.setPositiveButton(R.string.init_update_number_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Pref.putString(Pref.EMERGENCY_PHONE_NUMBER, fetchedEmergencyNumber);
                        startMainActivity();
                    }
                });
                dialog.setNegativeButton(R.string.init_update_number_dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startMainActivity();
                    }
                });
                dialog.setNeutralButton(R.string.init_update_number_dialog_neutral, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Pref.putBoolean(Pref.SHOW_NUMBER_CHANGED_DIALOG, false);
                        startMainActivity();
                    }
                });
                dialog.show();
            }
        }
    }

    private void fetchEmergencyNumbersData() {
        String countryCode = NetworkUtils.getUserCountry(this);
        if (countryCode == null || countryCode.isEmpty()) {
            showProgress(false);
            verifiedInit();
            return;
        }

        Call<EmergencyNumbersPOJO> call = EmergencyNumbersApi.getInstance().getService().getNumbers(countryCode);
        call.enqueue(new Callback<EmergencyNumbersPOJO>() {
            @Override
            public void onResponse(Call<EmergencyNumbersPOJO> call, Response<EmergencyNumbersPOJO> response) {
                if (response.isSuccessful()) {
                    String errorMsg = response.body().getError();
                    if (errorMsg == null || errorMsg.isEmpty()) {
                        EmergencyNumbersPOJO.DataBean dataBean = response.body().getData();
                        EmergencyNumbersPOJO.DataBean.DispatchBean dispatchBean = dataBean.getDispatch();
                        if (dataBean.isMember_112()) {
                            fetchedEmergencyNumber = "112";
                        } else if (dispatchBean.getAll() != null && !dispatchBean.getAll().isEmpty() && !dispatchBean.getAll().get(0).isEmpty()) {
                            fetchedEmergencyNumber = dispatchBean.getAll().get(0);
                        } else if (dispatchBean.getGsm() != null && !dispatchBean.getGsm().isEmpty() && !dispatchBean.getGsm().get(0).isEmpty()) {
                            fetchedEmergencyNumber = dispatchBean.getGsm().get(0);
                        } else if (dataBean.getPolice().getAll() != null && !dataBean.getPolice().getAll().isEmpty() && !dataBean.getPolice().getAll().get(0).isEmpty()) {
                            fetchedEmergencyNumber = dataBean.getPolice().getAll().get(0);
                        } else if (dataBean.getAmbulance().getAll() != null && !dataBean.getAmbulance().getAll().isEmpty() && !dataBean.getAmbulance().getAll().get(0).isEmpty()) {
                            fetchedEmergencyNumber = dataBean.getAmbulance().getAll().get(0);
                        }

                    } else {
                        Crashlytics.logException(new NetworkErrorException(call.request().toString()));
                    }
                } else {
                    Crashlytics.logException(new NetworkErrorException(call.request().toString()));
                }

                showProgress(false);
                verifiedInit();
            }

            @Override
            public void onFailure(final Call<EmergencyNumbersPOJO> call, final Throwable t) {
                NetworkUtils.hasActiveInternetConnection(new NetworkUtils.NetworkStateListener() {
                    @Override
                    public void hasNetworkConnection(boolean result) {
                        if (result) {
                            Crashlytics.logException(new NetworkErrorException(call.request().toString() + " - has internet"));
                        } else {
                            Crashlytics.logException(new NetworkErrorException(call.request().toString() + " - no internet"));
                        }
                    }
                });

                showProgress(false);
                verifiedInit();
            }
        });
    }

    @Override
    public void startStart() {
        canBack = false;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, StartFragment.newInstance(), StartFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void beginClicked() {
        canBack = true;
        shrinkLogo();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, BeginFragment.newInstance(fetchedEmergencyNumber), BeginFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void startMainActivity() {
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        if (!startIntent.getAction().equals("android.intent.action.MAIN") && startIntent.getExtras() != null) {
            intent.setAction(startIntent.getAction());
            intent.setFlags(startIntent.getFlags());
            intent.putExtras(startIntent.getExtras());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        startActivity(intent);

        finish();
    }

    @Override
    public void onBackPressed() {
        if (canBack) {
            growLogo();
            startStart();
        } else
            super.onBackPressed();
    }

    @Override
    public void showProgress(final boolean show) {
        if (isShrunk) {
            growLogo();
        }

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        contentFrame.setVisibility(show ? View.GONE : View.VISIBLE);
        contentFrame.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                contentFrame.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void shrinkLogo() {
        logo.clearAnimation();
        logo.setAnimation(shrinkAnimation);
        logo.getAnimation().start();

        isShrunk = true;
    }

    @Override
    public void growLogo() {
        logo.clearAnimation();
        logo.setAnimation(growAnimation);
        logo.getAnimation().start();

        isShrunk = false;
    }
}
