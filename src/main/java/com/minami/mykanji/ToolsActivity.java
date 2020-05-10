package com.minami.mykanji;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ToolsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private KanjiArrayAdapterSmall adapterSmall;
    private KanjiDAO kanjiDAO;
    private List<Kanji> kanjis;

    Button btnBack, btnInsertNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);


        kanjiDAO = MainActivity.getKanjiDAO();
        kanjis = kanjiDAO.getKanjis();
        adapterSmall = new KanjiArrayAdapterSmall(this, R.layout.kanji2, kanjis);
        TextView tv = findViewById(R.id.textView10);
        btnBack = findViewById(R.id.btnToolsBack);
        btnInsertNew = findViewById(R.id.btnToolInsertNew);

        btnInsertNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ToolsActivity.this, EditKanjiActivity.class));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ToolsActivity.this, MainActivity.class));
            }
        });

        GridView gv = (GridView) findViewById(R.id.gridview);
        gv.setAdapter(adapterSmall);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("APP ToolsActivity 39", "Clicked: " + position);
                Toast.makeText(ToolsActivity.this, "Please longclick for context menu", Toast.LENGTH_SHORT).show();
            }
        });

        PopupMenu popupMenu = new PopupMenu(this, gv);

        registerForContextMenu(gv);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("APP ToolsActivity 39", "Clicked: " + position);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.toolc_contextmenu, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Kanji kanji = (Kanji) adapterSmall.getItem(acmi.position);
            final int position = acmi.position;
            switch(item.getItemId()){
                case R.id.toolscontextEditKanji:
                    Intent intent = new Intent(ToolsActivity.this, EditKanjiActivity.class);
                    intent.putExtra("kanji", kanjis.get(position));
                    startActivity(intent);
                    return true;
                case R.id.toolscontextDeleteKanji:

                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Attention Database change!");
                    alertDialogBuilder.setMessage("Are you sure, You wanted to  delete " + kanjis.get(acmi.position).getKanji() + "?");
                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Kanji tempKanji = kanjiDAO.selectLastKanji();
                            int tempDeletedKanjiId = kanjis.get(position).getId();
                            int tempLastKanjiId = tempKanji.getId();

                            kanjiDAO.deleteById(tempDeletedKanjiId);

                            tempKanji.setId(tempDeletedKanjiId);
                            kanjiDAO.changeKanjiId(tempDeletedKanjiId, tempLastKanjiId);
                            Toast.makeText(ToolsActivity.this, "Kanji from Database deleted", Toast.LENGTH_SHORT).show();

                            update();
                            MainActivity.logKanjis();
                        }
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ToolsActivity.this, "Kanji not deleted", Toast.LENGTH_SHORT).show();
                            //finish();
                        }
                    });
                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return true;
                case R.id.toolscontextAddDraw:
                    Intent intent1 = new Intent(ToolsActivity.this, KanjiDraw.class);
                    intent1.putExtra("source", 3);
                    intent1.putExtra("kanji", kanjis.get(position));
                    startActivity(intent1);
                    finish();

                    return true;
                case R.id.toolscontextCheckDraw:
                    Intent intent3 = new Intent(ToolsActivity.this, DrawCheck.class);
                    intent3.putExtra("kanji", kanjis.get(position));
                    startActivity(intent3);
                    finish();

                    return true;
                default:
                    Log.d("APP ToolsActivity 39", "Clicked: " + acmi.position);
                    return super.onContextItemSelected(item);
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void update(){
        adapterSmall.notifyDataSetChanged();
        this.recreate();
    }
}
