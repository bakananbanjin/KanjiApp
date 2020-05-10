package com.minami.mykanji;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class KanjiListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, SwipeRefreshLayout.OnRefreshListener {


    //private KanjiDAO kanjiDAO;
    private KanjiArrayAdapter adapter;
    private KanjiArrayAdapterSmall smalladapter;
    static List<Kanji> allKanjis;
    static int selected = -1;
    static int selectedBy = 0;
    KanjiDAO kanjiDAO;
    //SwipeRefreshLayout mSwipe;
    static int swipeInt = -10;
    Thread t;
    Handler h;
    private static boolean isbigIcons = true;
    private static boolean myListselectmode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_list);

        kanjiDAO = MainActivity.getKanjiDAO();

       // mSwipe = (SwipeRefreshLayout) findViewById(R.id.kanjiListSwipe);
       // mSwipe.setOnRefreshListener(this);
        h = new Handler();

        if(allKanjis == null){
            Log.d("APP allKanjis", "befor creation allKanjis = null");
            allKanjis = kanjiDAO.getKanjis();
        }

        //getActionBar().setDisplayHomeAsUpEnabled(true);

       if(kanjiDAO.getKanjis().isEmpty()){

           allKanjis = kanjiDAO.getKanjis();


        }

        ListView lv = findViewById(R.id.listView);
        GridView gv = findViewById(R.id.listgridview);
        if(isbigIcons){
            adapter = new KanjiArrayAdapter(this, R.layout.kanji, allKanjis);
            gv.setVisibility(View.GONE);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);
            lv.setOnItemLongClickListener(this);
        } else {
            smalladapter = new KanjiArrayAdapterSmall(this, R.layout.kanji2, allKanjis);
            lv.setVisibility(View.GONE);
            gv.setAdapter(smalladapter);
            gv.setOnItemClickListener(this);
            gv.setOnItemLongClickListener(this);
        }
        setTitle(getResources().getString(R.string.stringKanjiList));



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Kanji kanji;
        if(isbigIcons) {
             kanji = (Kanji) adapter.getItem(position);
        } else {
            kanji = (Kanji) smalladapter.getItem(position);
        }
        if(myListselectmode){
            Kanji temp = allKanjis.get(position);
            if(allKanjis.get(position).isSelected()) {
                temp.setSelected(false);
                kanjiDAO.update(allKanjis.get(position));
                Toast.makeText(KanjiListActivity.this, temp.getKanji() + " deselected from MyList", Toast.LENGTH_SHORT).show();

            } else {
                allKanjis.get(position).setSelected(true);
                kanjiDAO.update(allKanjis.get(position));
                Toast.makeText(KanjiListActivity.this, temp.getKanji() + " selected to MyList", Toast.LENGTH_SHORT).show();

            }
            if(isbigIcons) {
                adapter.notifyDataSetChanged();
            } else {
                smalladapter.notifyDataSetChanged();
            }
            KanjiListActivity.this.recreate();

        } else {

            Intent intent = new Intent(this, KanjiDraw.class);
            intent.putExtra("kanji", kanji);
            intent.putExtra("source", 1);
            startActivity(intent);
        }

        //Toast.makeText(KanjiListActivity.this, "Short", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Kanji temp = allKanjis.get(position);
        if(allKanjis.get(position).isSelected()) {
            temp.setSelected(false);
            kanjiDAO.update(allKanjis.get(position));
            Toast.makeText(KanjiListActivity.this, temp.getKanji() + " deselected from MyList", Toast.LENGTH_SHORT).show();

        } else {
            allKanjis.get(position).setSelected(true);
            kanjiDAO.update(allKanjis.get(position));
            Toast.makeText(KanjiListActivity.this, temp.getKanji() + " selected to MyList", Toast.LENGTH_SHORT).show();

        }
        if(isbigIcons) {
            adapter.notifyDataSetChanged();
        } else {
            smalladapter.notifyDataSetChanged();
        }
        KanjiListActivity.this.recreate();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kanjilist_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.kanjiListSelectAll:
                selected = -1;
                return switchSelected();
            case R.id.schoolLvl1:
                swipeInt = R.id.schoolLvl1;
                selected = 11;
                return switchSelected();
            case R.id.schoolLvl2:
                swipeInt = R.id.schoolLvl2;
                selected = 12;
                return switchSelected();
            case R.id.schoolLvl3:
            case R.id.schoolLvl4:
            case R.id.schoolLvl5:
                /*swipeInt = R.id.schoolLvl3;
                selected = 13;
                return switchSelected();

                swipeInt = R.id.schoolLvl4;
                selected = 14;
                return switchSelected();

                swipeInt = R.id.schoolLvl5;
                selected = 15;
                return switchSelected();*/
                Toast.makeText(this, "Kanjis Schoollvl 3-5 will be added soon", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.skillLvl1:
                swipeInt = R.id.skillLvl1;
                selected = 19;
                return switchSelected();

            case R.id.skillLvl2:
                swipeInt = R.id.skillLvl2;
                selected = 22;
                return switchSelected();

            case R.id.skillLvl3:
                swipeInt = R.id.skillLvl3;
                selected = 25;
                return switchSelected();

            case R.id.skillLvl4:
                swipeInt = R.id.skillLvl4;
                selected = 27;
                return switchSelected();

            case R.id.skillLvl5:
                swipeInt = R.id.skillLvl5;
                selected = 28;
                return switchSelected();

            case R.id.selectMyList:
                swipeInt = R.id.selectMyList;
                selected = 30;
                return switchSelected();

            case R.id.myListselectAllKanjis:
                loadThread(R.id.myListselectAllKanjis);
                return true;

            case R.id.myListselectMode:
                myListselectmode = !myListselectmode;
                if(myListselectmode){
                    Toast.makeText(this, "Entered selectMode", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Left selectMode", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.myListReset:
                loadThread(R.id.myListReset);
                return true;

            case R.id.myListSortByNumber:
                selectedBy = 0;
                switchSelected();
                return true;

            case R.id.myListSortByStrokecount:
                selectedBy = 1;
                switchSelected();
                return true;

            case R.id.myListSortByRecollection:
                selectedBy = 2;
                switchSelected();
                return true;

            case R.id.reset:
                resetkanji();
                return true;

            // if no super is called
            case R.id.kanjiListIcon:
                isbigIcons = !isbigIcons;
                this.recreate();
                return true;
            case R.id.homeAsUp:
                //NavUtils.navigateUpFromSameTask(this);
                //return true;
            default:
                swipeInt = -10;
                allKanjis = kanjiDAO.getKanjis();
                if(isbigIcons) {
                    adapter.notifyDataSetChanged();
                } else {
                    smalladapter.notifyDataSetChanged();
                }
                return super.onOptionsItemSelected(item);
        }
    }

    public static List<Kanji> getAllKanjis() {
        return allKanjis;
    }

    public static void setAllKanjis(List<Kanji> allKanjis) {
        KanjiListActivity.allKanjis = allKanjis;
    }

    private boolean switchSelected(){
        switch (selected){
            case -1:
                switch (selectedBy) {

                    case 1:
                        allKanjis = kanjiDAO.getKanjiSortedByStrokeCount();
                        KanjiListActivity.this.recreate();
                        break;
                    case 2:
                        allKanjis = kanjiDAO.getKanjiSortedByMemory();
                        KanjiListActivity.this.recreate();
                        break;

                    case 0:
                    default:
                        allKanjis = kanjiDAO.getKanjiSortedByID();
                        KanjiListActivity.this.recreate();
                        break;
                }
                if(isbigIcons) {
                    adapter.notifyDataSetChanged();
                } else {
                    smalladapter.notifyDataSetChanged();
                }
                return true;
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                switch (selectedBy) {
                    case 2:
                        allKanjis = kanjiDAO.getKanjiBySchoolLvlSortedBYMemory(selected-10);
                        if(isbigIcons) {
                            adapter.notifyDataSetChanged();
                        } else {
                            smalladapter.notifyDataSetChanged();
                        }
                        KanjiListActivity.this.recreate();
                        return true;

                    case 1:
                        allKanjis = kanjiDAO.getKanjiBySchoolLvlSortedByStrokeCount(selected-10);
                        if(isbigIcons) {
                            adapter.notifyDataSetChanged();
                        } else {
                            smalladapter.notifyDataSetChanged();
                        }
                        KanjiListActivity.this.recreate();
                        return true;
                    case 0:
                    default:
                        allKanjis = kanjiDAO.getKanjiBySchoolLvlSortedByID(selected-10);
                        if(isbigIcons) {
                            adapter.notifyDataSetChanged();
                        } else {
                            smalladapter.notifyDataSetChanged();
                        }
                        KanjiListActivity.this.recreate();
                        return true;
                }

            case 19:
            case 22:
            case 25:
            case 28:
                switch (selectedBy) {
                    case 2:
                        allKanjis = kanjiDAO.getKanjiBetweenLvlSortedByMemory(selected-20, selected -18);
                        if(isbigIcons) {
                            adapter.notifyDataSetChanged();
                        } else {
                            smalladapter.notifyDataSetChanged();
                        }
                        KanjiListActivity.this.recreate();
                        return true;

                    case 1:
                        allKanjis = kanjiDAO.getKanjiBetweenLvlSortedByStrokeCount(selected-20, selected -18);
                        if(isbigIcons) {
                            adapter.notifyDataSetChanged();
                        } else {
                            smalladapter.notifyDataSetChanged();
                        }
                        KanjiListActivity.this.recreate();
                        return true;
                    case 0:
                    default:
                        allKanjis = kanjiDAO.getKanjiBetweenLvlSortedByID(selected-20, selected -18);
                        if(isbigIcons) {
                            adapter.notifyDataSetChanged();
                        } else {
                            smalladapter.notifyDataSetChanged();
                        }
                        KanjiListActivity.this.recreate();
                        return true;
                }
            case 29:
                switch (selectedBy) {
                    case 2:
                        allKanjis = kanjiDAO.getKanjiBetweenLvlSortedByMemory(11, 21);
                        if(isbigIcons) {
                            adapter.notifyDataSetChanged();
                        } else {
                            smalladapter.notifyDataSetChanged();
                        }
                        KanjiListActivity.this.recreate();
                        return true;

                    case 1:
                        allKanjis = kanjiDAO.getKanjiBetweenLvlSortedByStrokeCount(11, 21);
                        if(isbigIcons) {
                            adapter.notifyDataSetChanged();
                        } else {
                            smalladapter.notifyDataSetChanged();
                        }
                        KanjiListActivity.this.recreate();
                        return true;
                    case 0:
                    default:
                        allKanjis = kanjiDAO.getKanjiBetweenLvlSortedByID(11, 21);
                        if(isbigIcons) {
                            adapter.notifyDataSetChanged();
                        } else {
                            smalladapter.notifyDataSetChanged();
                        }
                        KanjiListActivity.this.recreate();
                        return true;
                }
            case 30:
                switch (selectedBy) {
                    case 2:
                        allKanjis = kanjiDAO.getKanjiSelectedSortedByMemory();
                        if(isbigIcons) {
                            adapter.notifyDataSetChanged();
                        } else {
                            smalladapter.notifyDataSetChanged();
                        }
                        KanjiListActivity.this.recreate();
                        return true;

                    case 1:
                        allKanjis = kanjiDAO.getKanjiSelectedSortedByStrokeCount();
                        if(isbigIcons) {
                            adapter.notifyDataSetChanged();
                        } else {
                            smalladapter.notifyDataSetChanged();
                        }
                        KanjiListActivity.this.recreate();
                        return true;
                    case 0:
                    default:
                        allKanjis = kanjiDAO.getKanjiSelectedSortedByID();
                        if(isbigIcons) {
                            adapter.notifyDataSetChanged();
                        } else {
                            smalladapter.notifyDataSetChanged();
                        }
                        KanjiListActivity.this.recreate();
                        return true;
                }

            default:
                allKanjis = kanjiDAO.getKanjiSortedByID();
                if(isbigIcons) {
                    adapter.notifyDataSetChanged();
                } else {
                    smalladapter.notifyDataSetChanged();
                }
                KanjiListActivity.this.recreate();
                return false;
        }
    }

   @Override
    public void onRefresh() {
        //switchSelected();
        //mSwipe.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(t !=null) {
            t.interrupt();
        }
    }

    private void loadThread(int source){
        switch(source) {
            case R.id.myListselectAllKanjis:
                t = new Thread() {
                    @Override
                    public void run() {
                        KanjiListActivity.allKanjis = kanjiDAO.getKanjis();
                        for (int i = 0; i < allKanjis.size(); i++) {
                            allKanjis.get(i).setSelected(true);
                            kanjiDAO.update(allKanjis.get(i));
                        }
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                if(isbigIcons) {
                                    adapter.notifyDataSetChanged();
                                } else {
                                    smalladapter.notifyDataSetChanged();
                                }
                                KanjiListActivity.this.recreate();
                            }
                        });
                    }
                };
                break;
            case R.id.myListReset:
                t = new Thread() {
                    @Override
                    public void run() {
                        KanjiListActivity.allKanjis = kanjiDAO.getKanjis();
                        for (int i = 0; i < allKanjis.size(); i++) {
                            if(allKanjis.get(i).isSelected()){
                                allKanjis.get(i).setSelected(false);
                                kanjiDAO.update(allKanjis.get(i));
                            }
                        }
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                if(isbigIcons) {
                                    adapter.notifyDataSetChanged();
                                } else {
                                    smalladapter.notifyDataSetChanged();
                                }
                                KanjiListActivity.this.recreate();
                            }
                        });
                    }
                };
                break;
        }
        t.start();
    }

    private void resetkanji(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Attention Database change!");
        alertDialogBuilder.setMessage("Are you sure, You wanted to reset Kanjis?");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                kanjiDAO.deleteAll();
                CSVreader.readFileKanji("KanjiCSV", getApplicationContext());
                CSVreader.readFileRadical("RadicalCSV", getApplicationContext());
                //MainActivity.logKanjis();
                Toast.makeText(KanjiListActivity.this, "Kanji reset!", Toast.LENGTH_SHORT).show();
                allKanjis = kanjiDAO.getKanjis();
                if(isbigIcons) {
                    adapter.notifyDataSetChanged();
                } else {
                    smalladapter.notifyDataSetChanged();
                }

                KanjiListActivity.this.recreate();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}

