package com.tnt.scoreboard.utils;

import android.content.Context;

public final class StringUtils {

    public static <T> String join(Iterable<T> list, String separator) {
        StringBuilder sb = new StringBuilder();
        String tempSeparator = "";
        for (T o : list) {
            sb.append(tempSeparator).append(o);
            tempSeparator = separator;
        }
        return sb.toString();
    }

    public static <T> String join(Iterable<T> list, String separator, int limitLen) {
        StringBuilder sb = new StringBuilder();
        String tempSeparator = "";
        for (T o : list) {
            if (sb.length() + tempSeparator.length() + o.toString().length() + 3 >= limitLen) {
                sb.append("...");
                break;
            }
            sb.append(tempSeparator).append(o);
            tempSeparator = separator;
        }
        return sb.toString();
    }

    public static String subString(String s, int start, int end) {
        if (start < 0)
            start = 0;
        if (end > s.length())
            end = s.length();
        return s.substring(start, end);
    }

    public static String padLeft(Object s, char padChar, int n) {
        return String.format("%1$" + n + "s", s).replace(' ', padChar);
    }

    public static String padRight(String s, char padChar, int n) {
        return String.format("%1$-" + n + "s", s).replace(' ', padChar);
    }

    public static String getInitial(Context context, String name) {
        String[] names = name.split(" ");
        return String.valueOf(names[PrefUtils.isFirstNameLast(context)
                ? names.length - 1 : 0].charAt(0));
    }
}
