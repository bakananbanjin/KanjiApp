package com.minami.mykanji;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVreader {
    private static BufferedReader br;
    private static String line;
    private static String split;

    public CSVreader() {

    }

    public static void readFileRadical(String file, Context context){
        List<String> fileLines = new ArrayList<String>();
        try {
            String line;
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(file), "UTF-8"));
            while((line = br.readLine()) != null){
                fileLines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < fileLines.size(); i++) {
            String[] tempString = fileLines.get(i).split(";");
            int id = Math.abs(Integer.parseInt(tempString[0]));
            int strokeCounr = Integer.parseInt(tempString[1]);
            String drawCode = tempString[2];
            Radical temp = new Radical(id, strokeCounr, drawCode);
            MainActivity.getRadicalDAO().insert(temp);
        }
    }

    public static void readFileKanji(String file, Context context){
        List<String> fileLines = new ArrayList<String>();
        try {
            String line;
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(file), "UTF-8"));
            while((line = br.readLine()) != null){
                fileLines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < fileLines.size(); i++) {
            String[] tempString = fileLines.get(i).split(";");

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
            String draw = tempString[9];
            String image = tempString[10];
            String radicalCode = tempString [11];
            Kanji temp = new Kanji(id, kanji, onYomi, kunYomi, translation, gradeLvl, memory, strokeCount, selected, draw, image, radicalCode);

            MainActivity.getKanjiDAO().insert(temp);
        }
    }

    public static boolean writeFileRadical(String file, Context context, List<Radical> radicals){
        File root = Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/Kanji");
        dir.mkdirs();
        //File file = new File(dir, "myData.txt");
        File newFile = new File(dir, file);
        try{
            FileOutputStream f = new FileOutputStream(newFile);
            PrintWriter pw = new PrintWriter(f);

            for(int i = 0; i < radicals.size(); i++){

                Radical k = radicals.get(i);
                String tempRadical = k.getId() + ";" + k.getStrokeCount() + ";" + k.getDraw() + "\n";
                f.write(tempRadical.getBytes());
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean writeFileKanji(String file, Context context, List<Kanji> kanjis){
        File root = Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/Kanji");
        dir.mkdirs();
        //File file = new File(dir, "myData.txt");
        File newFile = new File(dir, file);
        try{
            FileOutputStream f = new FileOutputStream(newFile);
            PrintWriter pw = new PrintWriter(f);

            for(int i = 0; i < kanjis.size(); i++){

                Kanji k = kanjis.get(i);
                String tempKanji = k.getId() + ";" + k.getKanji()+ ";"+ k.getOnYomi()+ ";" +
                        k.getKunYomi() + ";" + k.getTranslation() +  ";" + k.getLvl() + ";" +
                        k.getMemory() +";" + k.getStrokeCount() + ";" + k.isSelected()+ ";" + k.getDraw() +
                        ";" + k.getImage() + "\n";
                f.write(tempKanji.getBytes());
            }
            f.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
