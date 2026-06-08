package com.example.agrodnevnik;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrodnevnik.adapter.ParcelaAdapter;
import com.example.agrodnevnik.api.NominatimClient;
import com.example.agrodnevnik.db.AppDatabase;
import com.example.agrodnevnik.model.Parcela;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private ParcelaAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Podesavamo toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = AppDatabase.getInstance(this);

        recyclerView = findViewById(R.id.recyclerViewParcele);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Parcela> parcele = db.parcelaDao().getAll();
        adapter = new ParcelaAdapter(parcele, parcela -> {
            Intent intent = new Intent(this, ParcelaDetailActivity.class);
            intent.putExtra("parcelaId", parcela.id);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pretraziParcele(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    osvezi();
                } else {
                    pretraziParcele(newText);
                }
                return true;
            }
        });

        // Dugme za kalendar otvara KalendarActivity
        MaterialButton buttonKalendar = findViewById(R.id.buttonKalendar);
        buttonKalendar.setOnClickListener(v -> {
            Intent intent = new Intent(this, KalendarActivity.class);
            startActivity(intent);
        });

        FloatingActionButton fab = findViewById(R.id.fabDodaj);
        fab.setOnClickListener(v -> prikaziDijalogDodavanja());
    }

    @Override
    protected void onResume() {
        super.onResume();
        osvezi();
    }

    private void osvezi() {
        List<Parcela> parcele = db.parcelaDao().getAll();
        adapter.updateList(parcele);
    }

    private void pretraziParcele(String query) {
        List<Parcela> rezultati = db.parcelaDao().search("%" + query + "%");
        adapter.updateList(rezultati);
    }

    private void prikaziDijalogDodavanja() {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_parcela, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("🌾 Nova parcela")
                .setView(dialogView)
                .setPositiveButton("Sačuvaj", null)
                .setNegativeButton("Otkaži", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String naziv = ((TextInputEditText) dialogView
                        .findViewById(R.id.editTextNaziv))
                        .getText().toString().trim();
                String povrsinaStr = ((TextInputEditText) dialogView
                        .findViewById(R.id.editTextPovrsina))
                        .getText().toString().trim();
                String usev = ((TextInputEditText) dialogView
                        .findViewById(R.id.editTextUsev))
                        .getText().toString().trim();
                String datum = ((TextInputEditText) dialogView
                        .findViewById(R.id.editTextDatum))
                        .getText().toString().trim();
                String lokacija = ((TextInputEditText) dialogView
                        .findViewById(R.id.editTextLokacija))
                        .getText().toString().trim();
                String napomena = ((TextInputEditText) dialogView
                        .findViewById(R.id.editTextNapomena))
                        .getText().toString().trim();

                if (naziv.isEmpty() || usev.isEmpty()) {
                    Toast.makeText(this, "Naziv i usev su obavezni",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                double povrsina = 0;
                if (!povrsinaStr.isEmpty()) {
                    try {
                        povrsina = Double.parseDouble(povrsinaStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Neispravan format površine",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Parcela parcela = new Parcela(naziv, povrsina, usev,
                        datum, napomena, lokacija);
                long id = db.parcelaDao().insert(parcela);

                // Ako je lokacija uneta, automatski uzimamo koordinate
                if (!lokacija.isEmpty()) {
                    Toast.makeText(this, "Tražim koordinate za " + lokacija + "...",
                            Toast.LENGTH_SHORT).show();

                    NominatimClient.getInstance().getCoordinates(lokacija,
                            new NominatimClient.OnCoordinatesListener() {
                                @Override
                                public void onSuccess(double latitude, double longitude) {
                                    // Ucitavamo parcelu iz baze i dodajemo koordinate
                                    Parcela saved = db.parcelaDao().getById((int) id);
                                    if (saved != null) {
                                        saved.latitude = latitude;
                                        saved.longitude = longitude;
                                        db.parcelaDao().update(saved);
                                        Toast.makeText(MainActivity.this,
                                                "Lokacija pronađena ✓",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(String message) {
                                    Toast.makeText(MainActivity.this,
                                            "Lokacija nije pronađena, koristiće se podrazumevane koordinate",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                }

                osvezi();
                dialog.dismiss();
            });
        });

        dialog.show();
    }
}