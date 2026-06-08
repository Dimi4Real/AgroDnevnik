package com.example.agrodnevnik.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrodnevnik.R;
import com.example.agrodnevnik.model.Parcela;

import java.util.List;

public class ParcelaAdapter extends RecyclerView.Adapter<ParcelaAdapter.ParcelaViewHolder> {

    private List<Parcela> parcele;

    public interface OnParcelaClickListener {
        void onParcelaClick(Parcela parcela);
    }

    private OnParcelaClickListener listener;

    public ParcelaAdapter(List<Parcela> parcele, OnParcelaClickListener listener) {
        this.parcele = parcele;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParcelaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parcela, parent, false);
        return new ParcelaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParcelaViewHolder holder, int position) {
        Parcela parcela = parcele.get(position);
        holder.nazivTextView.setText(parcela.naziv);
        holder.usevTextView.setText(parcela.usev);
        holder.povrsinaTextView.setText(parcela.povrsina + " ha");

        // Prikazujemo lokaciju ako je uneta
        if (parcela.lokacija != null && !parcela.lokacija.isEmpty()) {
            holder.lokacijaTextView.setText(parcela.lokacija);
            holder.lokacijaTextView.setVisibility(View.VISIBLE);
        } else {
            holder.lokacijaTextView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onParcelaClick(parcela));
    }

    @Override
    public int getItemCount() {
        return parcele.size();
    }

    public void updateList(List<Parcela> noveParcele) {
        this.parcele = noveParcele;
        notifyDataSetChanged();
    }

    public static class ParcelaViewHolder extends RecyclerView.ViewHolder {
        TextView nazivTextView;
        TextView usevTextView;
        TextView povrsinaTextView;
        TextView lokacijaTextView;

        public ParcelaViewHolder(@NonNull View itemView) {
            super(itemView);
            nazivTextView = itemView.findViewById(R.id.textViewNaziv);
            usevTextView = itemView.findViewById(R.id.textViewUsev);
            povrsinaTextView = itemView.findViewById(R.id.textViewPovrsina);
            lokacijaTextView = itemView.findViewById(R.id.textViewLokacija);
        }
    }
}