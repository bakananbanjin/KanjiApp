package com.minami.mykanji;


import android.util.Log;

import java.util.List;

public class KanjiDrawCheck {
    private Kanji kanji;
    private String kanjiDraw;
    private String kanjiCode;
    private static List<Radical> r = MainActivity.getRadicalDAO().getAllRadicals();

    public KanjiDrawCheck(){
        //Radical liste laden
    }




    public boolean checkKanjiDraw(String userDrawCode, Kanji k){
        String[] userKanjiStrokes = userDrawCode.split("x");
       // Log.d("APP KanjiDrawCheck1", k.getStrokeCount() + " " + userKanjiStrokes.length + " Enter CheckKanjiDdraw");
        if(userKanjiStrokes.length != k.getStrokeCount()){
            return false;
        }
        Log.d("APP KanjiDrawCheck2", "Kanjidraw.lenght == drawcode.length" + KanjiDraw.translateStroke(userDrawCode));
        //Manuell eingegebener Code wird geprueft klassische variante
        String[] kanjiStrokes = k.getDraw().split(",");
        for(int i = 0; i < kanjiStrokes.length ; i++){
            if(kanjiStrokes[i].equals(userDrawCode)){
                Log.d("APP KanjiDrawCheck", "alter Test erfolgreich");
                return true;
            }
        }

        Log.d("APP KanjiDrawCheck3", "alter Test nicht erfolgreich");
        //Radicalcode holen und in Int parsen if no radicalcode return false
        String[] kanjiRadicalCode = k.getRadicalCode().split(",");
        if(kanjiRadicalCode[0].equals("x")){ return false;}
        int[] kanjiRadicalCodeInt = new int[kanjiRadicalCode.length];
        for(int i = 0; i < kanjiRadicalCode.length; i++){
            kanjiRadicalCodeInt[i] = Integer.parseInt(kanjiRadicalCode[i]);
        }



        Log.d("APP KanjiDrawCheck4", "Radicaleliste nicht x anzahl der Codefragmente: " + kanjiRadicalCodeInt.length);
        //Code und Radical testen aus preformance Gruenden wurden die Radicale so erstellt, dass man eine List ansprechen kann
        //position aktuelle Position im userKanjiStrokes
        int position = 0;

        for(int i = 0; i < kanjiRadicalCodeInt.length; i++){
            //Radicalcode > 0 means a non Radical part make simple check with?
            if(kanjiRadicalCodeInt[i] > 0){
                //Build tempString for simple Check]

                if(kanjiStrokes == null || kanjiStrokes.length < 1 || kanjiStrokes[0].equals("")){
                    Log.d("APP KanjiDrawCheck4", "Kein Draw vorhanden");
                    return false;
                }
                Log.d("APP KanjiDrawCheck4", "KanjiStrokes[0]" + kanjiStrokes[0]);

                String tempString = "";
                String[] tempKanjiStrokes = new String[kanjiStrokes.length];

                for(int j = 0; j < kanjiRadicalCodeInt[i]; j++) {
                    tempString += userKanjiStrokes[position] + "x";
                    //kanjiStrokees entsprechend aufteilen und zusammenbauen
                    for(int l = 0; l < tempKanjiStrokes.length; l++ ){
                        if(tempKanjiStrokes[l] == null){
                            tempKanjiStrokes[l] = "";
                        }
                        tempKanjiStrokes[l] += kanjiStrokes[l].split("x")[position] + "x";
                        Log.d("APP KanjiDrawCheck", "tempKanjiStrokes[" +l +"] " + tempKanjiStrokes[l]);
                    }
                    position++;
                }
                Log.d("APP KanjiDrawCheck", "Codesegment ist kein Radical simple test starten mit: " + tempString);
                //Wenn Teilstring nicht matched return false
                if(!segmentCheck(tempString, tempKanjiStrokes)){
                    return false;
                }

            } else {
                //Radicalcode <= 0 means a Radical part check length and make a segment Check

                Radical tempRadical = r.get(Math.abs(kanjiRadicalCodeInt[i]));
                Log.d("APP KanjiDrawCheck", "Codesegment ist ein Radical: " + tempRadical.toString());
                String tempString = "";
                for(int j = 0; j < tempRadical.getStrokeCount(); j++) {
                    tempString += userKanjiStrokes[position] + "x";
                    position++;
                }

                Log.d("APP KanjiDrawCheck", "Codesegment ist ein Radical simple test starten mit: " + tempString + " " + tempRadical.getDraw());
                //Wenn Teilstring nicht matched return false
                if(!segmentCheck(tempString, tempRadical.getDraw().split(","))){
                    Log.d("APP KanjiDrawCheck", "Radicaltest fail: " + tempString + "    " +  tempRadical.toString());
                    return false;
                }
            }

        }
        return true;
    }

    public boolean segmentCheck(String segment, String[] radical){
        Log.d("APP KanjiDrawCheck", "Segment");
        for(int i = 0; i < radical.length ; i++){
            if(radical[i].equals(segment)){
                return true;
            }
        }
        return false;
    }
}
