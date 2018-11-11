package com.wwa.widgetweatheralarm.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.wwa.widgetweatheralarm.util.Constants;
import com.wwa.widgetweatheralarm.util.Tag;

import java.util.Objects;

public class WeatherWidget extends AppWidgetProvider {
    private static RemoteViewsBuilder builder;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(Tag.get(WeatherWidget.class), "onUpdate");
        builder = RemoteViewsBuilder.getInstance(context);
        buildRemoteViews(context);
        pushWidgetUpdate(context, builder.build(context));
        builder.refreshWeather(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Tag.get(WeatherWidget.class), "onReceive: " + intent.getAction());

        builder = RemoteViewsBuilder.getInstance(context);
        buildRemoteViews(context);

        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case Constants.BTN_1_CLICKED:
                    builder.checkAlarm(context, 0);
                    break;
                case Constants.BTN_2_CLICKED:
                    builder.checkAlarm(context, 1);
                    break;
                case Constants.BTN_3_CLICKED:
                    builder.checkAlarm(context, 2);
                    break;
                case Constants.REFRESH_CLICKED:
                    builder.refreshWeather(context);
                    break;
                case Constants.CITY_CLICKED:
                    builder.changeCity();
                    break;
                case Constants.WIDGET_DELETED:
                    builder.cancelAllAlarms(context);
                    break;
            }
        }
        checkForAlarmChanges(context, intent);
        pushWidgetUpdate(context, builder.build(context));
    }

    private void buildRemoteViews(Context context) {
        builder.loadWeatherInfo(context);
        builder.parseWeatherInfo();
        builder.loadAlarmInfo(context);
    }

    private void checkForAlarmChanges(Context context, Intent intent) {
        if (intent.hasExtra(Constants.UPDATE_WIDGET_INFO)) {
            for (int i = 0; i < 3; i++) {
                if (intent.getIntArrayExtra(Constants.UPDATE_WIDGET_INFO)[i] != -1) {
                    builder.cancelAlarm(context, i);
                }
            }
        }
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, WeatherWidget.class);
        int[] appWidgetIds = manager.getAppWidgetIds(componentName);
        manager.updateAppWidget(appWidgetIds, remoteViews);
        Log.d(Tag.get(WeatherWidget.class), "Widget updated");
    }
}
