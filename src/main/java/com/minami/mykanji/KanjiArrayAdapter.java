package com.minami.mykanji;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class KanjiArrayAdapter extends ArrayAdapter<Kanji> {

    private Context context;
    private int layout;
    private List<Kanji> kanjis;
    private KanjiDAO kanjiDAO;

    public KanjiArrayAdapter(Context context, int layout, List<Kanji> kanjis){
        super(context, layout, kanjis);
        this.context = context;
        this.layout = layout;
        this.kanjis = kanjis;
        this.kanjiDAO = MainActivity.getKanjiDAO();
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

        if(kanji.isSelected()){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }

        return convertView;
    }
}
