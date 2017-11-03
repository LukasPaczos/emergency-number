package com.lukaspaczos.emergencynumber.ui.report.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lukaspaczos.emergencynumber.ui.AppFragment;
import com.lukaspaczos.emergencynumber.ui.report.Report;

public abstract class AbstractCategoryFragment extends AppFragment {
    public static final long TRANSITION_ANIMATION_DURATION = 300L; //milliseconds
    protected static final String ARG_REPORT = "arg_report";

    protected OnCategoryFragmentInteractionListener mListener;

    protected Report report;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            report = (Report) getArguments().getParcelable(ARG_REPORT);
        }

        if (getParentFragment() instanceof OnCategoryFragmentInteractionListener) {
            mListener = (OnCategoryFragmentInteractionListener) getParentFragment();
        } else {
            throw new RuntimeException(getParentFragment().getTag()
                    + " must implement OnCategoryFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (!(this instanceof MainCategoriesFragment) && !(this instanceof SubCategoriesFragment)) {
            view.setAlpha(0f);
            view.animate().alpha(1f).setDuration(TRANSITION_ANIMATION_DURATION).start();
        }

        mListener.invalidateChosenLayout();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_REPORT, report);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }



    public interface OnCategoryFragmentInteractionListener {
        void loadCategory(String chosenCategoryString);

        void loadVictimCategory();

        void loadLocationCategory();

        void loadSummary();

        void invalidateChosenLayout();

        void sendReport();

        void showChosenCategories(boolean show);
    }
}
