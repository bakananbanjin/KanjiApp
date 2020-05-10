package com.minami.mykanji;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchKanjiActivity extends AppCompatActivity implements View.OnClickListener {

    private final int MYKANJI = 0, ALLKANJI = 10;
    private final int GRADE1 = 1, GRADE2 = 2, GRADE3 = 3, GRADE4 = 4, GRADE5 = 5;
    private final int FROMQUIZSETUP = 2;
    private final int MATCHKANJISCOUNT = 5;

    private Button[] btnMatch;
    private TextView tvMatchResult;
    private Button btnMatchDefault;
    private List<Kanji> kanjis;
    private List<MyMap> mapKanjis;
    private Thread t;
    private Handler h;
    private int firstChoose = -1;
    private int secondChoose = -1;
    private int breakHelpint1, breakHelpint2;
    private Bundle extras;
    private int source = 0;
    private int kanjiWhich;
    private int tryCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_kanji);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x/3;
        int height = size.y/8;
        tryCount = 1;

        extras = getIntent().getExtras();
        if(extras != null){
            source = extras.getInt("source");
            kanjiWhich = extras.getInt("kanjiWhich");
        }

        tvMatchResult = findViewById(R.id.tvMatchResult);

        btnMatchDefault = findViewById(R.id.btnMatchDefault);
        btnMatchDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(source == 0) {
                    Intent intent = new Intent(MatchKanjiActivity.this, KanjiDraw.class);
                    intent.putExtra("source", 5);
                    startActivity(intent);
                } else {
                    Intent intent1 = new Intent(MatchKanjiActivity.this, MainActivity.class);
                    startActivity(intent1);
                }
            }
        });

        btnMatch = new Button[10];
        btnMatch[0] = findViewById(R.id.btnMatch1);
        btnMatch[1] = findViewById(R.id.btnMatch2);
        btnMatch[2] = findViewById(R.id.btnMatch3);
        btnMatch[3] = findViewById(R.id.btnMatch4);
        btnMatch[4] = findViewById(R.id.btnMatch5);
        btnMatch[5] = findViewById(R.id.btnMatch6);
        btnMatch[6] = findViewById(R.id.btnMatch7);
        btnMatch[7] = findViewById(R.id.btnMatch8);
        btnMatch[8] = findViewById(R.id.btnMatch9);
        btnMatch[9] = findViewById(R.id.btnMatch10);

        if(source == 0) {
            kanjis = new ArrayList<Kanji>();
            kanjis = LernActivity.getKanjis();
        } else {
            selectKanjisFromSetup();
        }

        mapKanjis = new ArrayList<MyMap>();
        for (int i = 0; i < kanjis.size(); i++) {
            mapKanjis.add(new MyMap(kanjis.get(i).getKanji()));
        }

        for(int i = 0; i < btnMatch.length; i++){
            btnMatch[i].setOnClickListener(this);
            btnMatch[i].setWidth(width);
            btnMatch[i].setHeight(height);
            btnMatch[i].setText(setButtonText(i));
        }
    }

    @Override
    public void onClick(View v) {

        if(firstChoose == -1) {
            switch (v.getId()) {
                case R.id.btnMatch1:
                    firstChoose = 0;
                    break;
                case R.id.btnMatch2:
                    firstChoose = 1;
                    break;
                case R.id.btnMatch3:
                    firstChoose = 2;
                    break;
                case R.id.btnMatch4:
                    firstChoose = 3;
                    break;
                case R.id.btnMatch5:
                    firstChoose = 4;
                    break;
                case R.id.btnMatch6:
                    firstChoose = 5;
                    break;
                case R.id.btnMatch7:
                    firstChoose = 6;
                    break;
                case R.id.btnMatch8:
                    firstChoose = 7;
                    break;
                case R.id.btnMatch9:
                    firstChoose = 8;
                    break;
                case R.id.btnMatch10:
                    firstChoose = 9;
                    break;
            }
            btnMatch[firstChoose].setBackgroundColor(Color.BLUE);
        } else {
            if (test(v.getId())) {
                btnMatch[firstChoose].setBackgroundColor(Color.GREEN);
                btnMatch[secondChoose].setBackgroundColor(Color.GREEN);

                btnMatch[firstChoose].setVisibility(View.GONE);
                btnMatch[secondChoose].setVisibility(View.GONE);

                    if(allBtnGone()){
                        tvMatchResult.setText("You needed:\n" + tryCount +"\ntrys.");
                        tvMatchResult.setVisibility(View.VISIBLE);
                        btnMatchDefault.setVisibility(View.VISIBLE);
                    }

            } else {
                btnMatch[firstChoose].setBackground(btnMatchDefault.getBackground());
            }
            tryCount++;
            firstChoose = -1;
            secondChoose = -1;
        }
    }

    private void load(){
        t = new Thread(){
            @Override
            public void run() {
            }
        };
    }

    private String setButtonText(int value){
        String tempString = "";
        boolean tempboolean = false;

        while (!tempboolean) {

            int tempint = MainActivity.randomMax(mapKanjis.size() - 1);
            int tempint2 = MainActivity.randomMax(1);

            if (tempint2 == 1) {
                if (mapKanjis.get(tempint).firstValue == -1) {
                    tempString = kanjis.get(tempint).getKanji();
                    tempboolean = true;
                    mapKanjis.get(tempint).setFirstValue(value);
                }
            } else {
                if (mapKanjis.get(tempint).secondvalue == -1) {
                    //tempString = kanjis.get(tempint).getOnYomi() + " " + kanjis.get(tempint).getKunYomi();
                    tempString = kanjis.get(tempint).getTranslation();
                    tempboolean = true;
                    mapKanjis.get(tempint).setSecondvalue(value);
                }
            }
        }
        return tempString;
    }

    private boolean test(int value){
        switch (value) {
            case R.id.btnMatch1:
                secondChoose = 0;
                break;
            case R.id.btnMatch2:
                secondChoose = 1;
                break;
            case R.id.btnMatch3:
                secondChoose = 2;
                break;
            case R.id.btnMatch4:
                secondChoose = 3;
                break;
            case R.id.btnMatch5:
                secondChoose = 4;
                break;
            case R.id.btnMatch6:
                secondChoose = 5;
                break;
            case R.id.btnMatch7:
                secondChoose = 6;
                break;
            case R.id.btnMatch8:
                secondChoose = 7;
                break;
            case R.id.btnMatch9:
                secondChoose = 8;
                break;
            case R.id.btnMatch10:
                secondChoose = 9;
                break;
        }
        Log.d("APP MatchKanjiActivity", "First " + firstChoose + " Second " + secondChoose);
        for(int i = 0; i < mapKanjis.size(); i++){
            if((mapKanjis.get(i).getFirstValue() == firstChoose || mapKanjis.get(i).getFirstValue() == secondChoose)
                    && ( mapKanjis.get(i).getSecondvalue() == firstChoose || mapKanjis.get(i).getSecondvalue() == secondChoose)){
                btnMatch[firstChoose].setBackgroundColor(Color.GREEN);
                btnMatch[secondChoose].setBackgroundColor(Color.GREEN);
                return true;
            }
        }
        return false;
    }

    private boolean allBtnGone(){
        boolean tempBoolean = true;
        for(int i = 0; i < btnMatch.length; i++){
            if(btnMatch[i].getVisibility() != View.GONE){
                tempBoolean = false;
            }
        }
        return tempBoolean;
    }

    private void selectKanjisFromSetup(){

        switch (kanjiWhich) {
            case ALLKANJI:
                kanjis = MainActivity.getKanjiDAO().getKanjisRandomLimit(MATCHKANJISCOUNT);
                break;
            case MYKANJI:
                kanjis = MainActivity.getKanjiDAO().getKanjiSelectedRandomLimit(MATCHKANJISCOUNT);
                break;
            case GRADE1:
                kanjis = MainActivity.getKanjiDAO().getKanjBySchoolLvlRandomLimit(GRADE1, MATCHKANJISCOUNT);
                break;
            case GRADE2:
                kanjis = MainActivity.getKanjiDAO().getKanjBySchoolLvlRandomLimit(GRADE2, MATCHKANJISCOUNT);
                break;
            case GRADE3:
                kanjis = MainActivity.getKanjiDAO().getKanjBySchoolLvlRandomLimit(GRADE3, MATCHKANJISCOUNT);
                break;
            case GRADE4:
                kanjis = MainActivity.getKanjiDAO().getKanjBySchoolLvlRandomLimit(GRADE4, MATCHKANJISCOUNT);
                break;
            case GRADE5:
                kanjis = MainActivity.getKanjiDAO().getKanjBySchoolLvlRandomLimit(GRADE5, MATCHKANJISCOUNT);
                break;
            default:
                kanjis = MainActivity.getKanjiDAO().getKanjisRandomLimit(MATCHKANJISCOUNT);
                break;
        }
        if (kanjis == null || kanjis.size() < 5) {
            Log.d("APP KanjiDraw 178", "noKanji");
            kanjis = MainActivity.getKanjiDAO().getKanjisRandomLimit(MATCHKANJISCOUNT);
            startActivity(new Intent(MatchKanjiActivity.this, MainActivity.class));
            finish();
            return;
        }
    }
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private class MyMap{
        private String id;
        private int firstValue;
        private int secondvalue;
        private int buttonId;
        MyMap(String id){
            this.id = id;
            firstValue = -1;
            secondvalue = -1;
            buttonId = -1;
        }

        public String toString(){
            return id + " " + firstValue + " " + secondvalue + " " + buttonId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getFirstValue() {
            return firstValue;
        }

        public void setFirstValue(int firstValue) {
            this.firstValue = firstValue;
        }

        public int getSecondvalue() {
            return secondvalue;
        }

        public void setSecondvalue(int secondvalue) {
            this.secondvalue = secondvalue;
        }

        public int getButtonId() {
            return buttonId;
        }

        public void setButtonId(int buttonId) {
            this.buttonId = buttonId;
        }
    }
}
