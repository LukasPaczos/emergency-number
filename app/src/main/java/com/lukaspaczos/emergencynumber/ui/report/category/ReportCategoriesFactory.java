package com.lukaspaczos.emergencynumber.ui.report.category;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lukaspaczos.emergencynumber.App;
import com.lukaspaczos.emergencynumber.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Lukas Paczos on 05-Mar-17
 */

public class ReportCategoriesFactory {

    public static List<View> inflateViews(LayoutInflater inflater, View parent, int childLayoutRes, String parentCategoryString) {
        List<View> views = new ArrayList<>();
        for (Map.Entry<String, String> category : ReportCategoriesConf.categories.entrySet()) {
            View view = inflater.inflate(childLayoutRes, (ViewGroup) parent, false);
            if (category.getValue().equals(parentCategoryString) ||
                    (category.getValue().equals(App.getContext().getResources().getString(R.string.universal_category))
                            && !parentCategoryString.equals(App.getContext().getString(R.string.category_none)))) {

                TextView descriptionTv = (TextView) view.findViewById(R.id.description);

                String description = category.getKey();
                descriptionTv.setText(description);

                view.setTag(category.getKey());

                ImageView icon = (ImageView) view.findViewById(R.id.icon);
                int iconResource = ReportCategoriesConf.icons.get(category.getKey());
                icon.setImageResource(iconResource);

                views.add(view);
            }
        }

        return views;
    }

    public static View inflateChosenView(LayoutInflater inflater, View parent, int layoutRes, String categoryNameString) {
        View view = inflater.inflate(layoutRes, (ViewGroup) parent, false);
        view.setTag(categoryNameString);

        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        int iconResource = ReportCategoriesConf.icons.get(categoryNameString);
        icon.setImageResource(iconResource);
        icon.setVisibility(View.VISIBLE);

        return view;
    }

    public static View inflateChosenView(LayoutInflater inflater, View parent, int layoutRes, Drawable iconDrawable) {
        View view = inflater.inflate(layoutRes, (ViewGroup) parent, false);

        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageDrawable(iconDrawable);
        icon.setVisibility(View.VISIBLE);

        return view;
    }

    public static View inflateChosenVictimView(LayoutInflater inflater, View parent, int layoutRes, String number) {
        View view = inflater.inflate(layoutRes, (ViewGroup) parent, false);

        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(number);
        text.setVisibility(View.VISIBLE);

        return view;
    }

    public static int getChildCount(String categoryString) {
        int count = 0;
        for (Map.Entry<String, String> category : ReportCategoriesConf.categories.entrySet()) {
            if (category.getValue().equals(categoryString)) {
                count++;
            }
        }
        return count;
    }

    public static List<String> getCategoriesTree(String categoryString) {
        List<String> tree = new ArrayList<>();
        fillTree(categoryString, tree);
        Collections.reverse(tree);
        return tree;
    }

    private static void fillTree(String categoryString, List<String> tree) {
        if (categoryString.equals(App.getContext().getString(R.string.category_none))) {
            return;
        }
        tree.add(categoryString);
        fillTree(findParent(categoryString), tree);
    }

    private static String findParent(String categoryString) {
        return ReportCategoriesConf.categories.get(categoryString);
    }

    public static int getIconDrawableRes(String categoryStringRes) {
        return ReportCategoriesConf.icons.get(categoryStringRes);
    }
}
