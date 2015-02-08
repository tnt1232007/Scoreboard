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

    public static String getInitial(Context context, String name) {
        String[] names = name.split(" ");
        return String.valueOf(names[PrefUtils.isFirstNameLast(context)
                ? names.length - 1 : 0].charAt(0));
    }
}
