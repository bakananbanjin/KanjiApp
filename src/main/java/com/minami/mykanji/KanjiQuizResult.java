package com.minami.mykanji;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class KanjiQuizResult extends AppCompatActivity {

    private final static int FROMKQUIZ = 1;
    private final static int FROMKANJIDRAW = 2;
    private final static int FROMLEARN = 5;

    private List<Kanji> resultKanji;
    private KanjiArrayAdapterResult adapter;
    private boolean[] correct;
    private int source;

    TextView resultTextView1, resultTextView2;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_quiz_result);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            switch(extras.getInt("source")) {
                case FROMKANJIDRAW:
                case FROMKQUIZ:
                    source = extras.getInt("source");
                    correct = extras.getBooleanArray("correct");
                    break;
                case FROMLEARN:

                    break;
            }
        }

        resultTextView1 = findViewById(R.id.resultTextView2);
        resultTextView2 = findViewById(R.id.resultTextView3);

        btnBack = findViewById(R.id.resultButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KanjiQuizResult.this, MainActivity.class));
            }
        });
        switch(source){
            case FROMKQUIZ:
                resultKanji = QuizActivity.getQuizKanjis();
                break;
            case FROMKANJIDRAW:
                resultKanji = KanjiDraw.getQuizKanjis();
                break;

        }

        Log.d("APP QuizResult 28", "KanjiListSize:" + resultKanji.size());
        int tempInt = 0;
        for(int i = 0; i < correct.length; i++){
            if(correct[i]){
                tempInt++;
            }
        }
        resultTextView1.setText("Kanjis in test: " + resultKanji.size() + "\nKanjis correct: " + tempInt );
        if(tempInt != 0 && (float)((float)tempInt/(float)resultKanji.size()) > 0.5f){
            resultTextView2.setText("Good Job");
        } else {
            resultTextView2.setText("Keep studing!　頑張れ！");
        }

        adapter = new KanjiArrayAdapterResult(this, R.layout.kanji, resultKanji, correct);
        ListView lv = findViewById(R.id.resultListView);
        lv.setAdapter(adapter);
        setTitle(getResources().getString(R.string.stringQuizResult));
    }
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
