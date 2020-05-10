package com.minami.mykanji;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DrawStringArrayAdapter extends ArrayAdapter {

    private Context context;
    private int layout;
    Kanji kanji;
    String[] strokes;

    public DrawStringArrayAdapter(Context context, int layout, String[] strokes, Kanji kanji){
        super(context, layout, strokes);
        this.context = context;
        this.layout = layout;
        this.strokes = strokes;
        this.kanji = kanji;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String stroke = strokes[position];
        String strokeTrans = KanjiDraw.translateStroke(stroke);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);
        }

        TextView tvStrokeCode = convertView.findViewById(R.id.tvStrokeCode);
        TextView tvStrokeTrans = convertView.findViewById(R.id.tvStrokeDraw);
        Button btnDeleteDdrae = convertView.findViewById(R.id.btnStrokeDraw);
        btnDeleteDdrae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAndDelete(stroke);
                Toast.makeText(context, "Draw deleted", Toast.LENGTH_SHORT).show();
            }
        });

        tvStrokeCode.setText(stroke);
        tvStrokeTrans.setText(strokeTrans);

        return convertView;
    }

    public void findAndDelete(String stroke){
        for(int i = 0; i < strokes.length; i++){
            if(strokes[i].equals(stroke)){
                strokes[i] = "";
            }
        }
        String temp = "";
        for(int i = 0; i < strokes.length; i++){
            if(!strokes[i].equals("")) {
                temp += strokes[i] + ",";
            }
        }
        kanji.setDraw(temp);
        MainActivity.getKanjiDAO().insert(kanji);
    }
}
