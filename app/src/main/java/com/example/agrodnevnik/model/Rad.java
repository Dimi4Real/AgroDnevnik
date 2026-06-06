package com.example.agrodnevnik.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "radovi")
public class Rad {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "parcela_id")
    public int parcelaId;         // kojoj parceli pripada ovaj rad

    @ColumnInfo(name = "tip_rada")
    public String tipRada;        // npr. "Oranje", "Đubrenje", "Prskanje"

    @ColumnInfo(name = "datum")
    public String datum;          // kada je rad obavljen

    @ColumnInfo(name = "opis")
    public String opis;           // detalji o radu

    // Konstruktor
    public Rad(int parcelaId, String tipRada, String datum, String opis) {
        this.parcelaId = parcelaId;
        this.tipRada = tipRada;
        this.datum = datum;
        this.opis = opis;
    }
}