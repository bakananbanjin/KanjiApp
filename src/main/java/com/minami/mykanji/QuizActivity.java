package com.minami.mykanji;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener{

    private final int MYKANJI = 0, ALLKANJI = 10, LEARNKANJI = 30;
    private final int GRADE1 = 1, GRADE2 = 2, GRADE3 = 3, GRADE4 = 4, GRADE5 = 5;
    private final int KANJIGUESS = 4, GUESSTRANSREAD = 3, FROMLEARN = 5;

    private static KanjiDAO kanjiDAO;
    static List<Kanji> allKanjis;
    private static List<Kanji> quizKanjis;
    private static List<Kanji> quizAllKanjis;

    private Button[] btnQuizSelect;
    private int target;
    private Handler h;
    private TextView tvTargetKanji, tvTargetTrans, tvTargetKun, tvTargetOn;
    private TextView tvCor;
    private Thread t;
    private boolean[] coorect;

    private boolean[] correctDraw;
    private boolean[] correctTest1;

    //hilfsvariable zum Random....
    private int[] intTemp = {-1, -1, -1, -1};
    private int[] intQuizArray;
    private static int quizRemaning = 0;

    private String guessKanji_kunReading, guessKanji_onReading, guessKanji_Translation, guessKanji_Kanji;
    private String randomKanji1_ans;
    private String randomKanji2_ans;
    private String randomKanji3_ans;
    private String guessKanji_ans;

    private boolean inTest = false;

    private Switch switchHint;

    private static int kanjiCount, kanjiWhich, kanjiMode, kanjiTransRead;
    private boolean kanjirandom;
    private static List<Integer> solution;
    private int fromMatchTimes;
    private int learntimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            this.kanjiMode = extras.getInt("source");
            switch (kanjiMode) {
                case KANJIGUESS:
                case GUESSTRANSREAD:
                    this.kanjiCount = extras.getInt("kanjiCount");
                    this.kanjiWhich = extras.getInt("kanjiWhich");
                    this.kanjirandom = extras.getBoolean("random");
                    this.kanjiTransRead = extras.getInt("translationReading");
                    break;
                case FROMLEARN:
                    this.kanjiCount = 5;
                    this.kanjiTransRead = 0;
                    this.kanjiWhich = 30;
                    this.correctDraw = extras.getBooleanArray("correctDraw");
                    this.learntimes = extras.getInt("times");
                    if(extras.getInt("times") != 1) {
                        this.correctTest1 = extras.getBooleanArray("correctQuiz1");
                        this.kanjiTransRead = 1;
                    }
                    this.fromMatchTimes = extras.getInt("times");
                    break;
            }

            Log.d("APP QuizActivity 60", "kanjiCount " + kanjiCount + " kanjiWhich " + kanjiWhich + " kanjiranodom "+ kanjirandom +
                    " kanjiMode " + kanjiMode);
        }

        kanjiDAO = MainActivity.getKanjiDAO();

        if(quizKanjis == null){
            quizKanjis = new ArrayList<>();
            Log.d("APP QuizActivity 73", "Init quizKanjis");
        }

        switchHint = findViewById(R.id.switchQuizHint);
        switchHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchHint.isChecked()){
                    switch(kanjiMode){
                        case KANJIGUESS:
                            switch(kanjiTransRead) {
                                case 0:
                                    tvTargetOn.setVisibility(View.VISIBLE);
                                    tvTargetKun.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    tvTargetTrans.setVisibility(View.VISIBLE);
                                    break;
                            }
                            break;
                        case GUESSTRANSREAD:
                            switch (kanjiTransRead){
                                case 0:
                                    tvTargetOn.setVisibility(View.VISIBLE);
                                    tvTargetKun.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    tvTargetTrans.setVisibility(View.VISIBLE);
                                    break;
                            }
                            break;
                        case FROMLEARN:
                            switch(kanjiTransRead){
                                case 0:
                                    tvTargetOn.setVisibility(View.VISIBLE);
                                    tvTargetKun.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    tvTargetTrans.setVisibility(View.VISIBLE);
                                    break;
                            }
                            break;
                    }
                }
            }
        });

        tvTargetKanji = findViewById(R.id.tvlearnRKanji);
        tvTargetTrans = findViewById(R.id.tvquiztranslation);
        tvTargetKun = findViewById(R.id.tvquizkunYomi);
        tvTargetOn = findViewById(R.id.tvlearnOn);
        tvTargetTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTargetTrans.setText(guessKanji_Translation);
            }
        });

        tvCor = findViewById(R.id.tvCorrection);

        btnQuizSelect = new Button[4];
        btnQuizSelect[0] = findViewById(R.id.btnQuizSelect1);
        btnQuizSelect[1] = findViewById(R.id.btnQuizSelect2);
        btnQuizSelect[2] = findViewById(R.id.btnQuizSelect3);
        btnQuizSelect[3] = findViewById(R.id.btnQuizSelect4);

        for(int i = 0; i < btnQuizSelect.length; i++){

            btnQuizSelect[i].setOnClickListener(this);
            btnQuizSelect[i].setText("Erleditgt" + " " + i);
        }

        h = new Handler();
        btnQuizSelect[0].setEnabled(false);
        btnQuizSelect[1].setEnabled(false);
        btnQuizSelect[2].setEnabled(false);
        btnQuizSelect[3].setEnabled(false);


        Log.d("APP QuizActivity 93", " Before load");
        this.load();

        if(allKanjis != null) {
            Log.d("APP QuizActivity 97", "KanjiListSize:" + allKanjis.size());
        }

    }

    private void load() {
       t = new Thread() {
            @Override
            public void run() {
                //Database queary only if new Test
                if(!inTest) {
                    //all Kanjis for wrong answer posibility
                    allKanjis = kanjiDAO.getKanjis();
                    Log.d("APP QuizActivity 112", "DB request all Kanjis");
                    //Select Kanji for test
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
                        case LEARNKANJI:
                            quizAllKanjis = LernActivity.getKanjis();
                            break;
                        default:
                            quizAllKanjis = kanjiDAO.getKanjis();
                            break;
                    }
                    Log.d("APP QuizActivity 141", "DB request quizAllKanjis");
                    if (allKanjis.size() == 0 || quizAllKanjis.size() == 0) {
                        Log.d("APP QuizActivity 142", "noKanji");
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "No Kanjis found pls change setup", Toast.LENGTH_LONG).show();
                                Log.d("APP QuizActivity 146", "kanjiCount " + kanjiCount + " kanjiWhich " + kanjiWhich + " kanjiranodom " + kanjirandom +
                                        " kanjiMode " + kanjiMode);
                            }
                        });
                        return;
                    }
                        quizKanjis.clear();
                        coorect = new boolean[kanjiCount];
                        Log.d("APP QuizActivity 154", "DB intQuizArry init");
                        if (kanjiMode != FROMLEARN) {
                            for (int i : intQuizArray = new int[kanjiCount]) {
                                coorect[i] = true;
                                if (kanjirandom) {
                                    intQuizArray[i] = randomMax(quizAllKanjis.size());
                                    quizKanjis.add(quizAllKanjis.get(intQuizArray[i]));
                                } else {
                                    //Take the lowest from 3 Kanjis
                                    int inttemp1 = randomMax(quizAllKanjis.size());
                                    int inttemp2 = randomMax(quizAllKanjis.size());
                                    int inttemp3 = randomMax(quizAllKanjis.size());

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
                        } else {
                            solution = new ArrayList<>();
                            for (int i = 0; i < 5 ; i++) {
                                solution.add(i);
                            }
                            Collections.shuffle(solution);
                            intQuizArray = new int[kanjiCount];
                            for (int i = 0; i < intQuizArray.length; i++)  {
                                Log.d("APP QuizActivity 281", "++++++++++++++++++++++++++++++++++++++++++++++++++++: " + i);
                                coorect[i] = true;
                                quizKanjis.add(quizAllKanjis.get(solution.get(i)));
                            }
                        }
                    quizRemaning = kanjiCount;
                }
                Log.d("APP QuizActivity 186", "after DB requests: quizRemaning: " + quizRemaning + " quizKanjis size: " + quizKanjis.size());
                inTest = true;

                if(quizRemaning > 0 ) {
                    Kanji temp;
                    if(kanjiMode == FROMLEARN){
                        temp = quizKanjis.get(solution.get(--quizRemaning));
                    } else {
                        temp = quizKanjis.get(--quizRemaning);
                    }
                    target = temp.getId();
                    guessKanji_Kanji = temp.getKanji();
                    guessKanji_kunReading = temp.getKunYomi();
                    guessKanji_onReading = temp.getOnYomi();
                    guessKanji_Translation = temp.getTranslation();
                    Log.d("APP QuizActivity 210", "after Target setup");

                    Kanji randomKanji1, randomKanji2, randomKanji3;
                    do {
                        randomKanji1 = allKanjis.get(randomMax(allKanjis.size()));
                        randomKanji2 = allKanjis.get(randomMax(allKanjis.size()));
                        randomKanji3 = allKanjis.get(randomMax(allKanjis.size()));
                    } while (randomKanji1.getId() == target || randomKanji2.getId() == target || randomKanji3.getId() == target);
                    // needed if you want not same Button answers
                    // || randomKanji1.getId() == randomKanji2.getId() || randomKanji2.getId() == randomKanji3.getId() || randomKanji1.getId() == randomKanji3.getId());

                    //Button text random
                    intTemp[1] = intTemp[2] = intTemp[3] = -1;
                    intTemp[0] = randomMax(4);
                    do {
                        intTemp[1] = randomMax(4);
                    } while (intTemp[0] == intTemp[1]);
                    do {
                        intTemp[2] = randomMax(4);
                    } while (intTemp[0] == intTemp[2] || intTemp[1] == intTemp[2]);
                    for (int i = 0; i < intTemp.length; i++) {
                        if (i != intTemp[0] && i != intTemp[1] && i != intTemp[2]) {
                            intTemp[3] = i;
                        }
                    }

                    switch(kanjiMode){
                        case KANJIGUESS:
                            randomKanji1_ans = randomKanji1.getKanji();
                            randomKanji2_ans = randomKanji2.getKanji();
                            randomKanji3_ans = randomKanji3.getKanji();
                            guessKanji_ans = temp.getKanji();
                            break;
                        case GUESSTRANSREAD:
                            switch(kanjiTransRead) {
                                case 0:
                                    randomKanji1_ans = randomKanji1.getTranslation();
                                    randomKanji2_ans = randomKanji2.getTranslation();
                                    randomKanji3_ans = randomKanji3.getTranslation();
                                    guessKanji_ans = temp.getTranslation();
                                    break;
                                case 1:
                                    randomKanji1_ans = randomKanji1.getOnYomi() + "  " + randomKanji1.getKunYomi();
                                    randomKanji2_ans = randomKanji2.getOnYomi() + "  " + randomKanji2.getKunYomi();
                                    randomKanji3_ans = randomKanji3.getOnYomi() + "  " + randomKanji3.getKunYomi();
                                    guessKanji_ans = temp.getOnYomi() + "  " + temp.getKunYomi();
                                    break;
                            }
                            break;
                        case FROMLEARN:
                            switch(learntimes) {
                                case 1:
                                    randomKanji1_ans = randomKanji1.getTranslation();
                                    randomKanji2_ans = randomKanji2.getTranslation();
                                    randomKanji3_ans = randomKanji3.getTranslation();
                                    guessKanji_ans = temp.getTranslation();
                                    break;
                                default:
                                    randomKanji1_ans = randomKanji1.getOnYomi() + "  " + randomKanji1.getKunYomi();
                                    randomKanji2_ans = randomKanji2.getOnYomi() + "  " + randomKanji2.getKunYomi();
                                    randomKanji3_ans = randomKanji3.getOnYomi() + "  " + randomKanji3.getKunYomi();
                                    guessKanji_ans = temp.getOnYomi() + "  " + temp.getKunYomi();
                                    break;
                            }
                    }
                } else {
                    if(kanjiMode  == FROMLEARN)
                    {
                        if(fromMatchTimes != 2) {
                            for(int i = 0; i < solution.size(); i++){
                                LernActivity.getLearnMap().insertTestValue(quizKanjis.get(solution.get(i)), coorect[i], 2);
                            }
                            Log.d("APP KanjiDraw 559", LernActivity.getLearnMap().toString());
                            Intent intent = new Intent(QuizActivity.this, QuizActivity.class);
                            intent.putExtra("source", 5);
                            intent.putExtra("times", 2);
                            startActivity(intent);
                        } else {
                            for(int i = 0; i < solution.size(); i++){
                                LernActivity.getLearnMap().insertTestValue(quizKanjis.get(solution.get(i)), coorect[i], 3);
                            }
                            Log.d("APP KanjiDraw 559", LernActivity.getLearnMap().toString());
                            Intent intent = new Intent(QuizActivity.this, LearnResult.class);
                            intent.putExtra("source", 5);
                            startActivity(intent);
                        }
                    } else {
                        loadresult();
                    }
                }
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        tvTargetKanji.setText(guessKanji_Kanji);
                        tvTargetKun.setText(guessKanji_kunReading);
                        tvTargetOn.setText(guessKanji_onReading);
                        tvTargetTrans.setText(guessKanji_Translation);

                        tvTargetKun.setVisibility(View.VISIBLE);
                        tvTargetOn.setVisibility(View.VISIBLE);
                        tvTargetKanji.setVisibility(View.VISIBLE);
                        tvTargetTrans.setVisibility(View.VISIBLE);

                        loadKanjiVisibil(kanjiMode);

                        btnQuizSelect[intTemp[0]].setText(guessKanji_ans);
                        btnQuizSelect[intTemp[1]].setText(randomKanji2_ans);
                        btnQuizSelect[intTemp[2]].setText(randomKanji1_ans);
                        btnQuizSelect[intTemp[3]].setText(randomKanji3_ans);

                        btnQuizSelect[intTemp[0]].setEnabled(true);
                        btnQuizSelect[intTemp[1]].setEnabled(true);
                        btnQuizSelect[intTemp[2]].setEnabled(true);
                        btnQuizSelect[intTemp[3]].setEnabled(true);
                    }
                });

            }
        };

        t.start();

    }

    int randomWithRange(int min, int max) {
        int range = Math.abs(max - min) + 1;
        return (int)(Math.random() * range) + (min <= max ? min : max);
    }

    public static int randomMax(int max)
    {
        return (int) (Math.random() * max);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(t != null){
           t.interrupt();
        }
        inTest = false;
        Log.d("APP QuizActivity 275", "Destroy");
    }

    @Override
    public void onClick(View v) {
        //Zugriff aendern und optimieren
        //ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.quizLayout);

        switchHint.setChecked(false);

        if(v.getId() == btnQuizSelect[intTemp[0]].getId()){
            coorect[quizRemaning] = true;

            int color = Color.parseColor("#00FF00");

            tvCor.setBackgroundColor(getResources().getColor(R.color.answercorrect));
            tvCor.setText(quizKanjis.get(quizRemaning).getKanji() + "\n" + quizKanjis.get(quizRemaning).getOnYomi()  +
                    "\n" + quizKanjis.get(quizRemaning).getKunYomi() +"\n" + quizKanjis.get(quizRemaning).getTranslation());

            if( quizKanjis.get(quizRemaning).getMemory() < 20) {
                quizKanjis.get(quizRemaning).setMemory(quizKanjis.get(quizRemaning).getMemory() + 1);
                kanjiDAO.update(quizKanjis.get(quizRemaning));
                }
        } else  {
            coorect[quizRemaning] = false;
            int color = getResources().getColor(R.color.colorPrimaryDark);

            tvCor.setBackgroundColor(getResources().getColor(R.color.answerfalse));
            tvCor.setText(quizKanjis.get(quizRemaning).getKanji() + "\n" + quizKanjis.get(quizRemaning).getOnYomi()  +
                    "\n" + quizKanjis.get(quizRemaning).getKunYomi() +"\n" + quizKanjis.get(quizRemaning).getTranslation());
            if( quizKanjis.get(quizRemaning).getMemory() > 0){
                quizKanjis.get(quizRemaning).setMemory( quizKanjis.get(quizRemaning).getMemory() - 2);
                kanjiDAO.update(quizKanjis.get(quizRemaning));
            }
        }
        //here test count.... if(testcount >= testnumbers){return to MainActivity or show result}
        btnQuizSelect[0].setEnabled(false);
        btnQuizSelect[1].setEnabled(false);
        btnQuizSelect[2].setEnabled(false);
        btnQuizSelect[3].setEnabled(false);
        Log.d("APP QuizActivit", "start load");
        this.load();

    }

    private void loadKanjiVisibil(int mode){

        Log.d("APP QuizActivity 369", "kanjiMode: " + kanjiMode);
        switch(kanjiMode){
            case KANJIGUESS:
                tvTargetKanji.setVisibility(View.INVISIBLE);
                switch(kanjiTransRead) {
                    case 0:
                        tvTargetOn.setVisibility(View.INVISIBLE);
                        tvTargetKun.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        tvTargetTrans.setVisibility(View.INVISIBLE);
                        break;
                }
                break;
            case GUESSTRANSREAD:
                Log.d("APP QuizActivity 384", "after Target setup");
                tvTargetOn.setVisibility(View.INVISIBLE);
                tvTargetKun.setVisibility(View.INVISIBLE);
                tvTargetTrans.setVisibility(View.INVISIBLE);
                break;
            case FROMLEARN:
                switch(learntimes) {
                    case 1:
                    default:
                        tvTargetOn.setVisibility(View.INVISIBLE);
                        tvTargetKun.setVisibility(View.INVISIBLE);
                        tvTargetTrans.setVisibility(View.INVISIBLE);
                        break;
                }
                break;
        }
    }

    private void loadresult(){
        Intent intent = new Intent(QuizActivity.this, KanjiQuizResult.class);
        inTest = false;
        intent.putExtra("correct", coorect);
        intent.putExtra("source", 1);
        startActivity(intent);

    }

    public static List<Kanji> getQuizKanjis() {
        return quizKanjis;
    }

    public static void setQuizKanjis(List<Kanji> quizKanjis) {
        QuizActivity.quizKanjis = quizKanjis;
    }

    private void loadlearn(){
    }

    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
