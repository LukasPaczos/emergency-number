package com.lukaspaczos.emergencynumber.ui.report.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.ui.report.Report;
import com.lukaspaczos.emergencynumber.ui.report.category.ReportCategoriesConf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.grantland.widget.AutofitTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFragment extends AbstractCategoryFragment {

    private static final int SMS_PERMISSION_REQUEST_CODE = 2;
    private static final String IS_HISTORY_ARG = "is_history_arg";
    private static final String TIMESTAMP_ARG = "timestamp_arg";

    private boolean isHistory;
    private long timestamp;

    private ProgressBar progressBar;
    private Button sendButton;

    private View summaryContent;

    private ImageView mainCategoryIv;
    private LinearLayout subCategoriesContainer;
    private TextView time;
    private AutofitTextView location;
    private TextView victims;
    private ImageView eventResult1;
    private AutofitTextView eventResultTitle1;
    private ImageView eventResult2;
    private AutofitTextView eventResultTitle2;
    private ImageView eventResult3;
    private AutofitTextView eventResultTitle3;

    public SummaryFragment() {
        // Required empty public constructor
    }

    public static SummaryFragment newInstance(Report report) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_REPORT, report);
        fragment.setArguments(args);
        return fragment;
    }

    public static SummaryFragment newInstance(Report report, boolean isHistory, long timestamp) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_REPORT, report);
        args.putBoolean(IS_HISTORY_ARG, isHistory);
        args.putLong(TIMESTAMP_ARG, timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            isHistory = getArguments().getBoolean(IS_HISTORY_ARG);
            timestamp = getArguments().getLong(TIMESTAMP_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.summary_progress_bar);
        sendButton = (Button) view.findViewById(R.id.summary_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        if (isHistory)
            sendButton.setVisibility(View.INVISIBLE);

        summaryContent = view.findViewById(R.id.summary_content);

        mainCategoryIv = (ImageView) view.findViewById(R.id.summary_main_category);
        subCategoriesContainer = (LinearLayout) view.findViewById(R.id.summary_sub_categories);
        time = (TextView) view.findViewById(R.id.summary_time);
        location = (AutofitTextView) view.findViewById(R.id.summary_location);
        victims = (TextView) view.findViewById(R.id.summary_victims);
        eventResult1 = (ImageView) view.findViewById(R.id.summary_event_1);
        eventResultTitle1 = (AutofitTextView) view.findViewById(R.id.summary_event_title_1);
        eventResult2 = (ImageView) view.findViewById(R.id.summary_event_2);
        eventResultTitle2 = (AutofitTextView) view.findViewById(R.id.summary_event_title_2);
        eventResult3 = (ImageView) view.findViewById(R.id.summary_event_3);
        eventResultTitle3 = (AutofitTextView) view.findViewById(R.id.summary_event_title_3);

        mainCategoryIv.setImageDrawable(ContextCompat.getDrawable(getActivity(), ReportCategoriesConf.icons.get(report.getCategories().get(0))));

        for (int i = 1; i < report.getCategories().size(); i++) {
            ImageView iv = (ImageView) getActivity().getLayoutInflater().inflate(
                    R.layout.view_summary_sub_category,
                    subCategoriesContainer,
                    false
            );
            iv.setImageDrawable(ContextCompat.getDrawable(getActivity(), ReportCategoriesConf.icons.get(report.getCategories().get(i))));
            subCategoriesContainer.addView(iv);
        }

        SimpleDateFormat sdm = new SimpleDateFormat("EEE, HH:mm");
        time.setText(sdm.format(isHistory ? timestamp : new Date()));

        location.setText(report.getLocation());

        victims.setText(String.valueOf(report.getVictimCount()));

        List<String> victimTypes = new ArrayList<>(report.getVictimsIncludes().keySet());
        eventResult1.setImageDrawable(
                ContextCompat.getDrawable(getActivity(),
                        report.getVictimsIncludes().get(victimTypes.get(0)) ? R.drawable.ic_done : R.drawable.ic_clear));
        eventResultTitle1.setText(victimTypes.get(0));

        eventResult2.setImageDrawable(
                ContextCompat.getDrawable(getActivity(),
                        report.getVictimsIncludes().get(victimTypes.get(1)) ? R.drawable.ic_done : R.drawable.ic_clear));
        eventResultTitle2.setText(victimTypes.get(1));

        eventResult3.setImageDrawable(
                ContextCompat.getDrawable(getActivity(),
                        report.getVictimsIncludes().get(victimTypes.get(2)) ? R.drawable.ic_done : R.drawable.ic_clear));
        eventResultTitle3.setText(victimTypes.get(2));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListener.showChosenCategories(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                sendButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                Toast.makeText(getActivity(), R.string.permissions, Toast.LENGTH_LONG).show();
                return;
            }

            sendMessage();
        }
    }

    private void sendMessage() {
        summaryContent.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
            return;
        }
        mListener.sendReport();
    }

}
