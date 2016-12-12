package com.mobile.persson.mobleeapp.utils;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by persson on 09/12/16.
 */

public class StringUtil {
    private static final String ORIGINAL_PATTERN_BEGIN = "<pre><code>";
    private static final String ORIGINAL_PATTERN_END = "</code></pre>";
    private static final String PARSED_PATTERN_BEGIN = "<font color=\"#888888\"><tt>";
    private static final String PARSED_PATTERN_END = "</tt></font>";

    public static Spanned parse(String text) {
        if (text == null) return null;

        text = parseSourceCode(text);

        Spanned textSpanned = Html.fromHtml(text);
        return textSpanned;
    }

    public static String parseSourceCode(String text) {
        if (text.indexOf(ORIGINAL_PATTERN_BEGIN) < 0) return text;

        StringBuilder result = new StringBuilder();
        int begin;
        int end;
        int beginIndexToProcess = 0;

        while (text.indexOf(ORIGINAL_PATTERN_BEGIN) >= 0) {
            begin = text.indexOf(ORIGINAL_PATTERN_BEGIN);
            end = text.indexOf(ORIGINAL_PATTERN_END);

            String code = parseCodeSegment(text, begin, end);

            result.append(text.substring(beginIndexToProcess, begin));
            result.append(PARSED_PATTERN_BEGIN);
            result.append(code);
            result.append(PARSED_PATTERN_END);

            text = text.replaceFirst(ORIGINAL_PATTERN_BEGIN, PARSED_PATTERN_BEGIN);
            text = text.replaceFirst(ORIGINAL_PATTERN_END, PARSED_PATTERN_END);

            beginIndexToProcess = text.lastIndexOf(PARSED_PATTERN_END) + PARSED_PATTERN_END.length();
        }

        result.append(text.substring(beginIndexToProcess, text.length()));

        return result.toString();
    }

    public static String parseCodeSegment(String text, int begin, int end) {
        String code = text.substring(begin + ORIGINAL_PATTERN_BEGIN.length(), end);
        code = code.replace(" ", "&nbsp;");
        code = code.replace("\n", "<br/>");
        return code;
    }
}
