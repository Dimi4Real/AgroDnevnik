package com.example.agrodnevnik.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {

    // "current" blok iz JSON odgovora
    @SerializedName("current")
    public Current current;

    // "daily" blok iz JSON odgovora
    @SerializedName("daily")
    public Daily daily;

    // Trenutno vreme
    public static class Current {
        @SerializedName("temperature_2m")
        public double temperature;          // temperatura u stepenima

        @SerializedName("windspeed_10m")
        public double windspeed;            // brzina vetra km/h

        @SerializedName("weathercode")
        public int weathercode;             // kod koji opisuje vreme (sunce, kisa...)
    }

    // Prognoza za narednih 7 dana
    public static class Daily {
        @SerializedName("time")
        public List<String> time;           // datumi

        @SerializedName("temperature_2m_max")
        public List<Double> temperatureMax; // maksimalne temperature

        @SerializedName("temperature_2m_min")
        public List<Double> temperatureMin; // minimalne temperature

        @SerializedName("weathercode")
        public List<Integer> weathercode;   // kod vremena po danima
    }
}