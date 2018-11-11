package com.wwa.widgetweatheralarm.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.wwa.widgetweatheralarm.util.Constants;
import com.wwa.widgetweatheralarm.util.Tag;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;

public class CustomAlarmManager {
    private static CustomAlarmManager instance;
    private AlarmManager alarmManager;
    private List<PendingIntent> alarmIntents = new ArrayList<>();

    private CustomAlarmManager(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for (int alarmNum = 0; alarmNum < 3; alarmNum++) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(Constants.ALARM_NUM, alarmNum);
            alarmIntents.add(PendingIntent.getBroadcast(context, alarmNum, intent, 0));
        }
    }

    public static CustomAlarmManager getInstance(Context context) {
        if (instance == null) {
            instance = new CustomAlarmManager(context);
        }
        return instance;
    }

    public void setAlarm(int hourOfDay, int minute, int alarmNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        //cancelAlarm(alarmNum);
        if (System.currentTimeMillis() <= calendar.getTimeInMillis()) {
            if (alarmManager != null) {
                if (Build.VERSION_CODES.KITKAT <= SDK_INT && SDK_INT < Build.VERSION_CODES.M) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(), alarmIntents.get(alarmNum));
                } else if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(
                            calendar.getTimeInMillis(), alarmIntents.get(alarmNum)
                    ), alarmIntents.get(alarmNum));
                }
                Log.d(Tag.get(CustomAlarmManager.class), alarmNum + " alarm On");
            }
        }
    }

    public void cancelAlarm(int alarmNum) {
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntents.get(alarmNum));
            Log.d(Tag.get(CustomAlarmManager.class), alarmNum + " alarm Off");
        }
    }
}