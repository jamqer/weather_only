package com.jamqer.weather.weatheronly.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ofaro on 6/1/2015.
 */
public class Clouds {
    @SerializedName("all")
    String all;

    public String getAll() {
        return all;
    }
}
