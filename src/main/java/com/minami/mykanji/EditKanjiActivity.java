package com.minami.mykanji;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditKanjiActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerMemory;
    Kanji editKanji;
    Bundle extras;
    EditText etKanji, etOnYomi, etKunyomi, etTranslation, etStrokeCount, etSchoolgrade;
    CheckBox cbSelected;
    Button btnInsert, btnBack;
    int memory;
    int source = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kanji);



        btnInsert = findViewById(R.id.btnEditInsert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(insertKanjiData() && loadtestkanji()){
                    if(1 == source){
                            MainActivity.getKanjiDAO().update(editKanji);
                            Toast.makeText(EditKanjiActivity.this, "updated Kanji in Database", Toast.LENGTH_SHORT).show();
                    } else if (MainActivity.getKanjiDAO().selectKanjiByKanji(editKanji.getKanji()) == null){
                        editKanji.setId(MainActivity.getKanjiDAO().selectLastKanji().getId() + 1);
                        MainActivity.getKanjiDAO().insert(editKanji);
                        Toast.makeText(EditKanjiActivity.this, "insert new Kanji to Database", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditKanjiActivity.this, "error Kanji not insert to Database", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBack = findViewById(R.id.btnEditBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditKanjiActivity.this, ToolsActivity.class));
                finish();
            }
        });

        cbSelected = findViewById(R.id.cbEditSelected);
        etKanji = findViewById(R.id.editKanjikanji);
        etOnYomi = findViewById(R.id.eteditOn);
        etKunyomi = findViewById(R.id.etEditKun);
        etTranslation = findViewById(R.id.etEditTrans);
        etStrokeCount = findViewById(R.id.etEditStroke);
        etSchoolgrade = findViewById(R.id.etEditSchoolgrade);

        extras = getIntent().getExtras();
        if(extras != null){
            //edit Kanji
            editKanji = (Kanji) extras.getSerializable("kanji");
            source = 1;
            etKanji.setEnabled(false);
        } else {
            //new Kanji
            editKanji = new Kanji();
            source = 0;
        }

        memory = editKanji.getMemory();

        etKanji.setText(editKanji.getKanji());
        etOnYomi.setText(editKanji.getOnYomi());
        etKunyomi.setText(editKanji.getKunYomi());
        etTranslation.setText(editKanji.getTranslation());
        cbSelected.setChecked(editKanji.isSelected());

        String temp =  editKanji.getStrokeCount() + "";
        etStrokeCount.setText(temp);
        temp = editKanji.getLvl() + "";
        etSchoolgrade.setText(temp);



        spinnerMemory = (Spinner) findViewById(R.id.spinnerEditKanji);
        ArrayAdapter<CharSequence> charAdapter = ArrayAdapter.createFromResource(this, R.array.editKanjiMemory, android.R.layout.simple_spinner_item);
        charAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMemory.setAdapter(charAdapter);

        int intTemp = editKanji.getMemory();
        if(intTemp < 2){
            spinnerMemory.setSelection(0);
        } else if ( intTemp < 5 && intTemp >= 2){
            spinnerMemory.setSelection(1);
        } else if(intTemp < 8 && intTemp >= 5) {
            spinnerMemory.setSelection(2);
        } else if(intTemp < 10 && intTemp >= 8){
            spinnerMemory.setSelection(3);
        } else {
            spinnerMemory.setSelection(4);
        }
        setTitle(getResources().getString(R.string.stringInsertEditKanji));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //parent.getItemAtPosition(position);
        switch(position){
            case 0:
                memory = -1;
                break;
            case 1:
                memory = 2;
                break;
            case 2:
                memory = 5;
                break;
            case 3:
                memory = 8;
                break;
            default:
                memory =11;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean loadtestkanji(){
        if(editKanji.getKanji().equals("")){
            return false;
        }
        if(editKanji.getOnYomi().equals("")){
            return false;
        }
        if(editKanji.getKunYomi().equals(""))        {
            return false;
        }
        if(editKanji.getTranslation().equals("")){
            return false;
        }

        return true;
    }

    private boolean insertKanjiData(){
        editKanji.setKanji(etKanji.getText().toString());
        editKanji.setOnYomi(etOnYomi.getText().toString());
        editKanji.setKunYomi(etKunyomi.getText().toString());
        editKanji.setTranslation(etTranslation.getText().toString());
        editKanji.setSelected(cbSelected.isSelected());
        editKanji.setLvl(Integer.parseInt(etSchoolgrade.getText().toString()));
        editKanji.setStrokeCount(Integer.parseInt(etStrokeCount.getText().toString()));
        editKanji.setMemory(memory);

        return true;
    }
}
