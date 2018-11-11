package com.wwa.widgetweatheralarm.alarm;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wwa.widgetweatheralarm.util.Constants;
import com.wwa.widgetweatheralarm.util.PrefsData;

import java.util.Calendar;

public class AlarmTimePick {
    public static void setAlarm(final Context context, final TextView tv, final int num) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute;
                switch (num) {
                    case 0:
                        PrefsData.saveTitlePref(context, Constants.PREFS_FIRST_ALARM, time);
                        break;
                    case 1:
                        PrefsData.saveTitlePref(context, Constants.PREFS_SECOND_ALARM, time);
                        break;
                    case 2:
                        PrefsData.saveTitlePref(context, Constants.PREFS_THIRD_ALARM, time);
                        break;
                }
                tv.setText(time);
            }
        }, mHour, mMinute, false).show();
    }
}
