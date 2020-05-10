package com.minami.mykanji;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class KanjiQuizSetup extends AppCompatActivity implements View.OnClickListener {
    Button  btnGuessKanji, btnGuessTranslation, btnDrawKanji, btnMatchKanji;
    RadioGroup rgKanjiappear, rgKanjiSelect, rgKanjicount, rgInner, rgTranslationReading;
    RadioButton rbRandom, rbWRandom, rbMyList, rbGrade1, rbGrade2, rbGrade3, rbGrade4, rbGrade5, rbx10, rbx20, rbx50, rbx100, rbAll;

    private int kanjiCount;
    private boolean isChecking = true;
    private int mCheckedId = R.id.rbAll;
    private int mCheckedCountid = R.id.rbx10;
    private int kanjiQuizCount = 10;
    private boolean kanjiQuizrandom = true;
    private int kanjiQuizWhich = 10;
    private int translationReading = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_quiz_setup);

        kanjiQuizrandom = true;
        kanjiQuizWhich = 10;
        kanjiQuizCount = 10;


        rgKanjiappear = findViewById(R.id.rgHowAppear);
        rgKanjiSelect = findViewById(R.id.rgWhichAppear);
        rgKanjicount = findViewById(R.id.rgManyAppear);
        rgInner = findViewById(R.id.rgInner);
        rgTranslationReading = findViewById(R.id.rgquizSetupTranslationReading);

        rgKanjiSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId != -1 && isChecking){
                    isChecking = false;
                    rgInner.clearCheck();
                    mCheckedId = checkedId;
                }
                isChecking = true;
                Log.d("APP KanjiQuizSetup 63", "kanjiQuizWhich = " + kanjiQuizWhich);

            }
        });
        rgInner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId != -1 && isChecking){
                    isChecking = false;
                    rgKanjiSelect.clearCheck();
                    mCheckedId = checkedId;
                }
                isChecking = true;
                Log.d("APP KanjiQuizSetup 63", "kanjiQuizWhich = " + kanjiQuizWhich + " McheckdId " + mCheckedId );
            }
        });
        rgKanjiappear.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });


        rbRandom = findViewById(R.id.rbRandom);
        rbWRandom = findViewById(R.id.rbWRandom);

        rbMyList = findViewById(R.id.rbMyList);
        rbGrade1 = findViewById(R.id.rbGrade1);
        rbGrade2 = findViewById(R.id.rbGrade2);
        rbGrade3 = findViewById(R.id.rbGrade3);
        rbGrade4 = findViewById(R.id.rbGrade4);
        rbGrade5 = findViewById(R.id.rbGrade5);
        rbAll = findViewById(R.id.rbAll);

        rbx10 = findViewById(R.id.rbx10);
        rbx20 = findViewById(R.id.rbx20);
        rbx50 = findViewById(R.id.rbx50);
        rbx100 = findViewById(R.id.rbx100);

        btnMatchKanji = findViewById(R.id.btnQuizSelectMatch);
        btnMatchKanji.setOnClickListener(this);

        btnGuessTranslation = findViewById(R.id.btnQuizSelectGuessTranslation);
        btnGuessTranslation.setOnClickListener(this);

        btnDrawKanji = findViewById(R.id.btnQuizSelectDrawKanji);
        btnDrawKanji.setOnClickListener(this);

        btnGuessKanji = findViewById(R.id.btnQuizSelectGuessKanji);
        btnGuessKanji.setOnClickListener(this);
        if(QuizActivity.allKanjis != null) {
            Log.d("APP MainActivity", "KanjiListSize:" + QuizActivity.allKanjis.size());
        }

        setTitle(getResources().getString(R.string.stringQuizSetup));
    }

    @Override
    public void onClick(View v) {
        Log.d("APP KanjiQuizSetup 108", "kanjiQuizWhich = " + kanjiQuizWhich + " checkid " + mCheckedId);

        if(rgKanjiappear.getCheckedRadioButtonId() == R.id.rbRandom){
            kanjiQuizrandom = true;
            Log.d("APP KanjiQuizSetup 109", "KanjiQuizRandom = true");
        } else {
            kanjiQuizrandom = false;
        }
        if(rgTranslationReading.getCheckedRadioButtonId() == R.id.rbByTranslation){
            translationReading = 0;
            Log.d("APP KanjiQuizSetup 109", "KanjiQuizRandom = true");
        } else {
            translationReading = 1;
        }
        switch (mCheckedId){
            case R.id.rbMyList:
                kanjiQuizWhich = 0;
                Log.d("APP KanjiQuizSetup 111", "kanjiQuizWhich = 0");
                break;
            case R.id.rbGrade1:
                kanjiQuizWhich = 1;
                Log.d("APP KanjiQuizSetup 115", "kanjiQuizWhich = 1");
                break;
            case R.id.rbGrade2:
                kanjiQuizWhich = 2;
                Log.d("APP KanjiQuizSetup 119", "kanjiQuizWhich = 2");
                break;
            case R.id.rbGrade3:
                kanjiQuizWhich = 3;
                Log.d("APP KanjiQuizSetup 123", "kanjiQuizWhich = 3");
                break;
            case R.id.rbGrade4:
                kanjiQuizWhich = 4;
                Log.d("APP KanjiQuizSetup 127", "kanjiQuizWhich = 4");
                break;
            case R.id.rbGrade5:
                kanjiQuizWhich = 5;
                Log.d("APP KanjiQuizSetup 131", "kanjiQuizWhich = 5");
                break;
            case R.id.rbAll:
                kanjiQuizWhich = 10;
                Log.d("APP KanjiQuizSetup135", "kanjiQuizWhich = All");
            default:
                kanjiQuizWhich = 10;
                Log.d("APP KanjiQuizSetup135", "kanjiQuizWhich = default");
                break;
        }
        mCheckedCountid = rgKanjicount.getCheckedRadioButtonId();
        switch (mCheckedCountid){
            case R.id.rbx10:
                kanjiQuizCount = 10;
                break;
            case R.id.rbx20:
                kanjiQuizCount = 20;
                break;
            case R.id.rbx50:
                kanjiQuizCount = 50;
                break;
            case R.id.rbx100:
                kanjiQuizCount = 100;
                break;
            default:
                kanjiQuizCount = 10;
                break;
        }
        Log.d("APP KanjiQuizSetup 160", "KanjiCount: " + kanjiQuizCount + " KanjiWhich: " + kanjiQuizWhich + " Kanjirandom: " + kanjiQuizrandom);

        switch (v.getId()){
            case R.id.btnQuizSelectDrawKanji:
                Log.d("APP KanjiQuizSetup", "DrawKanji");
                Intent intent = new Intent(this, KanjiDraw.class);
                intent.putExtra("kanjiCount", kanjiQuizCount);
                intent.putExtra("kanjiWhich", kanjiQuizWhich);
                intent.putExtra("random", kanjiQuizrandom);
                intent.putExtra("source", 2);
                intent.putExtra("translationReading", translationReading);
                if(intent.resolveActivity(getPackageManager())!= null){
                    Log.d("APP KanjiQuizSetup 171", "DrawKanji start Activity");
                    startActivity(intent);

                }
                return;
            case R.id.btnQuizSelectGuessKanji:
                Intent intent2 = new Intent(this, QuizActivity.class);
                intent2.putExtra("kanjiCount", kanjiQuizCount);
                intent2.putExtra("kanjiWhich", kanjiQuizWhich);
                intent2.putExtra("random", kanjiQuizrandom);
                intent2.putExtra("source", 4);
                intent2.putExtra("translationReading", translationReading);
                startActivity(intent2);

                Log.d("APP KanjiQuizSetup", "GuessKanji");
                return;
            case R.id.btnQuizSelectGuessTranslation:
                Intent intent1 = new Intent(this, QuizActivity.class);
                intent1.putExtra("kanjiCount", kanjiQuizCount);
                intent1.putExtra("kanjiWhich", kanjiQuizWhich);
                intent1.putExtra("random", kanjiQuizrandom);
                intent1.putExtra("source", 3);
                intent1.putExtra("translationReading", translationReading);
                startActivity(intent1);
                Log.d("APP KanjiQuizSetup 188", "GuessTranslation");
                return;
            case R.id.btnQuizSelectMatch:
                Intent intent3 = new Intent(this, MatchKanjiActivity.class);
                intent3.putExtra("kanjiCount", kanjiQuizCount);
                intent3.putExtra("kanjiWhich", kanjiQuizWhich);
                intent3.putExtra("random", kanjiQuizrandom);
                intent3.putExtra("source", 2);
                intent3.putExtra("translationReading", translationReading);
                startActivity(intent3);
                break;
            default:
                    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("APP ActivityResult", "KanjiQuizSetup: " + resultCode);
    }
}
