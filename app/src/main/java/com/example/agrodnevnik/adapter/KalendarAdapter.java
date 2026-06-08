package com.example.agrodnevnik.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrodnevnik.R;
import com.example.agrodnevnik.model.Rad;

import java.util.List;
import java.util.Map;

public class KalendarAdapter extends RecyclerView.Adapter<KalendarAdapter.KalendarViewHolder> {

    private List<Rad> radovi;
    // Mapa parcelaId -> naziv parcele da mozemo prikazati naziv uz svaki rad
    private Map<Integer, String> parcelaNazivi;

    public KalendarAdapter(List<Rad> radovi, Map<Integer, String> parcelaNazivi) {
        this.radovi = radovi;
        this.parcelaNazivi = parcelaNazivi;
    }

    @NonNull
    @Override
    public KalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kalendar, parent, false);
        return new KalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KalendarViewHolder holder, int position) {
        Rad rad = radovi.get(position);

        holder.datumTextView.setText("📅 " + rad.datum);
        holder.tipRadaTextView.setText(rad.tipRada);
        holder.opisTextView.setText(rad.opis != null ? rad.opis : "");

        // Prikazujemo naziv parcele kojoj rad pripada
        String nazivParcele = parcelaNazivi.get(rad.parcelaId);
        if (nazivParcele != null) {
            holder.parcelaTextView.setText("🌾 " + nazivParcele);
        }
    }

    @Override
    public int getItemCount() {
        return radovi.size();
    }

    public static class KalendarViewHolder extends RecyclerView.ViewHolder {
        TextView datumTextView;
        TextView tipRadaTextView;
        TextView opisTextView;
        TextView parcelaTextView;

        public KalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            datumTextView = itemView.findViewById(R.id.textViewKalendarDatum);
            tipRadaTextView = itemView.findViewById(R.id.textViewKalendarTipRada);
            opisTextView = itemView.findViewById(R.id.textViewKalendarOpis);
            parcelaTextView = itemView.findViewById(R.id.textViewKalendarParcela);
        }
    }
}