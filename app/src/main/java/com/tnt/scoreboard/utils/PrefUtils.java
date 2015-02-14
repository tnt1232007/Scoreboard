package com.tnt.scoreboard.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tnt.scoreboard.R;

public final class PrefUtils {

    public static String getName(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(context.getString(R.string.pref_key_name), "");
    }

    public static boolean isFirstNameLast(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getBoolean(context.getString(R.string.pref_key_name_format), true);
    }

    public static String getTheme(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(context.getString(R.string.pref_key_theme), "");
    }

    public static String getOrientation(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(context.getString(R.string.pref_key_orientation), "");
    }

    public static int getUpdateDelay(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(context.getString(R.string.pref_key_update_delay), -1);
    }

    public static int getScore0(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(context.getString(R.string.pref_key_score_0), -1);
    }

    public static int getScore1(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(context.getString(R.string.pref_key_score_1), -1);
    }

    public static int getScore2(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(context.getString(R.string.pref_key_score_2), -1);
    }

    public static int getScore3(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(context.getString(R.string.pref_key_score_3), -1);
    }
}
