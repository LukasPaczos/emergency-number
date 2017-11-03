package com.lukaspaczos.emergencynumber.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringDef;

import com.lukaspaczos.emergencynumber.App;
import com.lukaspaczos.emergencynumber.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

/**
 * Created by Lukas Paczos on 01-Nov-17
 */

public class Pref {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            NAME, EMERGENCY_PHONE_NUMBER, EMAIL
    })

    public @interface Type {}

    public static final String NAME = "pref_name";
    public static final String EMERGENCY_PHONE_NUMBER = "pref_emergency_phone_number";
    public static final String EMAIL = "pref_email";

    private static SharedPreferences preferences = App.getContext().getSharedPreferences(App.getContext()
            .getString(R.string.preferences_DB_name), Context.MODE_PRIVATE);

    public static void putInt(@Type String type, Integer value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(type, value);
        editor.apply();
    }

    public static void putBoolean(@Type String type, Boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(type, value);
        editor.apply();
    }

    public static void putFloat(@Type String type, Float value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(type, value);
        editor.apply();
    }

    public static void putLong(@Type String type, Long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(type, value);
        editor.apply();
    }

    public static void putString(@Type String type, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(type, value);
        editor.apply();
    }

    public static void putStringSet(@Type String type, Set<String> value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(type, value);
        editor.apply();
    }

    public static Integer getInt(@Type String type, Integer defValue) {
        return preferences.getInt(type, defValue);
    }

    public static Boolean getBoolean(@Type String type, Boolean defValue) {
        return preferences.getBoolean(type, defValue);
    }

    public static Float getFloat(@Type String type, Float defValue) {
        return preferences.getFloat(type, defValue);
    }

    public static Long getLong(@Type String type, Long defValue) {
        return preferences.getLong(type, defValue);
    }

    public static String getString(@Type String type, String defValue) {
        return preferences.getString(type, defValue);
    }

    public static Set<String> getStringSet(@Type String type, Set<String> defValue) {
        return preferences.getStringSet(type, defValue);
    }
}
