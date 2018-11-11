package com.wwa.widgetweatheralarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wwa.widgetweatheralarm.alarm.AlarmTimePick;
import com.wwa.widgetweatheralarm.util.Constants;
import com.wwa.widgetweatheralarm.util.PrefsData;
import com.wwa.widgetweatheralarm.util.Tag;
import com.wwa.widgetweatheralarm.weather.WeatherLoader;
import com.wwa.widgetweatheralarm.widget.WeatherWidget;

public class MainActivity extends AppCompatActivity {
    final Context context = MainActivity.this;
    private WeatherLoader mWeatherLoader;

    private boolean[] alarmChanged = new boolean[]{false, false, false};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(Tag.get(MainActivity.class), "onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mWeatherLoader = WeatherLoader.getInstance();

        String weatherInfo1 = PrefsData.loadTitlePref(context, Constants.PREFS_FIRST_CITY);
        String weatherInfo2 = PrefsData.loadTitlePref(context, Constants.PREFS_SECOND_CITY);

        final EditText city1Et = findViewById(R.id.city_search_first);
        if (weatherInfo1 != null) city1Et.setHint(weatherInfo1.split(",")[1]);
        final EditText city2Et = findViewById(R.id.city_search_second);
        if (weatherInfo2 != null) city2Et.setHint(weatherInfo2.split(",")[1]);

        findViewById(R.id.add_button_first).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSearchCityBtnClick(city1Et, 1);
                    }
                });
        findViewById(R.id.add_button_second).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSearchCityBtnClick(city2Et, 2);
                    }
                });

        String alarm1Time = PrefsData.loadTitlePref(context, Constants.PREFS_FIRST_ALARM);
        String alarm2Time = PrefsData.loadTitlePref(context, Constants.PREFS_SECOND_ALARM);
        String alarm3Time = PrefsData.loadTitlePref(context, Constants.PREFS_THIRD_ALARM);

        final TextView alarmTv1 = findViewById(R.id.alarm_1);
        if (alarm1Time != null) alarmTv1.setText(alarm1Time);
        final TextView alarmTv2 = findViewById(R.id.alarm_2);
        if (alarm2Time != null) alarmTv2.setText(alarm2Time);
        final TextView alarmTv3 = findViewById(R.id.alarm_3);
        if (alarm3Time != null) alarmTv3.setText(alarm3Time);

        findViewById(R.id.add_alarm_clock_1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlarmTimePick.setAlarm(context, alarmTv1, 0);
                        alarmChanged[0] = true;
                    }
                });
        findViewById(R.id.add_alarm_clock_2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlarmTimePick.setAlarm(context, alarmTv2, 1);
                        alarmChanged[1] = true;
                    }
                });
        findViewById(R.id.add_alarm_clock_3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlarmTimePick.setAlarm(context, alarmTv3, 2);
                        alarmChanged[2] = true;
                    }
                });
    }

    public void onSearchCityBtnClick(final EditText et, final int num) {
        String widgetText = et.getText().toString().trim();
        if (widgetText.length() > 0)
            mWeatherLoader.getWeather(context, et, widgetText, num);
    }

    @Override
    protected void onPause() {
        super.onPause();
        int[] alarmChanges = new int[]{-1, -1, -1};
        for (int i = 0; i < 3; i++) {
            if(alarmChanged[i]) alarmChanges[i] = i;
        }
        Intent intent = new Intent(context, WeatherWidget.class);
        intent.putExtra(Constants.UPDATE_WIDGET_INFO, alarmChanges);
        context.sendBroadcast(intent);
    }
}
