package com.example.agrodnevnik.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrodnevnik.R;
import com.example.agrodnevnik.model.Rad;

import java.util.List;

public class RadAdapter extends RecyclerView.Adapter<RadAdapter.RadViewHolder> {

    private List<Rad> radovi;

    // Interfejs sa metodama za izmenu i brisanje rada
    public interface OnRadClickListener {
        void onEditClick(Rad rad);
        void onDeleteClick(Rad rad);
    }

    private OnRadClickListener listener;

    public RadAdapter(List<Rad> radovi, OnRadClickListener listener) {
        this.radovi = radovi;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rad, parent, false);
        return new RadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadViewHolder holder, int position) {
        Rad rad = radovi.get(position);
        holder.tipRadaTextView.setText(rad.tipRada);
        holder.datumTextView.setText(rad.datum);
        holder.opisTextView.setText(rad.opis);

        // Dugme za izmenu
        holder.editButton.setOnClickListener(v -> listener.onEditClick(rad));

        // Dugme za brisanje
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(rad));
    }

    @Override
    public int getItemCount() {
        return radovi.size();
    }

    public void updateList(List<Rad> noviRadovi) {
        this.radovi = noviRadovi;
        notifyDataSetChanged();
    }

    public static class RadViewHolder extends RecyclerView.ViewHolder {
        TextView tipRadaTextView;
        TextView datumTextView;
        TextView opisTextView;
        ImageButton editButton;
        ImageButton deleteButton;

        public RadViewHolder(@NonNull View itemView) {
            super(itemView);
            tipRadaTextView = itemView.findViewById(R.id.textViewTipRada);
            datumTextView = itemView.findViewById(R.id.textViewDatum);
            opisTextView = itemView.findViewById(R.id.textViewOpis);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}