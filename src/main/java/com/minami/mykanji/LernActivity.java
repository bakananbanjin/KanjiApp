package com.minami.mykanji;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class LernActivity extends AppCompatActivity {
    private static List<Kanji> kanjis;
    private Button btnNormalTest, btnRandomTest, btnReview, btnBack;
    private static LearnMap learnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lern);
        btnNormalTest = findViewById(R.id.btnlearntemp);
        btnNormalTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kanjis = MainActivity.getKanjiDAO().getKanjisSortedByIDLimit(false, 5);
                learnMap = new LearnMap(kanjis);
                if(kanjis != null && kanjis.size() == 5) {
                    Intent intent = new Intent(LernActivity.this, KanjiDraw.class);
                    intent.putExtra("source", 4);
                    startActivity(intent);
                } else {
                    if (kanjis == null){
                        Toast.makeText(LernActivity.this, "No Kanjis found maybe all Kanjis already in MyList", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LernActivity.this, "To few Kanjis found maybe all Kanjis already in MyList", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnRandomTest = findViewById(R.id.btnLearnRandom);
        btnRandomTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kanjis = MainActivity.getKanjiDAO().getKanjisRandomLimit(false, 5);
                learnMap = new LearnMap(kanjis);
                if(kanjis != null && kanjis.size() == 5) {
                    Intent intent = new Intent(LernActivity.this, KanjiDraw.class);
                    intent.putExtra("source", 4);
                    startActivity(intent);
                } else {
                    if (kanjis == null){
                        Toast.makeText(LernActivity.this, "No Kanjis found maybe all Kanjis already in MyList", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LernActivity.this, "To few Kanjis found maybe all Kanjis already in MyList", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnReview = findViewById(R.id.btnLearnReview);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kanjis = MainActivity.getKanjiDAO().getKanjisRandomLimit(true, 5);
                learnMap = new LearnMap(kanjis);
                if(kanjis != null && kanjis.size() == 5) {
                    Intent intent = new Intent(LernActivity.this, MatchKanjiActivity.class);
                    intent.putExtra("source", 0);
                    startActivity(intent);
                } else {
                    if (kanjis == null){
                        Toast.makeText(LernActivity.this, "No Kanjis found maybe no Kanjis in MyList", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LernActivity.this, "To few Kanjis found maybe no Kanjis in MyList, start Normal or Random", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnBack = findViewById(R.id.btnLearnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LernActivity.this, MainActivity.class));
                finish();
            }
        });

        setTitle(getResources().getString(R.string.stringLearn));

    }

    public static List<Kanji> getKanjis() {
        return kanjis;
    }

    public static LearnMap getLearnMap() {
        return learnMap;
    }

    public static void finishLearn(){
        kanjis = null;
    }
}
