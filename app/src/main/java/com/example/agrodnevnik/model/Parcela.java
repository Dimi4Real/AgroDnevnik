package com.example.agrodnevnik.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "parcele")
public class Parcela {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "naziv")
    public String naziv;          // npr. "Njiva kod reke"

    @ColumnInfo(name = "povrsina")
    public double povrsina;       // u hektarima

    @ColumnInfo(name = "usev")
    public String usev;           // npr. "Kukuruz"

    @ColumnInfo(name = "datum_setve")
    public String datumSetve;     // npr. "15.04.2025"

    @ColumnInfo(name = "napomena")
    public String napomena;       // opciona beleška

    // Konstruktor - poziva se kada pravimo novu parcelu
    public Parcela(String naziv, double povrsina, String usev, String datumSetve, String napomena) {
        this.naziv = naziv;
        this.povrsina = povrsina;
        this.usev = usev;
        this.datumSetve = datumSetve;
        this.napomena = napomena;
    }
}