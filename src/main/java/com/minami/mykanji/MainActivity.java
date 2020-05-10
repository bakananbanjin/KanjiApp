package com.minami.mykanji;

import android.content.Intent;
import android.icu.text.IDNA;
import android.support.annotation.NonNull;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnViewKanji;
    private Button btnQuiz;
    private Button btnTool;
    private Button testButton;
    private Button btnMainLearn;
    private Guideline gdl;
    private static KanjiDAO kanjiDAO;
    private static RadicalDAO radicalDAO;
    private static List<Kanji> allMainKanjis;

    private Thread t;
    private BufferedReader br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMainLearn = findViewById(R.id.btnMainLearn);
        btnMainLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, LernActivity.class);


                startActivity(intent);
            }
        });


        btnViewKanji = findViewById(R.id.btnViewKanji);
        btnViewKanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, KanjiListActivity.class);
                startActivity(intent);
            }
        });
        btnQuiz = findViewById(R.id.btnQuiz);
        btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KanjiQuizSetup.class);
                startActivity(intent);
            }
        });

        btnTool = findViewById(R.id.btnMainTools);
        btnTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ToolsActivity.class);
                startActivity(intent);
            }
        });
        testButton = findViewById(R.id.btnMainExit);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });


        kanjiDAO = KanjiDatabase.getInstance(this).getKanjiDAO();
        radicalDAO = KanjiDatabase.getInstance(this).getRadicalDAO();
        QuizActivity.allKanjis = kanjiDAO.getKanjis();
        if(QuizActivity.allKanjis != null && QuizActivity.allKanjis.size() != 0) {
            Log.d("APP MainActivity", "KanjiListSize:" + QuizActivity.allKanjis.size());
        } else {

            t = new Thread(){
                @Override
                public void run() {
                    kanjiDAO.deleteAll();
                    CSVreader.readFileKanji("KanjiCSV", getApplicationContext());
                    CSVreader.readFileRadical("RadicalCSV", getApplicationContext());
                    QuizActivity.allKanjis = kanjiDAO.getKanjis();
                }
            };
            t.start();
        }
    }

    public static KanjiDAO getKanjiDAO(){
        return kanjiDAO;
    }
    public static RadicalDAO getRadicalDAO(){
        return radicalDAO;
    }

    public void onBackPressed(){
        super.onBackPressed();
        this.finishAffinity();
        finish();
    }

    public void readFile(String file ){
        List<String>fileLines = new ArrayList<String>();
        try {
            String line;
            br = new BufferedReader(new InputStreamReader(getAssets().open(file), "UTF-8"));
            while((line = br.readLine()) != null){
                fileLines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < fileLines.size(); i++){
            Log.d("APP CSVReader", "readFile:" + fileLines.get(i) );
        }

        for(int i = 0; i < fileLines.size(); i++) {
            String[] tempString = fileLines.get(i).split(";");

            for(int j = 0; j < tempString.length; j++){
                Log.d("APP CSVreader 83", "String:" + tempString[j] + (j + 1));
            }
                Log.d("APP CSVreader 83", "parseID");
                int id = Integer.parseInt(tempString[0]);
                String kanji = tempString[1];
                String onYomi = tempString[2];
                String kunYomi = tempString[3];
                String translation = tempString[4];
                int gradeLvl = Integer.parseInt(tempString[5]);
                int memory = Integer.parseInt(tempString[6]);
                int strokeCount = Integer.parseInt(tempString[7]);
                boolean selected = false;
                if (tempString[8].equals("true") || tempString[8].equals("1")) {
                    selected = true;
                }
                Log.d("APP CSVreader 143", id + kanji + onYomi + kunYomi + translation + gradeLvl + memory+ strokeCount + selected);
                Kanji temp = new Kanji(id, kanji,onYomi, kunYomi, translation, gradeLvl, memory, strokeCount, selected);
                Log.d("APP CSVreader 145", temp.toString());
                kanjiDAO.insert(temp);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static void logKanjis(){
        List<Kanji> tempkanjis = kanjiDAO.getKanjis();
       /* for(int i = 0; i < tempkanjis.size(); i++){
            Log.d("APP KanjiList 187", tempkanjis.get(i).toString());
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainInfo:
                startActivity(new Intent(MainActivity.this, InfoActivity.class));
                break;
        }
        return true;
    }

    public static int  randomWithRange(int min, int max) {
        int range = Math.abs(max - min) + 1;
        return (int)(Math.random() * range) + (min <= max ? min : max);
    }

    public static int randomMax(int max)
    {
        //includes max value
        return (int) (Math.random() * (max + 1));
    }
}
