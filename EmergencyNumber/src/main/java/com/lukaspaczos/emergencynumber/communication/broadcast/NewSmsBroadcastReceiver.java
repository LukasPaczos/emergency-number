package com.lukaspaczos.emergencynumber.communication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.lukaspaczos.emergencynumber.App;
import com.lukaspaczos.emergencynumber.MainActivity;
import com.lukaspaczos.emergencynumber.launcher.LauncherActivity;
import com.lukaspaczos.emergencynumber.ui.messages.database.DatabaseHelper;
import com.lukaspaczos.emergencynumber.util.Pref;
import com.lukaspaczos.emergencynumber.util.StringUtils;

/**
 * Created by Lukas Paczos on 22-Mar-17
 */

public class NewSmsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null)
            return;

        Intent activityStartIntent;
        if (((App)App.getContext()).isLoggedIn()) {
            activityStartIntent = new Intent(App.getContext(), MainActivity.class);
        } else {
            activityStartIntent = new Intent(App.getContext(), LauncherActivity.class);
        }

        activityStartIntent.setAction(MainActivity.ACTION_NEW_SMS);
        activityStartIntent.putExtra(MainActivity.ACTION_ARG, MainActivity.ACTION_NEW_SMS);
        activityStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        boolean startActivity = false;
        SmsMessage[] messages  = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        String contents = "";
        for (SmsMessage message : messages) {
            String originatingAddress = StringUtils.removeLocalized(message.getDisplayOriginatingAddress());

            if (originatingAddress.equals(Pref.getString(Pref.EMERGENCY_PHONE_NUMBER, ""))) {
                contents += message.getMessageBody();
                startActivity = true;
            }
        }

        if (startActivity && !contents.isEmpty()) {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
            databaseHelper.initialize();
            databaseHelper.insert112Message(contents);
            App.getContext().startActivity(activityStartIntent);
        }
    }
}
