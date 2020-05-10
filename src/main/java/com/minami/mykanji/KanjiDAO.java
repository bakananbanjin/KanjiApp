package com.minami.mykanji;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface KanjiDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Kanji... kanjis);

    @Update
    void update(Kanji... kanjis);

    @Query("DELETE FROM Kanji")
    void deleteAll();
    @Query("DELETE FROM Kanji WHERE id = :id")
    void deleteById(int id);


    //Select All Kanjis

    @Query("SELECT * FROM Kanji")
    List<Kanji> getKanjis();

    @Query("SELECT * FROM Kanji ORDER BY RANDOM() LIMIT :limit")
    List<Kanji> getKanjisRandomLimit(int limit);

    @Query("SELECT * FROM Kanji ORDER BY id ASC")
    List<Kanji> getKanjiSortedByID();

    @Query("SELECT * FROM Kanji ORDER BY memory DESC")
    List<Kanji> getKanjiSortedByMemory();

    @Query("SELECT * FROM Kanji ORDER BY strokeCount DESC")
    List<Kanji> getKanjiSortedByStrokeCount();

    //Select MyListKanjis

    @Query("SELECT * FROM Kanji WHERE selected")
    List<Kanji> getKanjiSelected();

    @Query("SELECT * FROM Kanji WHERE selected ORDER BY RANDOM() LIMIT :limit")
    List<Kanji> getKanjiSelectedRandomLimit(int limit);

    @Query("SELECT * FROM Kanji WHERE selected ORDER BY memory DESC")
    List<Kanji> getKanjiSelectedSortedByMemory();

    @Query("SELECT * FROM Kanji WHERE selected ORDER BY strokeCount DESC")
    List<Kanji> getKanjiSelectedSortedByStrokeCount();

    @Query("SELECT * FROM Kanji WHERE selected ORDER BY id ASC")
    List<Kanji> getKanjiSelectedSortedByID();

    //Select BySchoolgrade

    @Query("SELECT * FROM Kanji WHERE lvl = :value")
    List<Kanji> getKanjBySchoolLvl(int value);

    @Query("SELECT * FROM Kanji WHERE lvl = :value ORDER BY RANDOM() LIMIT :limit")
    List<Kanji> getKanjBySchoolLvlRandomLimit(int value, int limit);

    @Query("SELECT * FROM Kanji WHERE lvl = :value  ORDER BY id ASC")
    List<Kanji> getKanjiBySchoolLvlSortedByID(int value);

    @Query("SELECT * FROM Kanji WHERE lvl = :value  ORDER BY memory DESC")
    List<Kanji> getKanjiBySchoolLvlSortedBYMemory(int value);

    @Query("SELECT * FROM Kanji WHERE lvl = :value  ORDER BY strokeCount DESC")
    List<Kanji> getKanjiBySchoolLvlSortedByStrokeCount(int value);

    //Select By Memory

    @Query("SELECT * FROM Kanji WHERE memory BETWEEN :value1 AND :value2")
    List<Kanji> getKanjiBetweenLvl( int value1, int value2);

    @Query("SELECT * FROM Kanji WHERE memory BETWEEN :value1 AND :value2 ORDER BY id ASC")
    List<Kanji> getKanjiBetweenLvlSortedByID( int value1, int value2);

    @Query("SELECT * FROM Kanji WHERE memory BETWEEN :value1 AND :value2 ORDER BY memory DESC")
    List<Kanji> getKanjiBetweenLvlSortedByMemory( int value1, int value2);

    @Query("SELECT * FROM Kanji WHERE memory BETWEEN :value1 AND :value2 ORDER BY strokeCount DESC")
    List<Kanji> getKanjiBetweenLvlSortedByStrokeCount( int value1, int value2);


    //Random Kanjis

    @Query("SELECT * FROM Kanji ORDER BY RANDOM() LIMIT 1")
    Kanji selectRandom();

    @Query("SELECT * FROM Kanji ORDER BY RANDOM() LIMIT :number")
    Kanji selectRandomNumber(int number);

    @Query("SELECT * FROM Kanji WHERE id = :idNumber")
    Kanji selectKanjiById(int idNumber);

    @Query("SELECT * FROM Kanji WHERE kanji = :kanji LIMIT 1")
    Kanji selectKanjiByKanji(String kanji);

    @Query("SELECT * FROM Kanji ORDER BY id DESC LIMIT 1")
    Kanji selectLastKanji();

    @Query("SELECT * FROM Kanji LIMIT :limit")
    List<Kanji> selectKanjiLimit(int limit);



    //Change Kanji
    @Query("UPDATE kanji SET id = :idneu WHERE id = :idalt")
    void changeKanjiId(int idneu, int idalt);


    //more Select

    @Query("SELECT * FROM Kanji WHERE selected = :selected ORDER BY id ASC Limit :limit")
    List<Kanji> getKanjisSortedByIDLimit(boolean selected, int limit);

    @Query("SELECT * FROM Kanji WHERE selected = :selected ORDER BY RANDOM() ASC Limit :limit")
    List<Kanji> getKanjisRandomLimit(boolean selected, int limit);

}
