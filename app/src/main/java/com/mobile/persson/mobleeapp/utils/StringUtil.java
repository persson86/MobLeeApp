package com.mobile.persson.mobleeapp.utils;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by persson on 09/12/16.
 */

public class StringUtil {

    public static Spanned convertHtmlToText(String htmlContent) {
        String htmlAsString = htmlContent;
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);
        return htmlAsSpanned;
    }
}
