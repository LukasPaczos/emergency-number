package com.lukaspaczos.emergencynumber;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.lukaspaczos.emergencynumber.ui.report.category.ReportCategoriesConf;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Lukas Paczos on 06-Mar-17
 */

public class App extends Application {

    private static Context mContext;
    private boolean isLoggedIn = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mContext = this.getApplicationContext();

        ReportCategoriesConf.initialize();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static Context getContext() {
        return mContext;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
}
