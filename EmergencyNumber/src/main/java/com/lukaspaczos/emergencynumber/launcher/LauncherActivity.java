package com.lukaspaczos.emergencynumber.launcher;

import android.Manifest;
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

import com.lukaspaczos.emergencynumber.MainActivity;
import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.util.Pref;
import com.lukaspaczos.emergencynumber.util.ResizeAnimation;

public class LauncherActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static final int PERMISSIONS_REQUEST_CODE = 2536;

    private Intent startIntent;

    private View contentFrame;

    private ProgressBar mProgressView;

    private ImageView logo;
    private Animation shrinkAnimation;
    private Animation growAnimation;
    private boolean isShrunk = false;
    private boolean canBack = false;

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

        if (
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.SEND_SMS},
                    PERMISSIONS_REQUEST_CODE);
        }

        verifiedInit(savedInstanceState);
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
                        ActivityCompat.requestPermissions(LauncherActivity.this,
                                new String[]{
                                        Manifest.permission.READ_PHONE_STATE,
                                        Manifest.permission.ACCESS_NETWORK_STATE,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.RECEIVE_SMS,
                                        Manifest.permission.SEND_SMS},
                                PERMISSIONS_REQUEST_CODE);
                    }
                });
                dialog.show();

                return;
            }
        }
    }

    private void verifiedInit(final Bundle savedInstanceState) {
        final String name = Pref.getString(Pref.NAME, "");
        final String number = Pref.getString(Pref.EMERGENCY_PHONE_NUMBER, "");

        if (!name.isEmpty() && !number.isEmpty()) {
            startMainActivity();
        } else if (savedInstanceState == null) {
            startStart();
        }
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
                .replace(R.id.content_frame, BeginFragment.newInstance(), BeginFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

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
