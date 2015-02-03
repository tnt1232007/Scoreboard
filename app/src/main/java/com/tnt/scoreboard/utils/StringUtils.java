package com.tnt.scoreboard.utils;

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
}
