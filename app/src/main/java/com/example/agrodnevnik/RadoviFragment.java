package com.example.agrodnevnik;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrodnevnik.adapter.RadAdapter;
import com.example.agrodnevnik.db.AppDatabase;
import com.example.agrodnevnik.model.Rad;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class RadoviFragment extends Fragment {

    private AppDatabase db;
    private RadAdapter adapter;
    private int parcelaId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_radovi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getInstance(requireContext());

        if (getArguments() != null) {
            parcelaId = getArguments().getInt("parcelaId");
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRadovi);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Rad> radovi = db.radDao().getByParcelaId(parcelaId);
        adapter = new RadAdapter(radovi, new RadAdapter.OnRadClickListener() {
            @Override
            public void onEditClick(Rad rad) {
                prikaziDijalogIzmene(rad);
            }

            @Override
            public void onDeleteClick(Rad rad) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Brisanje rada")
                        .setMessage("Da li ste sigurni?")
                        .setPositiveButton("Obriši", (d, w) -> {
                            db.radDao().delete(rad);
                            osvezi();
                        })
                        .setNegativeButton("Otkaži", null)
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabDodajRad);
        fab.setOnClickListener(v -> prikaziDijalogDodavanja());
    }

    public void osvezi() {
        List<Rad> radovi = db.radDao().getByParcelaId(parcelaId);
        adapter.updateList(radovi);
    }

    private void prikaziDijalogDodavanja() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_rad, null);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("🌿 Novi rad")
                .setView(dialogView)
                .setPositiveButton("Sačuvaj", null)
                .setNegativeButton("Otkaži", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String tipRada = ((TextInputEditText) dialogView
                        .findViewById(R.id.editTextTipRada))
                        .getText().toString().trim();
                String datum = ((TextInputEditText) dialogView
                        .findViewById(R.id.editTextDatumRada))
                        .getText().toString().trim();
                String opis = ((TextInputEditText) dialogView
                        .findViewById(R.id.editTextOpisRada))
                        .getText().toString().trim();

                // Citamo koji radio button je selektovan
                RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroupStatus);
                String status = radioGroup.getCheckedRadioButtonId()
                        == R.id.radioPlaniran ? "Planiran" : "Obavljen";

                if (tipRada.isEmpty()) {
                    Toast.makeText(requireContext(), "Tip rada je obavezan",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Rad rad = new Rad(parcelaId, tipRada, datum, opis, status);
                db.radDao().insert(rad);
                osvezi();
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void prikaziDijalogIzmene(Rad rad) {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_rad, null);

        ((TextInputEditText) dialogView.findViewById(R.id.editTextTipRada))
                .setText(rad.tipRada);
        ((TextInputEditText) dialogView.findViewById(R.id.editTextDatumRada))
                .setText(rad.datum);
        ((TextInputEditText) dialogView.findViewById(R.id.editTextOpisRada))
                .setText(rad.opis);

        // Podesavamo radio button prema postojecem statusu
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroupStatus);
        if ("Planiran".equals(rad.status)) {
            radioGroup.check(R.id.radioPlaniran);
        } else {
            radioGroup.check(R.id.radioObavljen);
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("✏️ Izmeni rad")
                .setView(dialogView)
                .setPositiveButton("Sačuvaj", null)
                .setNegativeButton("Otkaži", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String tipRada = ((TextInputEditText) dialogView
                        .findViewById(R.id.editTextTipRada))
                        .getText().toString().trim();
                String datum = ((TextInputEditText) dialogView
                        .findViewById(R.id.editTextDatumRada))
                        .getText().toString().trim();
                String opis = ((TextInputEditText) dialogView
                        .findViewById(R.id.editTextOpisRada))
                        .getText().toString().trim();
                String status = radioGroup.getCheckedRadioButtonId()
                        == R.id.radioPlaniran ? "Planiran" : "Obavljen";

                if (tipRada.isEmpty()) {
                    Toast.makeText(requireContext(), "Tip rada je obavezan",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                rad.tipRada = tipRada;
                rad.datum = datum;
                rad.opis = opis;
                rad.status = status;
                db.radDao().update(rad);
                osvezi();
                dialog.dismiss();
            });
        });

        dialog.show();
    }
}