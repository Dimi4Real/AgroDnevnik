package com.example.agrodnevnik;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrodnevnik.adapter.KalendarAdapter;
import com.example.agrodnevnik.db.AppDatabase;
import com.example.agrodnevnik.model.Parcela;
import com.example.agrodnevnik.model.Rad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalendar);

        Toolbar toolbar = findViewById(R.id.toolbarKalendar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        AppDatabase db = AppDatabase.getInstance(this);

        // Dohvatamo sve planirane radove
        List<Rad> planirani = db.radDao().getAllPlanirani();

        // Pravimo mapu parcelaId -> naziv parcele
        // da ne moramo za svaki rad posebno da idemo u bazu
        List<Parcela> sveParcele = db.parcelaDao().getAll();
        Map<Integer, String> parcelaNazivi = new HashMap<>();
        for (Parcela p : sveParcele) {
            parcelaNazivi.put(p.id, p.naziv);
        }

        TextView textViewPrazno = findViewById(R.id.textViewPrazno);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewKalendar);

        if (planirani.isEmpty()) {
            // Ako nema planiranih radova prikazujemo poruku
            textViewPrazno.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewPrazno.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new KalendarAdapter(planirani, parcelaNazivi));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}