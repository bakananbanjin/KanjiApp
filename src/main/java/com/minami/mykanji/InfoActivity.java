package com.minami.mykanji;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InfoActivity extends AppCompatActivity {
    private TextView tvLink, tvMail;
    private Button btnBack, btnTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        btnBack = findViewById(R.id.btnInfoBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InfoActivity.this, MainActivity.class));
            }
        });

        btnTemp = findViewById(R.id.btnInfoExport);
        btnTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CSVreader.writeFileKanji("Kanji.csv", getApplicationContext(), MainActivity.getKanjiDAO().getKanjis());
            }
        });

        tvLink = findViewById(R.id.tvInfoLink);
        setTitle(getResources().getString(R.string.stringInfoTitle));

    }
}
