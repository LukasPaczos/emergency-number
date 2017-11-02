package com.lukaspaczos.emergencynumber.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.lukaspaczos.emergencynumber.App;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Lukas Paczos on 09-May-17
 */

public class NetworkUtils {

    public static void hasActiveInternetConnection(final NetworkStateListener listener) {
        if (isNetworkAvailable()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection urlc = (HttpURLConnection)
                                (new URL("http://clients3.google.com/generate_204")
                                        .openConnection());
                        urlc.setRequestProperty("User-Agent", "Test");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(1500);
                        urlc.connect();
                        boolean result = urlc.getResponseCode() == 204 && urlc.getContentLength() == 0;
                        listener.hasNetworkConnection(result);
                    } catch (IOException e) {
                        listener.hasNetworkConnection(false);
                        Log.e("NetworkUtils", "Error checking internet connection", e);
                    }
                }
            }).start();
        } else {
            listener.hasNetworkConnection(false);
        }
    }

    private static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public interface NetworkStateListener {
        void hasNetworkConnection(boolean result);
    }

}
