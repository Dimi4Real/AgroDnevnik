package com.example.agrodnevnik;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.agrodnevnik.db.AppDatabase;
import com.example.agrodnevnik.model.Parcela;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class UsevInfoFragment extends Fragment {

    private AppDatabase db;
    private Parcela parcela;
    private int parcelaId;

    private TextView textViewNaziv;
    private TextView textViewUsev;
    private TextView textViewPovrsina;
    private TextView textViewDatum;
    private TextView textViewLokacija;
    private TextView textViewNapomena;

    public interface OnParcelaUpdatedListener {
        void onParcelaUpdated();
    }

    private OnParcelaUpdatedListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnParcelaUpdatedListener) {
            listener = (OnParcelaUpdatedListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_usev_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getInstance(requireContext());

        if (getArguments() != null) {
            parcelaId = getArguments().getInt("parcelaId");
        }

        textViewNaziv = view.findViewById(R.id.textViewNazivParcele);
        textViewUsev = view.findViewById(R.id.textViewUsevInfo);
        textViewPovrsina = view.findViewById(R.id.textViewPovrsina);
        textViewDatum = view.findViewById(R.id.textViewDatumSetve);
        textViewLokacija = view.findViewById(R.id.textViewLokacijaInfo);
        textViewNapomena = view.findViewById(R.id.textViewNapomena);

        MaterialButton buttonIzmeni = view.findViewById(R.id.buttonIzmeniParcelu);
        MaterialButton buttonVrijeme = view.findViewById(R.id.buttonVrijeme);

        ucitajPodatke();

        buttonIzmeni.setOnClickListener(v -> prikaziDijalogIzmene());

        buttonVrijeme.setOnClickListener(v -> {
            // Saljemo koordinate parcele na WeatherActivity
            Intent intent = new Intent(requireContext(), WeatherActivity.class);
            intent.putExtra("nazivParcele", parcela.naziv);
            intent.putExtra("latitude", parcela.latitude);
            intent.putExtra("longitude", parcela.longitude);
            intent.putExtra("lokacija", parcela.lokacija);
            startActivity(intent);
        });
    }

    private void ucitajPodatke() {
        parcela = db.parcelaDao().getById(parcelaId);
        if (parcela != null) {
            textViewNaziv.setText("🌾 " + parcela.naziv);
            textViewUsev.setText("🌱 Usev: " + parcela.usev);
            textViewPovrsina.setText("📐 Površina: " + parcela.povrsina + " ha");
            textViewDatum.setText("📅 Datum setve: " + parcela.datumSetve);
            textViewNapomena.setText("📝 Napomena: " + parcela.napomena);

            if (parcela.lokacija != null && !parcela.lokacija.isEmpty()) {
                textViewLokacija.setText("📍 Lokacija: " + parcela.lokacija);
                textViewLokacija.setVisibility(View.VISIBLE);
            } else {
                textViewLokacija.setVisibility(View.GONE);
            }
        }
    }

    private void prikaziDijalogIzmene() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_parcela, null);

        ((TextInputEditText) dialogView.findViewById(R.id.editTextNaziv))
                .setText(parcela.naziv);
        ((TextInputEditText) dialogView.findViewById(R.id.editTextPovrsina))
                .setText(String.valueOf(parcela.povrsina));
        ((TextInputEditText) dialogView.findViewById(R.id.editTextUsev))
                .setText(parcela.usev);
        ((TextInputEditText) dialogView.findViewById(R.id.editTextDatum))
                .setText(parcela.datumSetve);
        ((TextInputEditText) dialogView.findViewById(R.id.editTextLokacija))
                .setText(parcela.lokacija);
        ((TextInputEditText) dialogView.findViewById(R.id.editTextNapomena))
                .setText(parcela.napomena);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("✏️ Izmeni parcelu")
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
                    Toast.makeText(requireContext(), "Naziv i usev su obavezni",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                double povrsina = 0;
                try {
                    povrsina = Double.parseDouble(povrsinaStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Neispravan format površine",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                parcela.naziv = naziv;
                parcela.povrsina = povrsina;
                parcela.usev = usev;
                parcela.datumSetve = datum;
                parcela.napomena = napomena;
                parcela.lokacija = lokacija;
                db.parcelaDao().update(parcela);

                ucitajPodatke();

                if (listener != null) {
                    listener.onParcelaUpdated();
                }

                dialog.dismiss();
            });
        });

        dialog.show();
    }
}