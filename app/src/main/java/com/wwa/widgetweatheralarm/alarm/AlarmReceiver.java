package com.wwa.widgetweatheralarm.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wwa.widgetweatheralarm.util.Constants;
import com.wwa.widgetweatheralarm.util.Tag;
import com.wwa.widgetweatheralarm.widget.RemoteViewsBuilder;
import com.wwa.widgetweatheralarm.widget.WeatherWidget;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(Tag.get(AlarmReceiver.class), "Alarm On");
        int alarmNum = intent.getIntExtra(Constants.ALARM_NUM, -1);
        if (alarmNum != -1) {
            RemoteViewsBuilder builder = RemoteViewsBuilder.getInstance(context);
            builder.checkAlarm(context, alarmNum);
            WeatherWidget.pushWidgetUpdate(context, builder.build(context));
        }

        Intent i = new Intent(context, AlarmActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setAction(Constants.ALARM_START);
        context.startActivity(i);
    }
}