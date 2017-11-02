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
        categories.put(r.getString(R.string.main_wypadek), r.getString(R.string.category_none));
        categories.put(r.getString(R.string.main_pozar), r.getString(R.string.category_none));
        categories.put(r.getString(R.string.main_wlamanie), r.getString(R.string.category_none));
        categories.put(r.getString(R.string.main_uraz), r.getString(R.string.category_none));
        categories.put(r.getString(R.string.main_przemoc), r.getString(R.string.category_none));
        categories.put(r.getString(R.string.main_zdrowie), r.getString(R.string.category_none));

        /*
         * WYPADEK SUB-CATEGORIES
         */
        categories.put(r.getString(R.string.wypadek_drogowy), r.getString(R.string.main_wypadek));
        categories.put(r.getString(R.string.wypadek_utoniecie), r.getString(R.string.main_wypadek));
        categories.put(r.getString(R.string.wypadek_porazenie), r.getString(R.string.main_wypadek));
        categories.put(r.getString(R.string.wypadek_inne_pogotowie), r.getString(R.string.main_wypadek));
        categories.put(r.getString(R.string.wypadek_inne_policja), r.getString(R.string.main_wypadek));
        categories.put(r.getString(R.string.wypadek_inne_straz), r.getString(R.string.main_wypadek));


        /*
         * POZAR SUB_CATEGORIES
         */
        categories.put(r.getString(R.string.pozar_budynku), r.getString(R.string.main_pozar));
        categories.put(r.getString(R.string.pozar_chemiczny), r.getString(R.string.main_pozar));
        categories.put(r.getString(R.string.pozar_natury), r.getString(R.string.main_pozar));
        categories.put(r.getString(R.string.pozar_przedmiotu), r.getString(R.string.main_pozar));

        /*
         * WLAMANIE SUB_CATEGORIES
         */
        categories.put(r.getString(R.string.kradziez_kradziez), r.getString(R.string.main_wlamanie));
        categories.put(r.getString(R.string.kradziez_morderstwo), r.getString(R.string.main_wlamanie));
        categories.put(r.getString(R.string.kradziez_wandalizm), r.getString(R.string.main_wlamanie));
        categories.put(r.getString(R.string.kradziez_wlamanie), r.getString(R.string.main_wlamanie));

        /*
         * URAZ SUB_CATEGORIES
         */
        categories.put(r.getString(R.string.uraz_krwotok), r.getString(R.string.main_uraz));
        categories.put(r.getString(R.string.uraz_poparzenie), r.getString(R.string.main_uraz));
        categories.put(r.getString(R.string.uraz_utrata), r.getString(R.string.main_uraz));
        categories.put(r.getString(R.string.uraz_zlamanie), r.getString(R.string.main_uraz));

        /*
         * PRZEMOC SUB_CATEGORIES
         */
        categories.put(r.getString(R.string.przemoc_fizyczna), r.getString(R.string.main_przemoc));
        categories.put(r.getString(R.string.przemoc_psychiczna), r.getString(R.string.main_przemoc));
        categories.put(r.getString(R.string.przemoc_seksualna), r.getString(R.string.main_przemoc));

        /*
         * ZDROWIE SUB_CATEGORIES
         */
        categories.put(r.getString(R.string.zdrowie_bol), r.getString(R.string.main_zdrowie));
        categories.put(r.getString(R.string.zdrowie_choroba), r.getString(R.string.main_zdrowie));
        categories.put(r.getString(R.string.zdrowie_uczulenie), r.getString(R.string.main_zdrowie));

        /*INNE - HAS TO BE LAST*/
        categories.put(r.getString(R.string.inne), r.getString(R.string.universal_category));

        /*
         * CATEGORIES ICONS
         */
        icons.put(r.getString(R.string.inne), R.drawable.ic_inne);

        icons.put(r.getString(R.string.main_wypadek), R.drawable.ic_main_wypadek);
        icons.put(r.getString(R.string.main_pozar), R.drawable.ic_main_pozar);
        icons.put(r.getString(R.string.main_wlamanie), R.drawable.ic_main_wlamanie);
        icons.put(r.getString(R.string.main_uraz), R.drawable.ic_main_uraz);
        icons.put(r.getString(R.string.main_przemoc), R.drawable.ic_main_przemoc);
        icons.put(r.getString(R.string.main_zdrowie), R.drawable.ic_main_zdrowie);

        icons.put(r.getString(R.string.wypadek_drogowy), R.drawable.ic_wypadek_drogowy);
        icons.put(r.getString(R.string.wypadek_utoniecie), R.drawable.ic_wypadek_utoniecie);
        icons.put(r.getString(R.string.wypadek_porazenie), R.drawable.ic_wypadek_porazenie);
        icons.put(r.getString(R.string.wypadek_inne_pogotowie), R.drawable.ic_wypadek_inne_pogotowie);
        icons.put(r.getString(R.string.wypadek_inne_policja), R.drawable.ic_wypadek_inne_policja);
        icons.put(r.getString(R.string.wypadek_inne_straz), R.drawable.ic_wypadek_inne_straz);

        icons.put(r.getString(R.string.pozar_budynku), R.drawable.ic_pozar_budynku);
        icons.put(r.getString(R.string.pozar_chemiczny), R.drawable.ic_pozar_chemiczny);
        icons.put(r.getString(R.string.pozar_natury), R.drawable.ic_pozar_natury);
        icons.put(r.getString(R.string.pozar_przedmiotu), R.drawable.ic_pozar_przedmiotu);

        icons.put(r.getString(R.string.kradziez_kradziez), R.drawable.ic_kradziez_kradziez);
        icons.put(r.getString(R.string.kradziez_morderstwo), R.drawable.ic_kradziez_morderstwo);
        icons.put(r.getString(R.string.kradziez_wandalizm), R.drawable.ic_kradziez_wandalizm);
        icons.put(r.getString(R.string.kradziez_wlamanie), R.drawable.ic_kradziez_wlamanie);

        icons.put(r.getString(R.string.uraz_krwotok), R.drawable.ic_uraz_krwotok);
        icons.put(r.getString(R.string.uraz_poparzenie), R.drawable.ic_uraz_poparzenie);
        icons.put(r.getString(R.string.uraz_utrata), R.drawable.ic_uraz_utrata);
        icons.put(r.getString(R.string.uraz_zlamanie), R.drawable.ic_uraz_zlamanie);

        icons.put(r.getString(R.string.przemoc_fizyczna), R.drawable.ic_przemoc_fizyczna);
        icons.put(r.getString(R.string.przemoc_psychiczna), R.drawable.ic_przemoc_psychiczna);
        icons.put(r.getString(R.string.przemoc_seksualna), R.drawable.ic_przemoc_seksualna);

        icons.put(r.getString(R.string.zdrowie_bol), R.drawable.ic_zdrowie_bol);
        icons.put(r.getString(R.string.zdrowie_choroba), R.drawable.ic_zdrowie_choroba);
        icons.put(r.getString(R.string.zdrowie_uczulenie), R.drawable.ic_zdrowie_uczulenie);

    }
}
