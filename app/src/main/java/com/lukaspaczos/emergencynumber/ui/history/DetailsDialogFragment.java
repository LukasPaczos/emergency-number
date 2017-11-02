package com.lukaspaczos.emergencynumber.ui.history;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.ui.messages.MessagesFragment;
import com.lukaspaczos.emergencynumber.ui.report.Report;
import com.lukaspaczos.emergencynumber.ui.report.fragment.AbstractCategoryFragment;
import com.lukaspaczos.emergencynumber.ui.report.fragment.SummaryFragment;

/**
 * Created by Lukas Paczos on 03-May-17
 */

public class DetailsDialogFragment extends DialogFragment implements AbstractCategoryFragment.OnCategoryFragmentInteractionListener {

    private static final String TAG = "DetailsDialogFragment";
    private static final String REPORT_ARG = "report_arg";
    private static final String TIMESTAMP_ARG = "timestamp_arg";

    private Report report;
    private long timestamp;

    public DetailsDialogFragment() {
        // Required empty public constructor
    }

    public static DetailsDialogFragment newInstance(Report report, long timestamp) {
        DetailsDialogFragment fragment = new DetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(REPORT_ARG, report);
        args.putLong(TIMESTAMP_ARG, timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            report = getArguments().getParcelable(REPORT_ARG);
            timestamp = getArguments().getLong(TIMESTAMP_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_fragment_details, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.history_details));

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);

        NestedScrollView nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.details_scroll_view);
        nestedScrollView.setNestedScrollingEnabled(false);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.details_content, SummaryFragment.newInstance(report, true, timestamp))
                .commit();

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.details_chat, MessagesFragment.newInstance(true, timestamp))
                .commit();

        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // handle close button click here
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getActivity().getSupportFragmentManager().popBackStack();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadCategory(String chosenCategoryStringRes) {

    }

    @Override
    public void loadVictimCategory() {

    }

    @Override
    public void loadLocationCategory() {

    }

    @Override
    public void loadSummary() {

    }

    @Override
    public void invalidateChosenLayout() {

    }

    @Override
    public void sendReport() {

    }

    @Override
    public void showChosenCategories(boolean show) {

    }
}
