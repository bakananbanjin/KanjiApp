package com.minami.mykanji;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class DrawCheck extends AppCompatActivity {

    private Kanji kanji;
    private String[] draws;
    private String[] drawsTrans;
    private DrawStringArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_check);


        kanji = (Kanji) getIntent().getSerializableExtra("kanji");
        draws = kanji.getDraw().split(",");

        adapter = new DrawStringArrayAdapter(this, R.layout.strokedraw, draws, kanji);
        ListView lv = findViewById(R.id.lvDrawCheck);
        lv.setAdapter(adapter);
    }
}
