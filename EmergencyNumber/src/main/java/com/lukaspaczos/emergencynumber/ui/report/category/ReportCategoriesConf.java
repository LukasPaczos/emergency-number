package com.lukaspaczos.emergencynumber.ui.report.category;

import android.content.res.Resources;

import com.lukaspaczos.emergencynumber.App;
import com.lukaspaczos.emergencynumber.R;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Lukas Paczos on 05-Mar-17
 */

public class ReportCategoriesConf {

    public static final Map<String, String> categories = new LinkedHashMap<>();

    public static final Map<String, Integer> icons = new HashMap<>();

    public static void initialize() {
        /*
         * MAIN CATEGORIES
         */
        Resources r = App.getContext().getResources();
        categories.put(r.getString(R.string.category_main_accident), r.getString(R.string.category_none));
        categories.put(r.getString(R.string.category_main_fire), r.getString(R.string.category_none));
        categories.put(r.getString(R.string.category_main_theft), r.getString(R.string.category_none));
        categories.put(r.getString(R.string.category_main_injury), r.getString(R.string.category_none));
        categories.put(r.getString(R.string.category_main_violence), r.getString(R.string.category_none));
        categories.put(r.getString(R.string.category_main_health), r.getString(R.string.category_none));

        /*
         * WYPADEK SUB-CATEGORIES
         */
        categories.put(r.getString(R.string.category_accident_road), r.getString(R.string.category_main_accident));
        categories.put(r.getString(R.string.category_drowning), r.getString(R.string.category_main_accident));
        categories.put(r.getString(R.string.category_electrocution), r.getString(R.string.category_main_accident));
        categories.put(r.getString(R.string.category_other_emergency), r.getString(R.string.category_main_accident));
        categories.put(r.getString(R.string.category_other_police), r.getString(R.string.category_main_accident));
        categories.put(r.getString(R.string.category_other_fire_dept), r.getString(R.string.category_main_accident));


        /*
         * POZAR SUB_CATEGORIES
         */
        categories.put(r.getString(R.string.category_building), r.getString(R.string.category_main_fire));
        categories.put(r.getString(R.string.category_chemical), r.getString(R.string.category_main_fire));
        categories.put(r.getString(R.string.category_nature), r.getString(R.string.category_main_fire));
        categories.put(r.getString(R.string.category_object), r.getString(R.string.category_main_fire));

        /*
         * WLAMANIE SUB_CATEGORIES
         */
        categories.put(r.getString(R.string.category_theft), r.getString(R.string.category_main_theft));
        categories.put(r.getString(R.string.category_murder), r.getString(R.string.category_main_theft));
        categories.put(r.getString(R.string.category_vandalism), r.getString(R.string.category_main_theft));
        categories.put(r.getString(R.string.category_burglary), r.getString(R.string.category_main_theft));

        /*
         * URAZ SUB_CATEGORIES
         */
        categories.put(r.getString(R.string.category_hemorrhage), r.getString(R.string.category_main_injury));
        categories.put(r.getString(R.string.category_burn), r.getString(R.string.category_main_injury));
        categories.put(r.getString(R.string.category_body_loss), r.getString(R.string.category_main_injury));
        categories.put(r.getString(R.string.category_fracture), r.getString(R.string.category_main_injury));

        /*
         * PRZEMOC SUB_CATEGORIES
         */
        categories.put(r.getString(R.string.category_physical), r.getString(R.string.category_main_violence));
        categories.put(r.getString(R.string.category_psychical), r.getString(R.string.category_main_violence));
        categories.put(r.getString(R.string.category_sexual), r.getString(R.string.category_main_violence));

        /*
         * ZDROWIE SUB_CATEGORIES
         */
        categories.put(r.getString(R.string.category_pain), r.getString(R.string.category_main_health));
        categories.put(r.getString(R.string.category_sickness), r.getString(R.string.category_main_health));
        categories.put(r.getString(R.string.category_allergy), r.getString(R.string.category_main_health));

        /*INNE - HAS TO BE LAST*/
        categories.put(r.getString(R.string.other), r.getString(R.string.universal_category));

        /*
         * CATEGORIES ICONS
         */
        icons.put(r.getString(R.string.other), R.drawable.ic_other);

        icons.put(r.getString(R.string.category_main_accident), R.drawable.ic_main_accident);
        icons.put(r.getString(R.string.category_main_fire), R.drawable.ic_main_fire);
        icons.put(r.getString(R.string.category_main_theft), R.drawable.ic_main_burglary);
        icons.put(r.getString(R.string.category_main_injury), R.drawable.ic_main_injury);
        icons.put(r.getString(R.string.category_main_violence), R.drawable.ic_main_violence);
        icons.put(r.getString(R.string.category_main_health), R.drawable.ic_main_health);

        icons.put(r.getString(R.string.category_accident_road), R.drawable.ic_road);
        icons.put(r.getString(R.string.category_drowning), R.drawable.ic_drowning);
        icons.put(r.getString(R.string.category_electrocution), R.drawable.ic_electrocution);
        icons.put(r.getString(R.string.category_other_emergency), R.drawable.ic_other_emergency);
        icons.put(r.getString(R.string.category_other_police), R.drawable.ic_other_police);
        icons.put(r.getString(R.string.category_other_fire_dept), R.drawable.ic_other_fire_dept);

        icons.put(r.getString(R.string.category_building), R.drawable.ic_building);
        icons.put(r.getString(R.string.category_chemical), R.drawable.ic_chemical);
        icons.put(r.getString(R.string.category_nature), R.drawable.ic_nature);
        icons.put(r.getString(R.string.category_object), R.drawable.ic_object);

        icons.put(r.getString(R.string.category_theft), R.drawable.ic_theft);
        icons.put(r.getString(R.string.category_murder), R.drawable.ic_murder);
        icons.put(r.getString(R.string.category_vandalism), R.drawable.ic_vandalism);
        icons.put(r.getString(R.string.category_burglary), R.drawable.ic_burglary);

        icons.put(r.getString(R.string.category_hemorrhage), R.drawable.ic_haemorrhage);
        icons.put(r.getString(R.string.category_burn), R.drawable.ic_burn);
        icons.put(r.getString(R.string.category_body_loss), R.drawable.ic_body_loss);
        icons.put(r.getString(R.string.category_fracture), R.drawable.ic_fracture);

        icons.put(r.getString(R.string.category_physical), R.drawable.ic_physical);
        icons.put(r.getString(R.string.category_psychical), R.drawable.ic_psychical);
        icons.put(r.getString(R.string.category_sexual), R.drawable.ic_sexual);

        icons.put(r.getString(R.string.category_pain), R.drawable.ic_pain);
        icons.put(r.getString(R.string.category_sickness), R.drawable.ic_sickness);
        icons.put(r.getString(R.string.category_allergy), R.drawable.ic_alergy);

    }
}
