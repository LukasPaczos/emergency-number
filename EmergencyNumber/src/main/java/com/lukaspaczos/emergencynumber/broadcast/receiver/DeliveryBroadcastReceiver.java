package com.lukaspaczos.emergencynumber.broadcast.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;

import com.lukaspaczos.emergencynumber.App;
import com.lukaspaczos.emergencynumber.MainActivity;
import com.lukaspaczos.emergencynumber.launcher.LauncherActivity;
import com.lukaspaczos.emergencynumber.ui.messages.database.DatabaseHelper;
import com.lukaspaczos.emergencynumber.ui.report.Report;

/**
 * Created by Lukas Paczos on 21-Mar-17
 */

public class DeliveryBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(MainActivity.ACTION_MESSAGE_DELIVERY)) {

            Intent activityStartIntent;
            if (((App) App.getContext()).isLoggedIn()) {
                activityStartIntent = new Intent(App.getContext(), MainActivity.class);
            } else {
                activityStartIntent = new Intent(App.getContext(), LauncherActivity.class);
            }

            activityStartIntent.setAction(MainActivity.ACTION_MESSAGE_DELIVERY);
            activityStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        String message = bundle.getString(MainActivity.MESSAGE_CONTENT_ARG);
                        Report report = bundle.getParcelable(MainActivity.MESSAGE_REPORT_ARG);
                        message = DatabaseUtils.sqlEscapeString(message);
                        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
                        databaseHelper.initialize();
                        if (report != null) {
                            report.setLocation(DatabaseUtils.sqlEscapeString(report.getLocation()));
                            databaseHelper.insertMyMessage(message, report, 1);
                        } else {
                            databaseHelper.insertMyMessage(message, null, 0);
                        }
                    }
                    activityStartIntent.putExtra(MainActivity.MESSAGE_DELIVERY_RESULT, true);
                    break;
                case Activity.RESULT_CANCELED:
                    activityStartIntent.putExtra(MainActivity.MESSAGE_DELIVERY_RESULT, false);
                    break;
            }

            App.getContext().startActivity(activityStartIntent);
        }
    }
}
