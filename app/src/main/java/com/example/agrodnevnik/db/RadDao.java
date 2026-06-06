package com.example.agrodnevnik.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.agrodnevnik.model.Rad;

@Dao
public interface RadDao {

    // Dohvati sve radove za odredjenu parcelu, sortirane po datumu
    @Query("SELECT * FROM radovi WHERE parcela_id = :parcelaId ORDER BY datum DESC")
    List<Rad> getByParcelaId(int parcelaId);

    // Dodaj novi rad
    @Insert
    void insert(Rad rad);

    // Izmeni postojeci rad
    @Update
    void update(Rad rad);

    // Obrisi rad
    @Delete
    void delete(Rad rad);
}