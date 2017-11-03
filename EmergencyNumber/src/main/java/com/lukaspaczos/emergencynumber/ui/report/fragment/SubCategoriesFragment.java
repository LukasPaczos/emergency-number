package com.lukaspaczos.emergencynumber.ui.report.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;
import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.ui.report.Report;
import com.lukaspaczos.emergencynumber.ui.report.ReportFragment;
import com.lukaspaczos.emergencynumber.ui.report.category.ReportCategoriesFactory;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;

import java.util.List;

public class SubCategoriesFragment extends AbstractCategoryFragment {

    private static final String ARG_PARENT = "parent";

    private String parentString;

    private View rootView;
    private FlexboxLayout subCategoryContainer;
    private List<View> categoryViews;

    public SubCategoriesFragment() {
        // Required empty public constructor
    }

    public static SubCategoriesFragment newInstance(String parentStringRes, Report report) {
        SubCategoriesFragment fragment = new SubCategoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARENT, parentStringRes);
        args.putParcelable(ARG_REPORT, report);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.parentString = getArguments().getString(ARG_PARENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_sub_categories, container, false);
        subCategoryContainer = (FlexboxLayout) rootView.findViewById(R.id.sub_categories);

        categoryViews =
                ReportCategoriesFactory.inflateViews(
                        inflater,
                        subCategoryContainer,
                        R.layout.view_category_small,
                        parentString);

        for (int i = 0; i < categoryViews.size(); i++) {
            categoryViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    report.addCategory((String) v.getTag());

                    TransitionManager.beginDelayedTransition(
                            (ViewGroup) rootView,
                            new Slide(Gravity.TOP)
                                    .setDuration(ReportFragment.SLIDE_IN_ANIMATION_DURATION)
                                    .addListener(new Transition.TransitionListenerAdapter() {
                                        @Override
                                        public void onTransitionEnd(Transition transition) {
                                            if (ReportCategoriesFactory.getChildCount((String) v.getTag()) > 0)
                                                mListener.loadCategory((String) v.getTag());
                                            else
                                                mListener.loadVictimCategory();
                                        }
                                    }));

                    for (View categoryView : categoryViews) {
                        categoryView.setVisibility(View.INVISIBLE);
                    }
                }
            });

            if (i > 0 && i % 3 == 0) {
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(categoryViews.get(i).getLayoutParams());
                params.wrapBefore = true;
                categoryViews.get(i).setLayoutParams(params);
            }

            subCategoryContainer.addView(categoryViews.get(i));
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.post(new Runnable() {
            @Override
            public void run() {
                if (mListener != null)
                    mListener.showChosenCategories(true);

                TransitionManager.beginDelayedTransition(
                        (ViewGroup) rootView,
                        new Slide(Gravity.TOP)
                                .setDuration(ReportFragment.SLIDE_IN_ANIMATION_DURATION));

                for (View categoryView : categoryViews) {
                    categoryView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
