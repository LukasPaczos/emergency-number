package com.lukaspaczos.emergencynumber.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * Created by Lukas Paczos on 12-Mar-17
 */

public abstract class AppFragment extends Fragment {
    protected static final long TRANSITION_ANIMATION_DURATION = 300L; //milliseconds

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.setAlpha(0f);
        view.animate().alpha(1f).setDuration(TRANSITION_ANIMATION_DURATION).start();
    }

    /**
     * <b>WORKAROUND</b> because popping from backstack of nested fragments is broken
     *
     * @param fm fragment manager
     * @return true if handled backpress, or false if didn't
     */
    public static boolean handleBackPressed(FragmentManager fm) {
        if (fm.getFragments() != null) {
            for (Fragment frag : fm.getFragments()) {
                if (frag != null && frag.isVisible() && frag instanceof AppFragment) {
                    if (((AppFragment) frag).onBackPressed()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean onBackPressed() {
        FragmentManager fm = getChildFragmentManager();
        if (handleBackPressed(fm)) {
            return true;
        } else if (getUserVisibleHint() && fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            return true;
        }
        return false;
    }
}
