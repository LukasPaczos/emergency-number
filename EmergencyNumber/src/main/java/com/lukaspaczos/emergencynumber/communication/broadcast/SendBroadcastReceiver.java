package com.lukaspaczos.emergencynumber.communication.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lukaspaczos.emergencynumber.App;
import com.lukaspaczos.emergencynumber.MainActivity;
import com.lukaspaczos.emergencynumber.launcher.LauncherActivity;

/**
 * Created by Lukas Paczos on 21-Mar-17
 */

public class SendBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(MainActivity.ACTION_MESSAGE_SEND)) {

            Intent activityStartIntent;
            if (((App)App.getContext()).isLoggedIn()) {
                activityStartIntent = new Intent(App.getContext(), MainActivity.class);
            } else {
                activityStartIntent = new Intent(App.getContext(), LauncherActivity.class);
            }

            activityStartIntent.setAction(MainActivity.ACTION_MESSAGE_SEND);
            activityStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    activityStartIntent.putExtra(MainActivity.MESSAGE_SEND_RESULT, true);
                    break;

                default:
                    activityStartIntent.putExtra(MainActivity.MESSAGE_SEND_RESULT, false);
                    break;
            }

            App.getContext().startActivity(activityStartIntent);
        }
    }
}
