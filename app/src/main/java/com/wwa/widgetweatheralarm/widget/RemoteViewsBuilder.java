package com.wwa.widgetweatheralarm.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.wwa.widgetweatheralarm.util.Constants;
import com.wwa.widgetweatheralarm.util.PrefsData;
import com.wwa.widgetweatheralarm.R;
import com.wwa.widgetweatheralarm.alarm.CustomAlarmManager;
import com.wwa.widgetweatheralarm.util.Tag;
import com.wwa.widgetweatheralarm.weather.WeatherLoader;

public class RemoteViewsBuilder {
    private static final int GREEN = 0xFF00FF00;
    private static final int RED = 0xFFFF0000;

    private static final int FIRST_CITY = 1;
    private static final int SECOND_CITY = 2;

    private static String sWeatherInfo1;
    private static String sWeatherInfo2;
    private static int sCityNumber = FIRST_CITY;

    private static int sImageViewResourceId;
    private static String sCityName;
    private static String sTemp;
    private static String sDescription;

    private static int[] sAlarmBtns = new int[]{R.id.btn_1, R.id.btn_2, R.id.btn_3};
    private static boolean[] sAlarmSets = new boolean[]{false, false, false};
    private static String[] sAlarmTimes;

    private static RemoteViewsBuilder sInstance;

    private RemoteViewsBuilder(Context context) {
        sImageViewResourceId = context.getResources().getIdentifier("ic_no_icon",
                "drawable", context.getPackageName());
        sCityName = "Нет данных";
        sTemp = "";
        sDescription = "";
    }

    public static RemoteViewsBuilder getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RemoteViewsBuilder(context);
        }
        return sInstance;
    }

    public RemoteViews build(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        views.setImageViewResource(R.id.icon, sImageViewResourceId);
        views.setTextViewText(R.id.city_name, sCityName);
        views.setTextViewText(R.id.temp, sTemp);
        views.setTextViewText(R.id.description, sDescription);

        for (int i = 0; i < 3; i++) {
            if (sAlarmTimes[i] != null) {
                views.setTextViewText(sAlarmBtns[i], sAlarmTimes[i]);
                if (sAlarmSets[i]) {
                    views.setTextColor(sAlarmBtns[i], GREEN);
                } else {
                    views.setTextColor(sAlarmBtns[i], RED);
                }
            }
        }

        views.setOnClickPendingIntent(R.id.city_name,
                getPendingIntent(context, Constants.CITY_CLICKED));
        views.setOnClickPendingIntent(R.id.refresh,
                getPendingIntent(context, Constants.REFRESH_CLICKED));
        views.setOnClickPendingIntent(R.id.btn_1,
                getPendingIntent(context, Constants.BTN_1_CLICKED));
        views.setOnClickPendingIntent(R.id.btn_2,
                getPendingIntent(context, Constants.BTN_2_CLICKED));
        views.setOnClickPendingIntent(R.id.btn_3,
                getPendingIntent(context, Constants.BTN_3_CLICKED));
        return views;
    }

    private PendingIntent getPendingIntent(Context context, String action) {
        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public void loadWeatherInfo(Context context) {
        sWeatherInfo1 = PrefsData.loadTitlePref(context, Constants.PREFS_FIRST_CITY);
        sWeatherInfo2 = PrefsData.loadTitlePref(context, Constants.PREFS_SECOND_CITY);
    }

    public void parseWeatherInfo() {
        String weatherInfo = null;
        switch (sCityNumber) {
            case FIRST_CITY:
                weatherInfo = sWeatherInfo1;
                break;
            case SECOND_CITY:
                weatherInfo = sWeatherInfo2;
                break;
        }
        if (weatherInfo == null) {
            if (sWeatherInfo1 != null) {
                weatherInfo = sWeatherInfo1;
            } else if (sWeatherInfo2 != null) {
                weatherInfo = sWeatherInfo2;
            }
        }
        if (weatherInfo != null) {
            String[] weather = weatherInfo.split(",");
            sImageViewResourceId = Integer.parseInt(weather[0]);
            sCityName = weather[1];
            sTemp = weather[2] + " " + "C";
            sDescription = weather[3];
        }
    }

    public void loadAlarmInfo(Context context) {
        sAlarmTimes = new String[]{
                PrefsData.loadTitlePref(context, Constants.PREFS_FIRST_ALARM),
                PrefsData.loadTitlePref(context, Constants.PREFS_SECOND_ALARM),
                PrefsData.loadTitlePref(context, Constants.PREFS_THIRD_ALARM)
        };
    }

    public void checkAlarm(Context context, int alarmNum) {
        if (sAlarmTimes[alarmNum] != null) {
            if (sAlarmSets[alarmNum]) {
                CustomAlarmManager.getInstance(context).cancelAlarm(alarmNum);
                sAlarmSets[alarmNum] = false;
            } else {
                int hour = Integer.parseInt(sAlarmTimes[alarmNum].split(":")[0]);
                int minute = Integer.parseInt(sAlarmTimes[alarmNum].split(":")[1]);
                CustomAlarmManager.getInstance(context).setAlarm(hour, minute, alarmNum);
                sAlarmSets[alarmNum] = true;
            }
        }
    }

    public void refreshWeather(Context context) {
        if (sWeatherInfo1 != null)
            WeatherLoader.getInstance().getWeather(
                context, null, sWeatherInfo1.split(",")[1], FIRST_CITY);
        if (sWeatherInfo2 != null)
            WeatherLoader.getInstance().getWeather(
                context, null, sWeatherInfo2.split(",")[1], SECOND_CITY);
    }

    public void changeCity() {
        sCityNumber = (sCityNumber == FIRST_CITY) ? SECOND_CITY : FIRST_CITY;
        parseWeatherInfo();
    }

    public void cancelAllAlarms(Context context) {
        for (int alarmNum = 0; alarmNum < 3; alarmNum++) {
            cancelAlarm(context, alarmNum);
        }
    }

    public void cancelAlarm(Context context, int alarmNum) {
        sAlarmSets[alarmNum] = false;
        CustomAlarmManager.getInstance(context).cancelAlarm(alarmNum);
        Log.d(Tag.get(RemoteViewsBuilder.class), alarmNum + " alarm canceled");
    }
}
