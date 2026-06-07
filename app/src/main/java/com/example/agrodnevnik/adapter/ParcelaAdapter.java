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

    // Interfejs za klik na parcelu - aktivnost ce implementirati ovo
    public interface OnParcelaClickListener {
        void onParcelaClick(Parcela parcela);
    }

    private OnParcelaClickListener listener;

    public ParcelaAdapter(List<Parcela> parcele, OnParcelaClickListener listener) {
        this.parcele = parcele;
        this.listener = listener;
    }

    // Pravi novi ViewHolder - ucitava XML layout za jednu stavku liste
    @NonNull
    @Override
    public ParcelaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parcela, parent, false);
        return new ParcelaViewHolder(view);
    }

    // Puni ViewHolder podacima za odredjenu poziciju u listi
    @Override
    public void onBindViewHolder(@NonNull ParcelaViewHolder holder, int position) {
        Parcela parcela = parcele.get(position);
        holder.nazivTextView.setText(parcela.naziv);
        holder.usevTextView.setText(parcela.usev);
        holder.povrsinaTextView.setText(parcela.povrsina + " ha");

        // Kada korisnik klikne na stavku, poziva se listener
        holder.itemView.setOnClickListener(v -> listener.onParcelaClick(parcela));
    }

    @Override
    public int getItemCount() {
        return parcele.size();
    }

    // Azurira listu i osvezava prikaz
    public void updateList(List<Parcela> noveParcele) {
        this.parcele = noveParcele;
        notifyDataSetChanged();
    }

    // ViewHolder cuva reference na views jedne stavke liste
    public static class ParcelaViewHolder extends RecyclerView.ViewHolder {
        TextView nazivTextView;
        TextView usevTextView;
        TextView povrsinaTextView;

        public ParcelaViewHolder(@NonNull View itemView) {
            super(itemView);
            nazivTextView = itemView.findViewById(R.id.textViewNaziv);
            usevTextView = itemView.findViewById(R.id.textViewUsev);
            povrsinaTextView = itemView.findViewById(R.id.textViewPovrsina);
        }
    }
}