package com.example.agrodnevnik;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.agrodnevnik.api.RetrofitClient;
import com.example.agrodnevnik.api.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    // Podrazumevane koordinate (Pancevo) ako parcela nema lokaciju
    private static final double DEFAULT_LATITUDE = 44.8667;
    private static final double DEFAULT_LONGITUDE = 20.6500;

    private TextView textViewLokacija;
    private TextView textViewTrenutnoVreme;
    private TextView textViewPrognoza;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Toolbar toolbar = findViewById(R.id.toolbarWeather);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewLokacija = findViewById(R.id.textViewLokacija);
        textViewTrenutnoVreme = findViewById(R.id.textViewTrenutnoVreme);
        textViewPrognoza = findViewById(R.id.textViewPrognoza);

        String nazivParcele = getIntent().getStringExtra("nazivParcele");
        String lokacija = getIntent().getStringExtra("lokacija");
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        // Ako parcela nema koordinate koristimo podrazumevane
        if (latitude == 0 && longitude == 0) {
            latitude = DEFAULT_LATITUDE;
            longitude = DEFAULT_LONGITUDE;
        }

        String naslov = "🌾 " + nazivParcele;
        if (lokacija != null && !lokacija.isEmpty()) {
            naslov += " — " + lokacija;
        }
        textViewLokacija.setText(naslov);
        textViewTrenutnoVreme.setText("Učitavanje...");

        ucitajPrognozu();
    }

    // Dugme nazad u toolbaru
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void ucitajPrognozu() {
        Call<WeatherResponse> call = RetrofitClient.getInstance()
                .getWeatherApi()
                .getForecast(
                        latitude,
                        longitude,
                        "temperature_2m,windspeed_10m,weathercode",
                        "temperature_2m_max,temperature_2m_min,weathercode",
                        "Europe/Belgrade",
                        7
                );

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call,
                                   Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    prikaziPodatke(response.body());
                } else {
                    textViewTrenutnoVreme.setText("Greška pri učitavanju podataka");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                textViewTrenutnoVreme.setText("Greška: nema internet konekcije");
                Toast.makeText(WeatherActivity.this,
                        "Greška: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prikaziPodatke(WeatherResponse weather) {
        String trenutno = "🌡 Temperatura: " + weather.current.temperature + "°C\n"
                + "💨 Vetar: " + weather.current.windspeed + " km/h\n"
                + "☁ Stanje: " + opisVremena(weather.current.weathercode);
        textViewTrenutnoVreme.setText(trenutno);

        StringBuilder prognoza = new StringBuilder();
        for (int i = 0; i < weather.daily.time.size(); i++) {
            prognoza.append("📅 ").append(weather.daily.time.get(i)).append("\n");
            prognoza.append("🌡 Max: ").append(weather.daily.temperatureMax.get(i))
                    .append("°C  Min: ").append(weather.daily.temperatureMin.get(i))
                    .append("°C\n");
            prognoza.append("☁ ").append(opisVremena(weather.daily.weathercode.get(i)));
            prognoza.append("\n\n");
        }
        textViewPrognoza.setText(prognoza.toString());
    }

    private String opisVremena(int code) {
        if (code == 0) return "Vedro ☀️";
        else if (code <= 2) return "Delimično oblačno ⛅";
        else if (code == 3) return "Oblačno ☁️";
        else if (code <= 49) return "Magla 🌫";
        else if (code <= 59) return "Rosulja 🌦";
        else if (code <= 69) return "Kiša 🌧";
        else if (code <= 79) return "Sneg ❄️";
        else if (code <= 82) return "Pljuskovi 🌧";
        else if (code <= 99) return "Grmljavina ⛈";
        else return "Nepoznato";
    }
}