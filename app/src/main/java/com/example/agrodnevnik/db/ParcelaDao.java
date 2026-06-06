package com.example.agrodnevnik.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.agrodnevnik.model.Parcela;

@Dao
public interface ParcelaDao {

    // Dohvati sve parcele, sortirane po nazivu
    @Query("SELECT * FROM parcele ORDER BY naziv ASC")
    List<Parcela> getAll();

    // Pretraga parcela po nazivu ili usevu
    @Query("SELECT * FROM parcele WHERE naziv LIKE :query OR usev LIKE :query")
    List<Parcela> search(String query);

    // Dohvati jednu parcelu po ID-u
    @Query("SELECT * FROM parcele WHERE id = :id")
    Parcela getById(int id);

    // Dodaj novu parcelu, vraca ID novog reda
    @Insert
    long insert(Parcela parcela);

    // Izmeni postojecu parcelu
    @Update
    void update(Parcela parcela);

    // Obrisi parcelu
    @Delete
    void delete(Parcela parcela);
}