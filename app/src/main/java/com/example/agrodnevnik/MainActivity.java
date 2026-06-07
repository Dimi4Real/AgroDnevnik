package com.example.agrodnevnik;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrodnevnik.adapter.ParcelaAdapter;
import com.example.agrodnevnik.db.AppDatabase;
import com.example.agrodnevnik.model.Parcela;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private ParcelaAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicijalizacija baze
        db = AppDatabase.getInstance(this);

        // Podesavanje RecyclerView-a sa LinearLayoutManager-om
        // LinearLayoutManager prikazuje stavke jednu ispod druge
        recyclerView = findViewById(R.id.recyclerViewParcele);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Ucitavamo parcele i pravimo adapter
        List<Parcela> parcele = db.parcelaDao().getAll();
        adapter = new ParcelaAdapter(parcele, parcela -> {
            // Kada korisnik klikne na parcelu, otvaramo detalj ekran
            // i saljemo ID parcele kao extra podatak
            Intent intent = new Intent(this, ParcelaDetailActivity.class);
            intent.putExtra("parcelaId", parcela.id);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // SearchView - pretraga parcela po nazivu ili usevu
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pretraziParcele(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Pretrazujemo u realnom vremenu dok korisnik kuca
                if (newText.isEmpty()) {
                    osvezi();
                } else {
                    pretraziParcele(newText);
                }
                return true;
            }
        });

        // FAB dugme za dodavanje nove parcele
        FloatingActionButton fab = findViewById(R.id.fabDodaj);
        fab.setOnClickListener(v -> prikaziDijalogDodavanja());
    }

    // Poziva se svaki put kada se vratimo na ovaj ekran
    // npr. nakon sto smo izmenili parcelu na detalj ekranu
    @Override
    protected void onResume() {
        super.onResume();
        osvezi();
    }

    // Ucitava sve parcele iz baze i osvezava RecyclerView
    private void osvezi() {
        List<Parcela> parcele = db.parcelaDao().getAll();
        adapter.updateList(parcele);
    }

    // Pretrazuje parcele i osvezava RecyclerView sa rezultatima
    private void pretraziParcele(String query) {
        // Dodajemo % oko query-ja za SQL LIKE pretragu
        List<Parcela> rezultati = db.parcelaDao().search("%" + query + "%");
        adapter.updateList(rezultati);
    }

    // Prikazuje AlertDialog za unos podataka o novoj parceli
    private void prikaziDijalogDodavanja() {
        // Ucitavamo custom layout za dijalog
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_parcela, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nova parcela")
                .setView(dialogView)
                .setPositiveButton("Sačuvaj", null) // null da ne zatvori automatski
                .setNegativeButton("Otkaži", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                // Citamo vrednosti iz input polja
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
                String napomena = ((TextInputEditText) dialogView
                        .findViewById(R.id.editTextNapomena))
                        .getText().toString().trim();

                // Validacija - naziv i usev su obavezni
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

                // Pravimo novi objekat i cuvamo u bazu
                Parcela parcela = new Parcela(naziv, povrsina, usev, datum, napomena);
                db.parcelaDao().insert(parcela);
                osvezi();
                dialog.dismiss();
            });
        });

        dialog.show();
    }
}