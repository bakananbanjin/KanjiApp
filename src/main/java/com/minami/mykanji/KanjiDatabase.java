package com.minami.mykanji;

import android.arch.persistence.room.*;
import android.content.Context;

@Database(entities = {Kanji.class, Radical.class}, version = 1)
public abstract class KanjiDatabase extends RoomDatabase {
    private static KanjiDatabase instance;

    public abstract  KanjiDAO getKanjiDAO();
    public abstract RadicalDAO getRadicalDAO();
    public static synchronized KanjiDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, KanjiDatabase.class, "kanji.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
