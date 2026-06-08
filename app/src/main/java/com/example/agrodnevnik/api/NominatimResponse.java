package com.example.agrodnevnik.api;

import com.google.gson.annotations.SerializedName;

public class NominatimResponse {

    // API vraca "lat" i "lon" kao String, ne kao double
    @SerializedName("lat")
    public String lat;

    @SerializedName("lon")
    public String lon;

    @SerializedName("display_name")
    public String displayName;
}