package com.tnt.scoreboard.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public final class PrefUtils {

    public static final String DEFAULT_NAME = "default_name";
    public static final String THEME = "theme";

    public static String getDefaultName(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(DEFAULT_NAME, "Player A");
    }

    public static String getTheme(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(THEME, "Default");
    }
}
