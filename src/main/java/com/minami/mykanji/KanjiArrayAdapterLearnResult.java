package com.minami.mykanji;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class KanjiArrayAdapterLearnResult extends ArrayAdapter<Kanji> {
    private Context context;
    private int layout;
    private List<Kanji> kanjis;
    private LearnMap correct;

    public KanjiArrayAdapterLearnResult(Context context, int layout, List<Kanji> kanjis, LearnMap correct){
        super(context, layout, kanjis);
        this.context = context;
        this.layout = layout;
        this.kanjis = kanjis;
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
        TextView tvonYomi = convertView.findViewById(R.id.tvlearnROn);
        TextView tvkunYomi = convertView.findViewById(R.id.tvlearnRKun);
        TextView tvtranslation = convertView.findViewById(R.id.tvlearnRTrans);
        CheckBox[] cb = new CheckBox[3];

        cb[0] = convertView.findViewById(R.id.cbkanji31);
        cb[1] = convertView.findViewById(R.id.cbkanji32);
        cb[2] = convertView.findViewById(R.id.cbkanji33);

        tvKanji.setText(kanji.getKanji());
        tvonYomi.setText("on Yomi: " + kanji.getOnYomi());
        tvkunYomi.setText("kun Yomi: " + kanji.getKunYomi());
        tvtranslation.setText(kanji.getTranslation());

         boolean[] tempboolean =  correct.getValues(kanji);
         for(int i = 0; i < cb.length; i++){
             cb[i].setChecked(tempboolean[i]);
             if(tempboolean[i]){
                 cb[i].setBackgroundColor(Color.GREEN);
             } else {
                 cb[i].setBackgroundColor(Color.RED);
             }
         }
        return convertView;
    }
}
