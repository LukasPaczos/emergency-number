package com.lukaspaczos.emergencynumber.ui.report.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.location.MyLocationManager;
import com.lukaspaczos.emergencynumber.ui.report.Report;

/**
 * Created by Lukas Paczos on 16-Mar-17
 */

public class LocationFragment extends AbstractCategoryFragment implements MyLocationManager.LocationManagerCallbacks {

    private View rootView;

    private ProgressBar progressBar;
    private View contentCurrentLocation;
    private Button getCurrentLocationBtn;
    private Button confirmAdressButton;
    private EditText adressView;

    private MyLocationManager locationManager;

    public LocationFragment() {
        // Required empty public constructor
    }

    public static LocationFragment newInstance(Report report) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_REPORT, report);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = new MyLocationManager(getActivity(), this);
    }

    @Override
    public void onPause() {
        super.onPause();
        contentCurrentLocation.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        locationManager.initialize();
    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.uninitialize();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_location, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.location_progress_bar);

        contentCurrentLocation = rootView.findViewById(R.id.location_content_current_location);

        getCurrentLocationBtn = (Button) rootView.findViewById(R.id.location_use_current_location_btn);
        getCurrentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentCurrentLocation.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                locationManager.requestLocation(getActivity(), true);
            }
        });

        adressView = (EditText) rootView.findViewById(R.id.location_adress);
        confirmAdressButton = (Button) rootView.findViewById(R.id.location_confirm_btn);
        confirmAdressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSummary(adressView.getText().toString());
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListener.showChosenCategories(true);
    }

    private void goToSummary(String adress) {
        if (adress != null && adress.trim().length() > 5) {
            report.setLocation(adress.trim());
            mListener.loadSummary();
        } else {
            Toast.makeText(getActivity(), R.string.location_wrong_address, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNewLocation(final Location location) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (location != null) {
                    locationManager.uninitialize();
                    report.setLocation(String.format("%.6f, %.6f", location.getLatitude(), location.getLongitude()));
                    mListener.loadSummary();
                } else {
                    Toast.makeText(getActivity(), R.string.gps_not_found_error_no_location, Toast.LENGTH_LONG).show();
                    contentCurrentLocation.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onGpsUnavailable() {
//        Toast.makeText(getActivity(), R.string.gps_not_found_error, Toast.LENGTH_LONG).show();

        contentCurrentLocation.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), R.string.error_unknown, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        progressBar.setVisibility(View.GONE);
        contentCurrentLocation.setAlpha(0f);
        contentCurrentLocation.setVisibility(View.VISIBLE);
        contentCurrentLocation.animate().alpha(1f).setDuration(TRANSITION_ANIMATION_DURATION).start();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), R.string.error_unknown, Toast.LENGTH_LONG).show();
    }
}
