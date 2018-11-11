package com.wwa.widgetweatheralarm.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Main {
    @SerializedName("temp")
    @Expose
    private Double temp;
    @SerializedName("pressure")
    @Expose
    private Double pressure;
    @SerializedName("humidity")
    @Expose
    private Integer humidity;
    @SerializedName("temp_min")
    @Expose
    private Double tempMin;
    @SerializedName("temp_max")
    @Expose
    private Double tempMax;
}