package com.wwa.widgetweatheralarm.weather.retrofit;

import com.wwa.widgetweatheralarm.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherReceiver {
    private static Retrofit mRetrofit;

    public static Retrofit initRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return mRetrofit;
    }
}
