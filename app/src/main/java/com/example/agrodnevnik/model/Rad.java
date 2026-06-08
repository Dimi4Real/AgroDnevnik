package com.example.agrodnevnik.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "radovi")
public class Rad {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "parcela_id")
    public int parcelaId;

    @ColumnInfo(name = "tip_rada")
    public String tipRada;

    @ColumnInfo(name = "datum")
    public String datum;

    @ColumnInfo(name = "opis")
    public String opis;

    // Novo — "Obavljen" ili "Planiran"
    @ColumnInfo(name = "status")
    public String status;

    public Rad(int parcelaId, String tipRada, String datum, String opis, String status) {
        this.parcelaId = parcelaId;
        this.tipRada = tipRada;
        this.datum = datum;
        this.opis = opis;
        this.status = status;
    }
}