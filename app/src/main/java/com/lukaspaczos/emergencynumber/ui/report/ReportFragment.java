package com.lukaspaczos.emergencynumber.ui.report;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lukaspaczos.emergencynumber.App;
import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.ui.AbstractTabFragment;
import com.lukaspaczos.emergencynumber.ui.report.category.ReportCategoriesFactory;
import com.lukaspaczos.emergencynumber.ui.report.fragment.AbstractCategoryFragment;
import com.lukaspaczos.emergencynumber.ui.report.fragment.LocationFragment;
import com.lukaspaczos.emergencynumber.ui.report.fragment.MainCategoriesFragment;
import com.lukaspaczos.emergencynumber.ui.report.fragment.SubCategoriesFragment;
import com.lukaspaczos.emergencynumber.ui.report.fragment.SummaryFragment;
import com.lukaspaczos.emergencynumber.ui.report.fragment.VictimCountFragment;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;

public class ReportFragment extends AbstractTabFragment implements AbstractCategoryFragment.OnCategoryFragmentInteractionListener {
    public static final String TAG = "REPORT_FRAGMENT";
    protected static final String ARG_REPORT = "arg_report";

    public static final long SLIDE_IN_ANIMATION_DURATION = 300L;

    private View rootView;

    private Report report;
    private View categories;
    private View chosenCategoriesLayout;
    private LinearLayout chosenCategoriesContainer;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            report = (Report) savedInstanceState.getParcelable(ARG_REPORT);
        } else
            report = new Report();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_REPORT, report);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_report, container, false);
        categories = rootView.findViewById(R.id.categories);
        chosenCategoriesLayout = rootView.findViewById(R.id.chosen_categories_layout);
        chosenCategoriesContainer = (LinearLayout) rootView.findViewById(R.id.chosen_categories);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.categories, MainCategoriesFragment.newInstance(report))
                .commit();
    }

    @Override
    public void loadCategory(String chosenCategoryString) {

        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.categories, SubCategoriesFragment.newInstance(chosenCategoryString, report))
                .commit();
    }

    @Override
    public void loadVictimCategory() {
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.categories, VictimCountFragment.newInstance(report))
                .commit();
    }

    @Override
    public void loadLocationCategory() {
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.categories, LocationFragment.newInstance(report))
                .commit();
    }

    @Override
    public void loadSummary() {
        getChildFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.categories, SummaryFragment.newInstance(report))
                .commit();
    }

    @Override
    public void invalidateChosenLayout() {
        LayoutInflater inflater = (LayoutInflater) App.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;

        int childCount = chosenCategoriesContainer.getChildCount();
        int diff = report.getSelectedCount() - childCount;

        if (diff < 0) {

            diff = -diff;
            for (int i = childCount - 1; i >= childCount - diff; i--) {
                chosenCategoriesContainer.removeViewAt(i);
            }

        } else if (report.getLocation() != null) {

            view = ReportCategoriesFactory.inflateChosenView(
                    inflater,
                    chosenCategoriesContainer,
                    R.layout.view_chosen_category,
                    ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_location));

        } else if (report.getVictimCount() != null) {

            view = ReportCategoriesFactory.inflateChosenVictimView(
                    inflater,
                    chosenCategoriesContainer,
                    R.layout.view_chosen_category,
                    report.getVictimCount());

        } else if (report.getCategories().size() > 0) {

            String category = report.getCategories().get(report.getCategories().size() - 1);
            view = ReportCategoriesFactory.inflateChosenView(
                    inflater,
                    chosenCategoriesContainer,
                    R.layout.view_chosen_category,
                    category);

        }

        if (view != null) {
            view.setAlpha(0f);
            chosenCategoriesContainer.addView(view);
            view.animate().alpha(1f).setDuration(AbstractCategoryFragment.TRANSITION_ANIMATION_DURATION).start();
        }
    }

    @Override
    public void sendReport() {
        String content = report.toString();
        if (Report.sendMessage(getActivity(), content, report)) {
            mListener.notifyMessageSent();
            Toast.makeText(getActivity(), R.string.report_send_try,
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), R.string.error_sending_report,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showChosenCategories(boolean show) {
        if (show) {
            TransitionManager.beginDelayedTransition((ViewGroup) rootView, new Fade().setDuration(SLIDE_IN_ANIMATION_DURATION));
            chosenCategoriesLayout.setVisibility(View.VISIBLE);
        } else {
            TransitionManager.beginDelayedTransition((ViewGroup) rootView, new Slide(Gravity.TOP).setDuration(SLIDE_IN_ANIMATION_DURATION));
            chosenCategoriesLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean onBackPressed() {
        report.onBackPressed();
        return super.onBackPressed();
    }
}
