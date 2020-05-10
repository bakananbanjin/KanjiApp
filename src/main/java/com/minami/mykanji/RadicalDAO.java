package com.minami.mykanji;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RadicalDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (Radical... radicals);

    @Update
    void update(Radical... radicals);

    //Select all Radicals

    @Query("SELECT * FROM Radical")
    List<Radical> getAllRadicals();


}
