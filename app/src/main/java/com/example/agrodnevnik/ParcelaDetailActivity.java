package com.example.agrodnevnik;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agrodnevnik.UsevInfoFragment.OnParcelaUpdatedListener;

public class ParcelaDetailActivity extends AppCompatActivity
        implements OnParcelaUpdatedListener {

    private RadoviFragment radoviFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcela_detail);

        // Citamo parcelaId koji nam je MainActivity poslao
        int parcelaId = getIntent().getIntExtra("parcelaId", -1);

        // Pravimo Bundle sa parcelaId koji cemo proslediti fragmentima
        // Bundle je Android nacin prosledjivanja podataka fragmentima
        Bundle args = new Bundle();
        args.putInt("parcelaId", parcelaId);

        // Pravimo i prikazujemo UsevInfoFragment
        UsevInfoFragment usevInfoFragment = new UsevInfoFragment();
        usevInfoFragment.setArguments(args);

        // Pravimo i prikazujemo RadoviFragment
        // Cuvamo referencu jer cemo ga pozivati kada se parcela izmeni
        radoviFragment = new RadoviFragment();
        radoviFragment.setArguments(args);

        // Dodajemo fragmente u FragmentContainerView iz layouta
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentUsevInfo, usevInfoFragment)
                .replace(R.id.fragmentRadovi, radoviFragment)
                .commit();
    }

    // Ova metoda se poziva iz UsevInfoFragment kada se parcela izmeni
    // Aktivnost dalje poziva osvezi() na RadoviFragment
    // Ovo je komunikacija izmedju fragmenata kroz aktivnost
    @Override
    public void onParcelaUpdated() {
        if (radoviFragment != null) {
            radoviFragment.osvezi();
        }
    }
}