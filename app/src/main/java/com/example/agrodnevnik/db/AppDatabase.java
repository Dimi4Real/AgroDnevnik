package com.example.agrodnevnik.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.agrodnevnik.model.Parcela;
import com.example.agrodnevnik.model.Rad;

// Registrujemo sve entitete (tabele) i postavljamo verziju baze
@Database(entities = {Parcela.class, Rad.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // Metode koje vracaju DAO objekte
    public abstract ParcelaDao parcelaDao();
    public abstract RadDao radDao();

    // Singleton - ako baza vec postoji vrati je, ako ne napravi novu
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "agrodnevnik_db"
                    )
                    .allowMainThreadQueries() // dozvoljavamo upite na glavnom threadu
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}