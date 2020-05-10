package com.minami.mykanji;

import android.arch.persistence.room.*;
import android.support.annotation.NonNull;

@Entity
public class Radical {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    int id;
    int strokeCount;
    String draw;

    public Radical (int id, int strokeCount, String draw){
        this.id = id;
        this.strokeCount = strokeCount;
        this.draw = draw;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStrokeCount() {
        return strokeCount;
    }

    public void setStrokeCount(int strokeCount) {
        this.strokeCount = strokeCount;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String toString(){
        return (id + " Strokecount: " + strokeCount +" Drawcode: " + draw );
    }
}
