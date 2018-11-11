package com.wwa.widgetweatheralarm.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class WeatherRequest {
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("name")
    @Expose
    private String name;
}