package com.example.agrodnevnik.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrodnevnik.R;
import com.example.agrodnevnik.model.Rad;

import java.util.List;

public class RadAdapter extends RecyclerView.Adapter<RadAdapter.RadViewHolder> {

    private List<Rad> radovi;

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
        holder.opisTextView.setText(rad.opis != null ? rad.opis : "");

        // Postavljamo tekst i boju statusa
        if ("Planiran".equals(rad.status)) {
            holder.statusTextView.setText("📅 Planiran");
            // Menjamo boju pozadine badge-a u narandzastu
            GradientDrawable bg = (GradientDrawable) ContextCompat
                    .getDrawable(holder.itemView.getContext(), R.drawable.bg_status).mutate();
            bg.setColor(ContextCompat.getColor(
                    holder.itemView.getContext(), R.color.status_planiran));
            holder.statusTextView.setBackground(bg);
        } else {
            holder.statusTextView.setText("✅ Obavljen");
            GradientDrawable bg = (GradientDrawable) ContextCompat
                    .getDrawable(holder.itemView.getContext(), R.drawable.bg_status).mutate();
            bg.setColor(ContextCompat.getColor(
                    holder.itemView.getContext(), R.color.status_obavljen));
            holder.statusTextView.setBackground(bg);
        }

        holder.editButton.setOnClickListener(v -> listener.onEditClick(rad));
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
        TextView statusTextView;
        ImageButton editButton;
        ImageButton deleteButton;

        public RadViewHolder(@NonNull View itemView) {
            super(itemView);
            tipRadaTextView = itemView.findViewById(R.id.textViewTipRada);
            datumTextView = itemView.findViewById(R.id.textViewDatum);
            opisTextView = itemView.findViewById(R.id.textViewOpis);
            statusTextView = itemView.findViewById(R.id.textViewStatus);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}