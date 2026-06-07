package com.example.agrodnevnik.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.open-meteo.com/";
    private static RetrofitClient instance;
    private final WeatherApi weatherApi;

    // Privatni konstruktor - pravi Retrofit instancu
    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherApi = retrofit.create(WeatherApi.class);
    }

    // Singleton - vrati postojecu ili napravi novu instancu
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    // Vrati API objekat za koriscenje
    public WeatherApi getWeatherApi() {
        return weatherApi;
    }
}