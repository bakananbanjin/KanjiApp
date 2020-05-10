package com.minami.mykanji;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class KanjiDraw extends AppCompatActivity implements View.OnClickListener {

    private static final int MYKANJI = 0, ALLKANJI = 10;
    private static final int GRADE1 = 1, GRADE2 = 2, GRADE3 = 3, GRADE4 = 4, GRADE5 = 5;

    private final static int FROMKANJILIST = 1;
    private final static int FROMKANJIQUIZSETUP = 2;
    private final static int FROMEDITSTROKE = 3;
    private final static int FROMLEARN = 4;
    private final static int FROMMATCH = 5;

    private static KanjiDAO kanjiDAO;
    private static ListIterator<Kanji> listIterator;
    private static List<Kanji> allKanjis;
    private static List<Kanji> quizAllKanjis;
    private static List<Kanji> quizKanjis;
    private static Kanji myKanji;

    private static TextView tvDrawKanji, tvDrawOnYomi, tvDrawKunYomi, tvDrawTranslation, tvCor;
    private static Button btnErase, btnSkip, btnInsert, btnBack;
    private static Switch switchHint, switchKanji;
    private static boolean[] coorect;
    private static int[] intQuizArray;
    private static int KanjiTransRead;

    private static Thread t;

    private static MyFL fl;
    private static ConstraintLayout drawCL;

    private String tempDrawCode;
    private static String guessKanji_kunReading, guessKanji_onReading, guessKanji_Translation, guessKanji_Kanji;

    private static String[] kanjiStrokes;

    private static int kanjiCount = 0;
    private static boolean kanjirandom = true;
    private static int kanjiWhich = -1;
    private static int kanjiMode;
    private static int kanjiTransRead;
    private static boolean inTest = false;
    private static int quizRemaning = 0;

    private static int red, green;

    private static Handler h;
    private static Context context;
    private static Bundle extras;

    private static boolean firstfromLearn = true;
    private static ImageView imageBack, imageNext;
    private static int learnID = 0;

    //help list for if you come from Match
    private static List<Integer> solution;

    //so kanjiwill be resete if draw is recreated
    private static boolean kanjiDrawEdit = true;

    //FUUUUUUUUUUUUUU

    private static int resID;
    private static Drawable d;
    private static KanjiDrawCheck tempCheck = new KanjiDrawCheck();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_draw);

        kanjiDAO = MainActivity.getKanjiDAO();

        imageBack = findViewById(R.id.drawlearnback);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(listIterator.hasPrevious()) {
                    myKanji = listIterator.previous();
                }
                */
                fl.resetDrawCode();
                fl.erasePhat();
                switch(getIntent().getExtras().getInt("source")) {
                    case FROMLEARN:
                    if (learnID > 0) {
                        myKanji = quizKanjis.get(--learnID);
                        imageNext.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_forward));
                    } else {
                        imageBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));
                    }

                    break;
                    case FROMKANJILIST:
                    case FROMEDITSTROKE:
                        if(myKanji.getId() > 1){
                            //id starts with 1
                            myKanji = allKanjis.get(myKanji.getId() - 2);
                            imageNext.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_forward));
                        }  else {
                            imageBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));
                        }
                        break;
                }
                loadFromLearn();

            }
        });
        imageNext = findViewById(R.id.drawlearnnext);
        imageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(listIterator.hasNext()) {
                    myKanji = listIterator.next();
                }*/
                fl.resetDrawCode();
                fl.erasePhat();
                switch(getIntent().getExtras().getInt("source")) {
                    case FROMLEARN:
                    if (learnID < 4) {
                        myKanji = quizKanjis.get(++learnID);
                        imageBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_back));
                    } else {
                        imageNext.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));
                    }
                    break;
                    case FROMKANJILIST:
                    case FROMEDITSTROKE:
                        if(myKanji.getId() < allKanjis.size()){
                            //id starts with 1
                            myKanji = allKanjis.get(myKanji.getId());
                            imageBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_back));
                        }  else {
                            imageNext.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));
                        }
                        break;
                }
                loadFromLearn();
            }
        });

        drawCL = findViewById(R.id.drawCL);
        imageBack.setVisibility(View.GONE);
        imageNext.setVisibility(View.GONE);

        tvDrawKanji = findViewById(R.id.tvDrawKanji);
        tvDrawKunYomi = findViewById(R.id.tvDrawKunYomi);
        tvDrawOnYomi = findViewById(R.id.tvDrawOnYomi);
        tvDrawTranslation = findViewById(R.id.tvDrawTranslation);
        tvCor = findViewById(R.id.tvCorrection2);

        red = getResources().getColor(R.color.answerfalse);
        green = getResources().getColor(R.color.answercorrect);

        fl = findViewById(R.id.drawframlayout);

        btnErase = findViewById(R.id.btnDrawErase);
        btnErase.setOnClickListener(this);
        btnErase.bringToFront();

        btnSkip = findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(this);

        btnInsert = findViewById(R.id.btnDrawInsert);
        btnInsert.setOnClickListener(this);

        btnBack = findViewById(R.id.btnDrawBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KanjiDraw.this, ToolsActivity.class));
                finish();
            }
        });

        switchKanji = findViewById(R.id.switchDrawKanji);
        switchKanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDrawKanji.setVisibility(View.VISIBLE);
            }
        });

        switchHint = findViewById(R.id.drawSwitchHint);
        switchHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchHint.isChecked()) {
                    switchKanji.setVisibility(View.VISIBLE);
                    tvDrawTranslation.setVisibility(View.VISIBLE);
                    tvDrawKunYomi.setVisibility(View.VISIBLE);
                    tvDrawOnYomi.setVisibility(View.VISIBLE);
                    tvDrawTranslation.setVisibility(View.VISIBLE);
                }
            }
        });
        h = new Handler();
        context = getApplicationContext();
        Bundle tempExtras = getIntent().getExtras();
        extras = tempExtras;
        if(extras != null) {
            loadDrawbackground();
        }
        setTitle(getResources().getString(R.string.stringDrawKanji));
        load();
        btnErase.bringToFront();
        btnSkip.bringToFront();
    }

    public void loadDrawbackground(){
        switch(extras.getInt("source")){
            case FROMKANJILIST:
            case FROMLEARN:
                return;
        }
    }

    public static List<Kanji> getQuizKanjis() {
        return quizKanjis;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(t != null){
            t.interrupt();
        }
        inTest = false;
        myKanji = null;
        kanjiDrawEdit = true;
        firstfromLearn = true;
        learnID = 0;
        finish();
    }

    @Override
    protected void onPause() {
        firstfromLearn = true;
        learnID = 0;
        inTest = false;
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnDrawErase:
                fl.erasePhat();
                fl.resetDrawCode();
                break;
            case R.id.btnSkip:
                switch(extras.getInt("source")) {
                    case FROMKANJILIST:
                        break;
                    case FROMKANJIQUIZSETUP:
                    case FROMMATCH:
                        coorect[quizRemaning] = false;
                        fl.resetDrawCode();
                        fl.erasePhat();
                        if (quizKanjis.get(quizRemaning).getMemory() > 0) {
                            quizKanjis.get(quizRemaning).setMemory(quizKanjis.get(quizRemaning).getMemory() - 1);
                            kanjiDAO.update(quizKanjis.get(quizRemaning));
                        }
                        switchKanji.setVisibility(View.INVISIBLE);
                        load();
                        break;
                    case FROMEDITSTROKE:
                        if(allKanjis != null && myKanji != null) {
                            if(allKanjis.size() > myKanji.getId()) {

                                //id is bigger then index first kanji has id 1 and im array index 0
                                int temp = myKanji.getId();
                                myKanji = allKanjis.get(temp);
                                Kanji tempKanji = allKanjis.get(temp);
                            }
                            fl.resetDrawCode();
                            load();
                        }
                        break;
                }
                break;
            case R.id.btnDrawInsert:
                switch (extras.getInt("source")) {
                    case FROMEDITSTROKE:
                        String temp = translateStroke(fl.getDrawCode());
                        final String tempDrawCode = fl.getDrawCode();
                        if (!tempCheck.checkKanjiDraw(tempDrawCode, myKanji)) {

                            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
                            alertDialogBuilder.setTitle("Attention Database change!");
                            alertDialogBuilder.setMessage("Are you sure, You wanted to add\n" + temp + "\nas a draw possibility for\n" + myKanji.getKanji() + "?");
                            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    if (myKanji.getDraw().equals("")) {
                                        myKanji.setDraw(tempDrawCode);
                                    } else {
                                        myKanji.setDraw(myKanji.getDraw() + "," + tempDrawCode);
                                    }
                                    kanjiDAO.update(myKanji);
                                    Toast.makeText(KanjiDraw.this, "Draw insert into Database", Toast.LENGTH_SHORT).show();
                                    MainActivity.logKanjis();
                                }
                            });
                            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(KanjiDraw.this, "Draw Not insert to Database", Toast.LENGTH_SHORT).show();
                                    //finish();
                                }
                            });
                            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else {
                            Toast.makeText(KanjiDraw.this, "Draw already in Database", Toast.LENGTH_SHORT).show();
                        }

                        //tvCor.setText(translateStroke(fl.getDrawCode()));
                        fl.resetDrawCode();
                        break;
                    case FROMLEARN:
                        Intent intent = new Intent(KanjiDraw.this, MatchKanjiActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
        }
        fl.erasePhat();
    }

    private static void load(){
        t = new Thread() {
            @Override
            public void run() {

                if (extras == null) {

                        //At some point this code segment will not be reached
                        myKanji = (Kanji) kanjiDAO.selectRandom();
                        tvDrawKanji.setText(myKanji.getKanji());
                        tvDrawOnYomi.setText("音読み：" + myKanji.getOnYomi());
                        tvDrawKunYomi.setText("訓読み: " + myKanji.getKunYomi());
                        tvDrawTranslation.setText(myKanji.getTranslation());
                        kanjiStrokes = myKanji.getDraw().split(",");


                } else {
                    switch (extras.getInt("source")) {
                        case FROMKANJILIST:
                            //here later sniffer

                            if(allKanjis == null || allKanjis.size() < 2){
                                allKanjis = MainActivity.getKanjiDAO().getKanjis();
                                Log.d("APP", "All Kanjisize " + allKanjis.size() );

                            }
                            if(myKanji == null) {
                                myKanji = (Kanji) extras.getSerializable("kanji");
                            }
                            tvDrawKanji.setText(myKanji.getKanji());
                            tvDrawOnYomi.setText("音読み：" + myKanji.getOnYomi());
                            tvDrawKunYomi.setText("訓読み: " + myKanji.getKunYomi());
                            tvDrawTranslation.setText(myKanji.getTranslation());
                            kanjiStrokes = myKanji.getDraw().split(",");

                            resID = context.getResources().getIdentifier(myKanji.getImage(), "drawable", "com.minami.mykanji");
                            try {
                                d = context.getResources().getDrawable(resID);
                            } catch (Exception e) {
                             d = null;
                            }
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(d != null) {
                                        fl.setBackground(d);
                                    } else {
                                        Log.d("APP", "d = null " + myKanji.getImage() );
                                    }
                                    fl.invalidate();
                                    switchHint.setVisibility(View.INVISIBLE);
                                    btnSkip.setVisibility(View.GONE);
                                    if(myKanji.getId() == 1){
                                        imageBack.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_stop));
                                    } else {
                                        imageBack.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_back));
                                    }
                                    imageBack.setVisibility(View.VISIBLE);
                                    imageNext.setVisibility(View.VISIBLE);
                                }
                            });
                            break;

                        case FROMKANJIQUIZSETUP:
                            kanjiCount = extras.getInt("kanjiCount");
                            kanjiWhich = extras.getInt("kanjiWhich");
                            kanjirandom = extras.getBoolean("random");
                            kanjiMode = extras.getInt("source");
                            kanjiTransRead = extras.getInt("translationReading");

                            if (!inTest) {
                                //load Quiz Kanji
                                switch (kanjiWhich) {
                                    case ALLKANJI:
                                        quizAllKanjis = kanjiDAO.getKanjis();
                                        break;
                                    case MYKANJI:
                                        quizAllKanjis = kanjiDAO.getKanjiSelected();
                                        break;
                                    case GRADE1:
                                        quizAllKanjis = kanjiDAO.getKanjBySchoolLvl(GRADE1);
                                        break;
                                    case GRADE2:
                                        quizAllKanjis = kanjiDAO.getKanjBySchoolLvl(GRADE2);
                                        break;
                                    case GRADE3:
                                        quizAllKanjis = kanjiDAO.getKanjBySchoolLvl(GRADE3);
                                        break;
                                    case GRADE4:
                                        quizAllKanjis = kanjiDAO.getKanjBySchoolLvl(GRADE4);
                                        break;
                                    case GRADE5:
                                        quizAllKanjis = kanjiDAO.getKanjBySchoolLvl(GRADE5);
                                        break;
                                    default:
                                        quizAllKanjis = kanjiDAO.getKanjis();
                                        break;
                                }
                                if (quizAllKanjis.size() == 0) {
                                    Log.d("APP KanjiDraw 178", "noKanji");
                                    h.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context, "No Kanjis found pls change setup", Toast.LENGTH_LONG).show();
                                            Log.d("APP QuizActivity 146", "kanjiCount " + kanjiCount + " kanjiWhich " + kanjiWhich + " kanjiranodom " + kanjirandom +
                                                    " kanjiMode " + kanjiMode);
                                        }
                                    });
                                    return;
                                }
                                if (quizKanjis != null) {
                                    quizKanjis.clear();
                                } else {
                                    quizKanjis = new ArrayList<Kanji>();
                                }

                                quizRemaning = kanjiCount;
                                coorect = new boolean[kanjiCount];
                                for (int i : intQuizArray = new int[kanjiCount]) {
                                    coorect[i] = false;
                                    if (kanjirandom) {
                                        intQuizArray[i] = QuizActivity.randomMax(quizAllKanjis.size());
                                        quizKanjis.add(quizAllKanjis.get(intQuizArray[i]));
                                    } else {
                                        //Take the lowest from 3 Kanjis
                                        int inttemp1 = QuizActivity.randomMax(quizAllKanjis.size());
                                        int inttemp2 = QuizActivity.randomMax(quizAllKanjis.size());
                                        int inttemp3 = QuizActivity.randomMax(quizAllKanjis.size());

                                        Kanji temp1 = quizAllKanjis.get(inttemp1);
                                        Kanji temp2 = quizAllKanjis.get(inttemp2);
                                        Kanji temp3 = quizAllKanjis.get(inttemp3);

                                        if (temp1.getMemory() < temp2.getMemory()) {
                                            if (temp1.getMemory() < temp3.getMemory()) {
                                                intQuizArray[i] = inttemp3;
                                                quizKanjis.add(temp3);
                                            } else {
                                                intQuizArray[i] = inttemp3;
                                                quizKanjis.add(temp1);
                                            }
                                        } else if (temp2.getMemory() < temp3.getMemory()) {
                                            intQuizArray[i] = inttemp2;
                                            quizKanjis.add(temp2);
                                        } else {
                                            intQuizArray[i] = inttemp3;
                                            quizKanjis.add(temp3);
                                        }
                                    }
                                }
                            }
                            inTest = true;

                            if (quizRemaning > 0) {
                                Kanji temp = quizKanjis.get(--quizRemaning);
                                myKanji = temp;
                                //target = temp.getId();
                                guessKanji_Kanji = temp.getKanji();
                                guessKanji_kunReading = temp.getKunYomi();
                                guessKanji_onReading = temp.getOnYomi();
                                guessKanji_Translation = temp.getTranslation();
                                kanjiStrokes = temp.getDraw().split(",");
                            } else {
                                loadresult();
                            }
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    switchHint.setChecked(false);
                                    switchKanji.setChecked(false);
                                    tvDrawKanji.setText(guessKanji_Kanji);
                                    tvDrawOnYomi.setText("音読み：" + guessKanji_onReading);
                                    tvDrawKunYomi.setText("訓読み: " + guessKanji_kunReading);
                                    tvDrawTranslation.setText(guessKanji_Translation);
                                    loadKanjiVisibil(kanjiMode);

                                    if(quizRemaning < kanjiCount - 1 && quizRemaning > 0) {
                                        tvCor.setText(quizKanjis.get(quizRemaning + 1).getKanji() + " "
                                            + quizKanjis.get(quizRemaning + 1).getOnYomi() +
                                            " " + quizKanjis.get(quizRemaning + 1).getKunYomi() +
                                            "\n" + quizKanjis.get(quizRemaning + 1).getTranslation());
                                        if(coorect[quizRemaning + 1]) {
                                            tvCor.setBackgroundColor(green);
                                        } else {
                                            tvCor.setBackgroundColor(red);
                                        }
                                    }
                                }
                            });
                            break;
                        case FROMEDITSTROKE:
                            if(allKanjis == null){
                                allKanjis = kanjiDAO.getKanjis();
                            }
                            if(myKanji == null || kanjiDrawEdit ) {
                                myKanji = (Kanji) extras.getSerializable("kanji");
                                kanjiDrawEdit = false;
                            }
                            kanjiStrokes = myKanji.getDraw().split(",");
                            resID = context.getResources().getIdentifier(myKanji.getImage(), "drawable", "com.minami.mykanji");
                            try {
                                d = context.getResources().getDrawable(resID);
                            } catch (Exception e) {
                                d = null;
                            }
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(d != null) {
                                        fl.setBackground(d);
                                    } else {
                                        // Reset fl to default or dummy
                                    }
                                tvDrawKanji.setText(myKanji.getKanji());
                                tvDrawOnYomi.setText("音読み：" + myKanji.getOnYomi());
                                tvDrawKunYomi.setText("訓読み: " + myKanji.getKunYomi());
                                tvDrawTranslation.setText(myKanji.getTranslation());
                                btnInsert.setVisibility(View.VISIBLE);
                                btnBack.setVisibility(View.VISIBLE);

                                switchHint.setVisibility(View.INVISIBLE);
                                    if(myKanji.getId() == 1){
                                        imageBack.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_stop));
                                    } else {
                                        imageBack.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_back));
                                    }
                                    imageBack.setVisibility(View.VISIBLE);
                                    imageNext.setVisibility(View.VISIBLE);
                                //btnSkip.setVisibility(View.GONE);
                                }
                            });
                            break;
                        case FROMLEARN:
                            if(LernActivity.getKanjis() != null) {

                                if (firstfromLearn ) {
                                    quizKanjis = LernActivity.getKanjis();
                                    listIterator = quizKanjis.listIterator();
                                    firstfromLearn = false;
                                    //myKanji = listIterator.next();
                                    myKanji = quizKanjis.get(learnID);
                                }resID = context.getResources().getIdentifier(myKanji.getImage(), "drawable", "com.minami.mykanji");
                                try {
                                    d = context.getResources().getDrawable(resID);
                                } catch (Exception e) {
                                    d = null;
                                }
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(d != null) {
                                            fl.setBackground(d);
                                        }
                                        fl.invalidate();
                                        tvDrawKanji.setText(myKanji.getKanji());
                                        tvDrawOnYomi.setText("音読み：" + myKanji.getOnYomi());
                                        tvDrawKunYomi.setText("訓読み: " + myKanji.getKunYomi());
                                        tvDrawTranslation.setText(myKanji.getTranslation());
                                        kanjiStrokes = myKanji.getDraw().split(",");
                                        imageBack.setVisibility(View.VISIBLE);
                                        imageNext.setVisibility(View.VISIBLE);
                                        switchHint.setVisibility(View.GONE);
                                        btnSkip.setVisibility(View.GONE);
                                        btnInsert.setVisibility(View.VISIBLE);
                                        btnInsert.setText(R.string.btnDrawStartTest);
                                    }
                                });
                            }
                            break;
                        case FROMMATCH:
                            if (!inTest) {
                                kanjiCount = 5;
                                quizRemaning = kanjiCount;
                                coorect = new boolean[kanjiCount];
                                quizKanjis = LernActivity.getKanjis();
                                for (int i : intQuizArray = new int[kanjiCount]) {
                                    coorect[i] = false;
                                }
                                solution = new ArrayList<>();
                                for (int i = 0; i < 5 ; i++) {
                                    solution.add(i);
                                }
                                Collections.shuffle(solution);
                            }
                            inTest = true;

                            if (quizRemaning > 0) {
                                Log.d("APP KanjiDraw 178", "Remaning " + quizRemaning + " Kanjisize " + quizKanjis.size() + " solution"+solution.toString());
                                Kanji temp = myKanji = quizKanjis.get(solution.get(--quizRemaning));
                                Log.d("APP KanjiDraw 178", "Remaning " + temp.toString());
                                //target = temp.getId();
                                guessKanji_Kanji = temp.getKanji();
                                guessKanji_kunReading = temp.getKunYomi();
                                guessKanji_onReading = temp.getOnYomi();
                                guessKanji_Translation = temp.getTranslation();
                                kanjiStrokes = temp.getDraw().split(",");
                            } else {
                                //Start next test

                                for(int i = 0; i < solution.size(); i++){
                                    LernActivity.getLearnMap().insertTestValue(quizKanjis.get(solution.get(i)), coorect[i], 1);
                                }
                                Log.d("APP KanjiDraw 559", LernActivity.getLearnMap().toString());

                                Intent intent = new Intent(context, QuizActivity.class);
                                intent.putExtra("source", 5);
                                intent.putExtra("times", 1);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                context.startActivity(intent);
                            }
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    switchHint.setChecked(false);
                                    switchKanji.setChecked(false);
                                    tvDrawKanji.setText(guessKanji_Kanji);
                                    tvDrawOnYomi.setText("音読み：" + guessKanji_onReading);
                                    tvDrawKunYomi.setText("訓読み: " + guessKanji_kunReading);
                                    tvDrawTranslation.setText(guessKanji_Translation);
                                    loadKanjiVisibil(kanjiMode);

                                    if(quizRemaning < kanjiCount - 1 && quizRemaning > 0) {
                                        tvCor.setText(quizKanjis.get(solution.get(quizRemaning + 1)).getKanji() + " "
                                                + quizKanjis.get(solution.get(quizRemaning + 1)).getOnYomi() +
                                                " " + quizKanjis.get(solution.get(quizRemaning + 1)).getKunYomi() +
                                                "\n" + quizKanjis.get(solution.get(quizRemaning + 1)).getTranslation());
                                        if(coorect[quizRemaning + 1]) {
                                            tvCor.setBackgroundColor(green);
                                        } else {
                                            tvCor.setBackgroundColor(red);
                                        }
                                    }
                                }
                            });
                            break;
                    }
                }
            }
        };
        t.start();
    }

    private static void loadresult(){
        Intent intent = new Intent(context, KanjiQuizResult.class);
        inTest = false;
        intent.putExtra("correct", coorect);
        intent.putExtra("source", 2);
        context.startActivity(intent);
    }

    private static void loadKanjiVisibil(int mode){
        Log.d("APP QuizActivity 369", "kanjiMode: " + kanjiMode);
        tvDrawKanji.setVisibility(View.INVISIBLE);

                switch(kanjiTransRead) {
                    case 0:
                        tvDrawOnYomi.setVisibility(View.INVISIBLE);
                        tvDrawKunYomi.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        tvDrawTranslation.setVisibility(View.INVISIBLE);
                        break;
                }

    }

    public static String translateStroke(String DrawCode){
        String translate = "";

        char[] charDrawCode = DrawCode.toCharArray();

        for(int i = 0; i < charDrawCode.length; i++){
            //Log.d("APP KanjiDraw 429", "Position: " + i + "Symbole: " + charDrawCode[i]);
            switch(charDrawCode[i]){
                //← W ↑ N → E ↓ S ↖ w ↗ n ↘ e ↙ s
                case 'N':
                    translate += '↑';
                    break;
                case 'E':
                    translate += '→';
                    break;
                case 'S':
                    translate += '↓';
                    break;
                case 'W':
                    translate += '←';
                    break;
                case 'n':
                    translate += '↗';
                    break;
                case 'e':
                    translate += '↘';
                    break;
                case 's':
                    translate += '↙';
                    break;
                case 'w':
                    translate += '↖';
                    break;
                case 'x':
                    translate += '|';
                default:
                    break;
            }
        }

        return translate;
    }

    public void loadFromLearn(){
        resID = context.getResources().getIdentifier(myKanji.getImage(), "drawable", "com.minami.mykanji");
        try {
            d = context.getResources().getDrawable(resID);
        } catch (Exception e) {
            d = null;
        }
        if(d != null) {
            fl.setBackground(d);
            fl.invalidate();
        }
        tvDrawKanji.setText(myKanji.getKanji());
        tvDrawOnYomi.setText("音読み：" + myKanji.getOnYomi());
        tvDrawKunYomi.setText("訓読み: " + myKanji.getKunYomi());
        tvDrawTranslation.setText(myKanji.getTranslation());
        kanjiStrokes = myKanji.getDraw().split(",");
        switchHint.setVisibility(View.GONE);
        btnSkip.setVisibility(View.GONE);

    }

    public static class MyFL extends View {

        private Paint paint = new Paint();
        private GestureDetector gDetector;
        private Path path = new Path();
        private float savedPositionX, savedPositionY;
        private String drawCode;
        private char lastchar;

        private float mMinDistance;
        long movecount;


        public MyFL(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            this.movecount = 0;
            this.drawCode = "";
            this.lastchar = 'M';
            gDetector = new GestureDetector(context, new MyGestureListener());
            this.mMinDistance = 10.0f;

            paint.setAntiAlias(true);
            paint.setStrokeWidth(20.0f);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
        }

        @Override
        public void onDraw(Canvas canvas) {
            paint.setColor(Color.WHITE);
            canvas.drawPath(path, paint);
        }
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mMinDistance = ((float)this.getWidth()) / 20.0f;

            //padding and other boarders not accounted for.... redo but for now it works good enough
            if(eventX < 0 || eventY < 0 || eventX > this.getWidth() || eventY > this.getHeight()){
                path.moveTo(eventX, eventY);
                return false;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    savedPositionX = event.getX();
                    savedPositionY = event.getY();
                    path.moveTo(eventX, eventY);
                    return true;
                case MotionEvent.ACTION_MOVE:

                    float dist = (float) Math.sqrt(Math.pow(eventX - savedPositionX, 2) + Math.pow(eventY - savedPositionY, 2));
                    if (dist > mMinDistance){
                        //set Coordination relativ to the point (0,0)
                        float coordX = eventX - savedPositionX;
                        float coordY = eventY - savedPositionY;
                        //right of the coordsystem
                        if((coordX) > 0){

                            if(0.5 * coordX < coordY && 2 * coordX > coordY){
                                //SouthEast e
                                if(lastchar != 'e') {
                                    drawCode += "e";
                                    lastchar = 'e';
                                }
                            } else if( -0.5 * coordX < coordY && 2 * coordX > coordY) {
                                //East E
                                if(lastchar != 'E') {
                                    drawCode += "E";
                                    lastchar = 'E';
                                }
                            } else if( -2 * coordX < coordY && 2 * coordX > coordY) {
                                //NorthEast n
                                if(lastchar != 'n') {
                                    drawCode += "n";
                                    lastchar = 'n';
                                }
                            } else {
                                if(coordY > 0){
                                    //South S
                                    if(lastchar != 'S') {
                                        drawCode += "S";
                                        lastchar = 'S';
                                    }
                                } else {
                                    //North N
                                    if(lastchar != 'N') {
                                        drawCode += "N";
                                        lastchar = 'N';
                                    }
                                }
                            }
                        } else {
                            if(0.5 * coordX < coordY && -0.5 * coordX > coordY ){
                                //West W
                                if(lastchar != 'W') {
                                    drawCode += "W";
                                    lastchar = 'W';
                                }
                            } else if( 0.5 * coordX < coordY && -2 * coordX > coordY) {
                                //SouthWest s (lowercase)
                                if(lastchar != 's') {
                                    drawCode += "s";
                                    lastchar = 's';
                                }
                            } else if(0.5 * coordX > coordY && 2 * coordX < coordY) {
                                //NorthWest w (lowercase)
                                if(lastchar != 'w') {
                                    drawCode += "w";
                                    lastchar = 'w';
                                }
                            } else {
                                if(coordY > 0){
                                    //South S
                                    if(lastchar != 'S') {
                                        drawCode += "S";
                                        lastchar = 'S';
                                    }
                                } else {
                                    //North N
                                    if(lastchar != 'N') {
                                        drawCode += "N";
                                        lastchar = 'N';
                                    }
                                }
                            }
                        }
                        savedPositionX = eventX;
                        savedPositionY = eventY;

                    }
                    path.lineTo(eventX, eventY);
                    movecount++;
                    break;
                case MotionEvent.ACTION_UP:
                    // Important distance check otherwise at drawCode can start with x
                    //helpPath.reset();
                    if(lastchar != 'x' || lastchar != 'M') {
                        this.drawCode += "x";
                    }
                    lastchar = 'M';

                    switch (extras.getInt("source")) {
                        case FROMKANJILIST:
                        case FROMLEARN:

                            testStroke(kanjiStrokes);
                            break;

                        case FROMKANJIQUIZSETUP:
                        case FROMMATCH:
                            if(testStroke(kanjiStrokes)) {
                                coorect[quizRemaning] = true;
                                resetDrawCode();
                                if (!switchKanji.isChecked()) {
                                    if (quizKanjis.get(quizRemaning).getMemory() < 20) {
                                        quizKanjis.get(quizRemaning).setMemory(quizKanjis.get(quizRemaning).getMemory() + 1);
                                        kanjiDAO.update(quizKanjis.get(quizRemaning));
                                    }
                                }
                                Thread t = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(200);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                erasePhat();
                                            }
                                        });
                                    }
                                };
                                t.start();
                                load();
                                break;
                            }
                            break;
                        case FROMEDITSTROKE:
                            kanjiStrokes = myKanji.getDraw().split(",");
                            testStroke(kanjiStrokes);
                            break;
                    }
                    break;
                default:
                    return false;
            }
            //gDetector.onTouchEvent(event);


            /*Log.d("APP onTouchEvent End", "X:" + event.getX() + " : Y: " + event.getY() + " movecount: " + movecount + " " +
                    Calendar.getInstance().getTimeInMillis() +" " + this.getWidth() + " " + drawCode + " " + mMinDistance);*/

            //testStroke(kanjiStrokes);
            invalidate();

            return true;
        }
        public void erasePhat() {
            path.reset();
            drawCL.setBackgroundColor(getResources().getColor(R.color.backgroundgrey));
            invalidate();
        }
        private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        /*
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            path.reset();
            Log.d("APP com.minami.mykanji", "Tapped at: (" + e.getX() + "," + e.getY() + ")");
            invalidate();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            path.reset();
            invalidate();
        }*/
        }
        public String getDrawCode() {
            return drawCode;
        }
        public void resetDrawCode() {
            this.drawCode = "";
        }
        public boolean testStroke(String[] strokes){
            if(tempCheck.checkKanjiDraw(drawCode, myKanji)){
                drawCL.setBackgroundColor(Color.GREEN);
                return true;
            }
            drawCL.setBackgroundColor(Color.RED);
            return false;
        }
 /*
        public boolean testStroke(String[]strokes, String code){
            if(strokes.length == 0){
                return false;
            }
            for(int i = 0; i < strokes.length ; i++){
                if(strokes[i].equals(code)){
                    drawCL.setBackgroundColor(Color.GREEN);
                    return true;
                }
            }
            drawCL.setBackgroundColor(Color.RED);
            return false;
        }
*/
    }
}