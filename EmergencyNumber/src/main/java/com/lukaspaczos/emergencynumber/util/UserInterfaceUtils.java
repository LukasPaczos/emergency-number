package com.lukaspaczos.emergencynumber.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lukaspaczos.emergencynumber.App;

/**
 * Created by Lukas Paczos on 03-May-17
 */

public final class UserInterfaceUtils {

    public static void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
