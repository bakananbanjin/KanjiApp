package com.minami.mykanji;

import android.arch.persistence.room.*;

import java.io.Serializable;

@Entity
public class Kanji implements Serializable {


    public Kanji(int id, String kanji, String onYomi, String kunYomi, String translation, int lvl,
                 int memory, int strokeCount, boolean selected){
        this.id = id;
        this.kanji = kanji;
        this.kunYomi = kunYomi;
        this.onYomi = onYomi;
        this.translation = translation;
        this.lvl = lvl;
        this.memory = memory;
        this.strokeCount = strokeCount;
        this.selected = selected;
        this.draw ="";
        this.image = "k0000";
        this.radicalCode = "";
    }
    public Kanji(int id, String kanji, String onYomi, String kunYomi, String translation, int lvl,
                 int memory, int strokeCount, boolean selected, String draw, String image){
        this.id = id;
        this.kanji = kanji;
        this.kunYomi = kunYomi;
        this.onYomi = onYomi;
        this.translation = translation;
        this.lvl = lvl;
        this.memory = memory;
        this.strokeCount = strokeCount;
        this.selected = selected;
        this.draw = draw;
        this.image = image;
        this.radicalCode = "";
    }

    public Kanji(int id, String kanji, String onYomi, String kunYomi, String translation, int lvl,
                 int memory, int strokeCount, boolean selected, String draw, String image, String radicalCode){
        this.id = id;
        this.kanji = kanji;
        this.kunYomi = kunYomi;
        this.onYomi = onYomi;
        this.translation = translation;
        this.lvl = lvl;
        this.memory = memory;
        this.strokeCount = strokeCount;
        this.selected = selected;
        this.draw = draw;
        this.image = image;
        this.radicalCode = radicalCode;
    }

    public Kanji(){

        this.kanji = "";
        this.kunYomi = "";
        this.onYomi = "";
        this.translation = "";
        this.lvl = 0;
        this.memory = 0;
        this.strokeCount = 0;
        this.selected = false;
        this.draw ="";
        this.image = "k0000";
        this.radicalCode = "x";
    }

    @PrimaryKey(autoGenerate = false)
    private int id;
    private String kanji;
    private String kunYomi;
    private String onYomi;
    private String translation;
    private int lvl;
    private int memory;
    private int strokeCount;
    private boolean selected;
    private String draw;
    private String image;
    private String radicalCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getKunYomi() {
        return kunYomi;
    }

    public void setKunYomi(String kunYomi) {
        this.kunYomi = kunYomi;
    }

    public String getOnYomi() {
        return onYomi;
    }

    public void setOnYomi(String onYomi) {
        this.onYomi = onYomi;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public int getStrokeCount() {
        return strokeCount;
    }

    public void setStrokeCount(int strokeCount) {
        this.strokeCount = strokeCount;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String toString(){
        return (id + " " + kanji + " onYomi: " + onYomi + " kunYomi: " + kunYomi + " translation: " + translation + " Draw: " + draw
        + "Grade: " + lvl + " Image: " + image);
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRadicalCode() {
        return radicalCode;
    }

    public void setRadicalCode(String radicalCode) {
        this.radicalCode = radicalCode;
    }
}
