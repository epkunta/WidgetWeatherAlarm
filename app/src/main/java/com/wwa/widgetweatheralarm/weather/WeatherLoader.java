package com.wwa.widgetweatheralarm.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.wwa.widgetweatheralarm.util.Constants;
import com.wwa.widgetweatheralarm.util.PrefsData;
import com.wwa.widgetweatheralarm.weather.retrofit.OpenWeather;
import com.wwa.widgetweatheralarm.weather.retrofit.WeatherReceiver;
import com.wwa.widgetweatheralarm.util.Tag;
import com.wwa.widgetweatheralarm.widget.RemoteViewsBuilder;
import com.wwa.widgetweatheralarm.widget.WeatherWidget;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherLoader {
    private static WeatherLoader sInstance;
    private static OpenWeather mOpenWeather;

    private WeatherLoader() {
        mOpenWeather = WeatherReceiver.initRetrofit().create(OpenWeather.class);
    }

    public static WeatherLoader getInstance() {
        if (sInstance == null) sInstance = new WeatherLoader();
        return sInstance;
    }

    public void getWeather(final Context context, final EditText et, String city,
                           final int num) {
        Call<WeatherRequest> call = mOpenWeather.loadWeather(city,
                Constants.UNITS, Constants.LANG, Constants.API_KEY);
        call.enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(@NonNull Call<WeatherRequest> call,
                                   @NonNull Response<WeatherRequest> response) {
                if (response.isSuccessful()) {
                    Log.d(Tag.get(WeatherLoader.class), response.body().toString());

                    WeatherRequest weatherRequest = response.body();
                    String cityName = weatherRequest.getName();
                    String data = parseData(context, weatherRequest);

                    switch (num) {
                        case 1:
                            PrefsData.saveTitlePref(context, Constants.PREFS_FIRST_CITY, data);
                            break;
                        case 2:
                            PrefsData.saveTitlePref(context, Constants.PREFS_SECOND_CITY, data);
                    }
                    if (et != null) {
                        et.getText().clear();
                        et.setHint(cityName);
                        Toast.makeText(context, cityName + " добавлен", Toast.LENGTH_SHORT).show();
                    } else {
                        RemoteViewsBuilder builder = RemoteViewsBuilder.getInstance(context);
                        builder.loadWeatherInfo(context);
                        builder.parseWeatherInfo();
                        WeatherWidget.pushWidgetUpdate(context, builder.build(context));
                        Toast.makeText(context, "Данные обновлены", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherRequest> call, @NonNull Throwable t) {
                Toast.makeText(context, "Не удалось загрузить", Toast.LENGTH_SHORT).show();
                Log.d(Tag.get(WeatherLoader.class), t.getMessage());
            }
        });
    }

    private String parseData(Context context, WeatherRequest weatherRequest) {
        String drawableName = "ic_" + weatherRequest.getWeather().get(0).getIcon();
        int iconId = context.getResources().getIdentifier(
                drawableName, "drawable", context.getPackageName());
        String cityName = weatherRequest.getName();
        double temp = weatherRequest.getMain().getTemp();
        String description = weatherRequest.getWeather().get(0).getDescription();
        return iconId + "," + cityName + "," + temp + "," + description;
    }
}
