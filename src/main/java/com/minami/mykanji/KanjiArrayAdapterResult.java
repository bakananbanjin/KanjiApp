package com.minami.mykanji;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class KanjiArrayAdapterResult extends ArrayAdapter<Kanji> {

    private Context context;
    private int layout;
    private List<Kanji> kanjis;
    private KanjiDAO kanjiDAO;
    boolean[] correct;

    public KanjiArrayAdapterResult(Context context, int layout, List<Kanji> kanjis, boolean[] correct){
        super(context, layout, kanjis);
        this.context = context;
        this.layout = layout;
        this.kanjis = kanjis;
        this.kanjiDAO = MainActivity.getKanjiDAO();
        this.correct = correct;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Kanji kanji = kanjis.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);
        }
        TextView tvKanji = convertView.findViewById(R.id.tvlearnRKanji);
        TextView tvonYomi = convertView.findViewById(R.id.tvlearnOn);
        TextView tvkunYomi = convertView.findViewById(R.id.kunYomi);
        TextView tvtranslation = convertView.findViewById(R.id.translation);

        tvKanji.setText(kanji.getKanji());
        tvonYomi.setText("on Yomi: " + kanji.getOnYomi());
        tvkunYomi.setText("kun Yomi: " + kanji.getKunYomi());
        tvtranslation.setText(kanji.getTranslation());
        if(position < correct.length && correct[position]){
            convertView.setBackgroundColor(Color.GREEN);
        } else {
            convertView.setBackgroundColor(Color.RED);
        }
        return convertView;
    }

}
