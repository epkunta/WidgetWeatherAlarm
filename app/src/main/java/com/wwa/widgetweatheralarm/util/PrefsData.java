package com.wwa.widgetweatheralarm.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsData {
    public static void saveTitlePref(Context context, String prefsName, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(prefsName, 0).edit();
        prefs.putString(prefsName, text);
        prefs.apply();
    }

    public static String loadTitlePref(Context context, String prefsName) {
        SharedPreferences prefs = context.getSharedPreferences(prefsName, 0);
        return prefs.getString(prefsName, null);
    }

    public static void deleteTitlePref(Context context, String prefsName) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(prefsName, 0).edit();
        prefs.remove(prefsName);
        prefs.apply();
    }
}