package com.example.agrodnevnik.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "parcele")
public class Parcela {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "naziv")
    public String naziv;

    @ColumnInfo(name = "povrsina")
    public double povrsina;

    @ColumnInfo(name = "usev")
    public String usev;

    @ColumnInfo(name = "datum_setve")
    public String datumSetve;

    @ColumnInfo(name = "napomena")
    public String napomena;

    // Novo — naziv mesta za vremensku prognozu
    @ColumnInfo(name = "lokacija")
    public String lokacija;

    // Novo — koordinate koje se automatski popunjavaju
    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "longitude")
    public double longitude;

    public Parcela(String naziv, double povrsina, String usev,
                   String datumSetve, String napomena, String lokacija) {
        this.naziv = naziv;
        this.povrsina = povrsina;
        this.usev = usev;
        this.datumSetve = datumSetve;
        this.napomena = napomena;
        this.lokacija = lokacija;
        this.latitude = 0;
        this.longitude = 0;
    }
}