package com.lukaspaczos.emergencynumber.ui.report;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.SmsManager;

import com.lukaspaczos.emergencynumber.App;
import com.lukaspaczos.emergencynumber.MainActivity;
import com.lukaspaczos.emergencynumber.util.Pref;
import com.lukaspaczos.emergencynumber.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lukas Paczos on 12-Mar-17
 */

public class Report implements Parcelable {
    private List<String> categories = new ArrayList<>();
    private String victimCount;
    private Map<String, Boolean> victimsIncludes = new LinkedHashMap<>();
    private String location;

    public void addCategory(String categoryStringRes) {
        categories.add(categoryStringRes);
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getVictimCount() {
        return victimCount;
    }

    public void setVictimCount(String victimCount) {
        this.victimCount = victimCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void addVictimType(String type, boolean result) {
        this.victimsIncludes.put(type, result);
    }

    public Map<String, Boolean> getVictimsIncludes() {
        return victimsIncludes;
    }

    public int getSelectedCount() {
        int count = 0;

        if (location != null)
            count++;

        if (victimCount != null)
            count++;

        count += categories.size();

        return count;
    }

    public void onBackPressed() {
        if (location != null)
            location = null;
        else if (victimCount != null)
            victimCount = null;
        else if (categories.size() > 0) {
            categories.remove(categories.size() - 1);
        }
    }

    public static boolean sendMessage(Context context, String content, Report report) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(MainActivity.MESSAGE_CONTENT_ARG, content);
            bundle.putParcelable(MainActivity.MESSAGE_REPORT_ARG, report);

            Intent intentSend = new Intent(MainActivity.ACTION_MESSAGE_SEND);
            intentSend.putExtras(bundle);
            SmsManager smsManager = SmsManager.getDefault();

            ArrayList<String> dividedMessages = smsManager.divideMessage(content);
            ArrayList<PendingIntent> sentIntents = new ArrayList<>(dividedMessages.size());
            for (int i = 0; i < dividedMessages.size(); i++) {
                sentIntents.add(PendingIntent.getBroadcast(
                        context,
                        MainActivity.NEW_MESSAGE_SENT_REQUSET_CODE,
                        intentSend,
                        PendingIntent.FLAG_CANCEL_CURRENT));
            }

            Intent intentDelivered = new Intent(MainActivity.ACTION_MESSAGE_DELIVERY);
            intentDelivered.putExtras(bundle);
            ArrayList<PendingIntent> receivedIntents = new ArrayList<>(dividedMessages.size());
            for (int i = 0; i < dividedMessages.size(); i++) {
                receivedIntents.add(PendingIntent.getBroadcast(
                        context,
                        MainActivity.NEW_MESSAGE_DELIVERED_REQUSET_CODE,
                        intentDelivered,
                        PendingIntent.FLAG_CANCEL_CURRENT));
            }

            smsManager.sendMultipartTextMessage(
                    Pref.getString(Pref.EMERGENCY_PHONE_NUMBER, ""),
                    null,
                    dividedMessages,
                    sentIntents,
                    receivedIntents
            );

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {

        String categoriesNames = "";
        for (String category : categories) {
            categoriesNames += category + "-";
        }
        categoriesNames = categoriesNames.substring(0, categoriesNames.length() - 1);

        String amongsVictims = "";
        for (String amongst : victimsIncludes.keySet()) {
            if (victimsIncludes.get(amongst))
                amongsVictims += amongst + ", ";
        }
        if (!amongsVictims.isEmpty())
            amongsVictims = amongsVictims.substring(0, amongsVictims.length() - 2);

        String name = Pref.getString(Pref.NAME, "");

        return name + "\n"
                + StringUtils.deAccent(categoriesNames) +
                "\nvictims:" + victimCount +
                ";info:" + StringUtils.deAccent(amongsVictims) +
                "\ngeo:" + location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.categories);
        dest.writeString(this.victimCount);
        dest.writeInt(this.victimsIncludes.size());
        for (Map.Entry<String, Boolean> entry : this.victimsIncludes.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeValue(entry.getValue());
        }
        dest.writeString(this.location);
    }

    public Report() {
    }

    protected Report(Parcel in) {
        this.categories = in.createStringArrayList();
        this.victimCount = in.readString();
        int victimsIncludesSize = in.readInt();
        this.victimsIncludes = new HashMap<>(victimsIncludesSize);
        for (int i = 0; i < victimsIncludesSize; i++) {
            String key = in.readString();
            Boolean value = (Boolean) in.readValue(Boolean.class.getClassLoader());
            this.victimsIncludes.put(key, value);
        }
        this.location = in.readString();
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel source) {
            return new Report(source);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
}
