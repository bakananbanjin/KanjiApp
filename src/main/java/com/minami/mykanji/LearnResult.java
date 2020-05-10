package com.minami.mykanji;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class LearnResult extends AppCompatActivity {

    Button btnBack;
    private KanjiArrayAdapterLearnResult adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_result);

        btnBack = findViewById(R.id.btnLearnRBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LearnResult.this, MainActivity.class));
                LernActivity.finishLearn();
                finish();
            }
        });

        List<Kanji> tempKanjis = LernActivity.getKanjis();
        LearnMap tempLearnMap = LernActivity.getLearnMap();
        for(int i = 0; i < tempKanjis.size(); i++ ){
            Kanji kanji = tempKanjis.get(i);
           boolean[] tempboolean = tempLearnMap.getValues(tempKanjis.get(i));
            if(tempboolean[0] && tempboolean [1] && tempboolean[2]){
                kanji.setMemory(kanji.getMemory() + 3);
            } else if ((tempboolean[0] && tempboolean [1]) || (tempboolean[0] && tempboolean [2])  || (tempboolean[1] && tempboolean [2]) ){
                kanji.setMemory(kanji.getMemory() + 1);
            } else if(tempboolean[0] || tempboolean [1] || tempboolean [1]){
                // no change in Memory
            } else {
                if(kanji.getMemory() >= 0) {
                    kanji.setMemory(-1);
                }
            }
            kanji.setSelected(true);

            MainActivity.getKanjiDAO().insert(kanji);
        }
        adapter = new KanjiArrayAdapterLearnResult(this, R.layout.kanji3, tempKanjis, LernActivity.getLearnMap());
        ListView lv = findViewById(R.id.lvLearnResult);
        lv.setAdapter(adapter);
        setTitle(getResources().getString(R.string.stringQuizResult));
    }

    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        LernActivity.finishLearn();
        finish();
    }
}
