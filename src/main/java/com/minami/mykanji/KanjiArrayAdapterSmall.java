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

public class KanjiArrayAdapterSmall extends ArrayAdapter {
    private Context context;
    private int layout;
    private List<Kanji> kanjis;
    private KanjiDAO kanjiDAO;
    public KanjiArrayAdapterSmall(Context context, int layout, List<Kanji> kanjis){
        super(context, layout, kanjis);
        this.context = context;
        this.layout = layout;
        this.kanjis = kanjis;
        this.kanjiDAO = MainActivity.getKanjiDAO();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);
        }
        Kanji temp = kanjis.get(position);
        TextView tv = convertView.findViewById(R.id.tvlearnRKanji);
        tv.setText(temp.getKanji());

        if(temp.isSelected()){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }


        return convertView;
    }
}
