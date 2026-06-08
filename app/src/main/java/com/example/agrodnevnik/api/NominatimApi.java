package com.example.agrodnevnik.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NominatimApi {

    // Poziva: https://nominatim.openstreetmap.org/search?q=Pancevo&format=json&limit=1
    @GET("search")
    Call<List<NominatimResponse>> getCoordinates(
            @Query("q") String mesto,
            @Query("format") String format,
            @Query("limit") int limit
    );
}