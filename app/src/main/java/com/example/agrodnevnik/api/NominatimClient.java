package com.example.agrodnevnik.api;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NominatimClient {

    private static final String BASE_URL = "https://nominatim.openstreetmap.org/";
    private static NominatimClient instance;
    private final NominatimApi nominatimApi;

    // Nominatim zahteva User-Agent header u svakom requestu
    // Koristimo OkHttpClient da ga dodamo automatski
    private NominatimClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .header("User-Agent", "AgroDnevnik/1.0")
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nominatimApi = retrofit.create(NominatimApi.class);
    }

    public static synchronized NominatimClient getInstance() {
        if (instance == null) {
            instance = new NominatimClient();
        }
        return instance;
    }

    // Pomocna metoda koja uzima naziv mesta i vraca koordinate kroz callback
    // onSuccess vraca latitude i longitude
    // onError vraca poruku greske
    public void getCoordinates(String mesto,
                               OnCoordinatesListener listener) {
        nominatimApi.getCoordinates(mesto, "json", 1)
                .enqueue(new Callback<List<NominatimResponse>>() {
                    @Override
                    public void onResponse(Call<List<NominatimResponse>> call,
                                           Response<List<NominatimResponse>> response) {
                        if (response.isSuccessful()
                                && response.body() != null
                                && !response.body().isEmpty()) {
                            NominatimResponse result = response.body().get(0);
                            double lat = Double.parseDouble(result.lat);
                            double lon = Double.parseDouble(result.lon);
                            listener.onSuccess(lat, lon);
                        } else {
                            listener.onError("Mesto nije pronađeno");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<NominatimResponse>> call,
                                          Throwable t) {
                        listener.onError("Greška: " + t.getMessage());
                    }
                });
    }

    // Interfejs za callback
    public interface OnCoordinatesListener {
        void onSuccess(double latitude, double longitude);
        void onError(String message);
    }
}