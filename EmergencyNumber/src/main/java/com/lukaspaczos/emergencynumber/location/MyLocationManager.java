package com.lukaspaczos.emergencynumber.location;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.lukaspaczos.emergencynumber.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Lukas Paczos on 14-May-17
 */

public class MyLocationManager implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private Timer timer;
    private List<Location> locations = new ArrayList<>();
    private GoogleApiClient mGoogleApiClient;

    private LocationManagerCallbacks callbacks;

    public MyLocationManager(Context context, LocationManagerCallbacks callbacks) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();

        this.callbacks = callbacks;
    }

    public void initialize() {
        mGoogleApiClient.connect();
    }

    public void uninitialize() {
        if (timer != null)
            timer.cancel();

        mGoogleApiClient.disconnect();
    }

    private Location getLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    public void requestLocation(final Context context, boolean locationRequired) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationRequired) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.gps_not_found_title);
            builder.setMessage(R.string.gps_not_found_message);
            builder.setPositiveButton(R.string.gps_not_found_confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.setNegativeButton(R.string.gps_not_found_cancel, null);
            builder.show();
            callbacks.onGpsUnavailable();
            return;
        }
        locations.clear();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                locations.add(getLocation());

                if (locations.size() >= 4) {
                    Location resultLocation = locations.get(0);
                    for (Location location : locations) {
                        if (resultLocation != null && location != null
                                && resultLocation.hasAccuracy() && location.hasAccuracy()
                                && location.getAccuracy() < resultLocation.getAccuracy()) {
                            resultLocation = location;
                        }
                    }

                    callbacks.onNewLocation(resultLocation);
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        callbacks.onConnected(bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {
        callbacks.onConnectionSuspended(i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        callbacks.onConnectionFailed(connectionResult);
    }

    public interface LocationManagerCallbacks {
        void onConnected(@Nullable Bundle bundle);

        void onConnectionSuspended(int i);

        void onConnectionFailed(@NonNull ConnectionResult connectionResult);

        void onNewLocation(Location location);

        void onGpsUnavailable();
    }
}
