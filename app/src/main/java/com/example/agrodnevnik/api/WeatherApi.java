package com.example.agrodnevnik.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    // Poziva: https://api.open-meteo.com/v1/forecast?latitude=...&longitude=...&current=...&daily=...
    @GET("v1/forecast")
    Call<WeatherResponse> getForecast(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("current") String current,
            @Query("daily") String daily,
            @Query("timezone") String timezone,
            @Query("forecast_days") int forecastDays
    );
}