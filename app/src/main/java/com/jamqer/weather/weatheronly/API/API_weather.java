package com.jamqer.weather.weatheronly.API;



import com.jamqer.weather.weatheronly.Model.ResponseData;

import java.net.Authenticator;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

/**
 * Created by Piotras on 2015-10-03.
 */
public interface API_weather {
@Headers({"x-api-key: 94c2ab7dd8c70dd01566b00b825245ad"})
    @GET("weather")
    Call<ResponseData> getWeather(@Query("q") String CityName);

}
