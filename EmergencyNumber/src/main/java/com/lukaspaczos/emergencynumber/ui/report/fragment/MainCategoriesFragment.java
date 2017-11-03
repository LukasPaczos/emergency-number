package com.lukaspaczos.emergencynumber.ui.report.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;
import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.ui.report.Report;
import com.lukaspaczos.emergencynumber.ui.report.category.ReportCategoriesFactory;

import java.util.List;

public class MainCategoriesFragment extends AbstractCategoryFragment {

    private FlexboxLayout mainCategoryContainer;
    private List<View> categoryViews;
    private View rootView;

    public MainCategoriesFragment() {
        // Required empty public constructor
    }

    public static MainCategoriesFragment newInstance(Report report) {
        MainCategoriesFragment fragment = new MainCategoriesFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_REPORT, report);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View parentView = getParentFragment().getView();
        if (parentView != null) {
            mListener.showChosenCategories(false);
        }

        rootView = inflater.inflate(R.layout.fragment_main_categories, container, false);
        mainCategoryContainer = (FlexboxLayout) rootView.findViewById(R.id.main_categories);

        categoryViews =
                ReportCategoriesFactory.inflateViews(
                        inflater,
                        mainCategoryContainer,
                        R.layout.view_category_big,
                        getString(R.string.category_none));

        for (int i = 0; i < categoryViews.size(); i++) {
            categoryViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    report.addCategory((String) v.getTag());
                    mListener.loadCategory((String) v.getTag());
                }
            });

            if (i > 0 && i % 2 == 0) {
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(categoryViews.get(i).getLayoutParams());
                params.wrapBefore = true;
                categoryViews.get(i).setLayoutParams(params);
            }

            mainCategoryContainer.addView(categoryViews.get(i));
        }
        return rootView;
    }
}
