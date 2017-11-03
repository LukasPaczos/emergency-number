package com.lukaspaczos.emergencynumber.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by Lukas Paczos on 20-Apr-17
 */

public final class StringUtils {

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

}
