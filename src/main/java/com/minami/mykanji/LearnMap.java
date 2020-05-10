package com.minami.mykanji;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LearnMap implements Serializable {
    private List<LearnMapItem> myList;
    private List<Kanji> myKanjis;

    LearnMap(List<Kanji> myKanjis){
        this.myKanjis = myKanjis;
        if(this.myKanjis == null) {
            //error
        } else {
            myList = new ArrayList<LearnMapItem>();
            for (int i = 0; i < myKanjis.size(); i++) {
                myList.add(new LearnMapItem(i, myKanjis.get(i).getId()));
            }
        }
    }
    public int getSize(){
        return myList.size();
    }
    public boolean insertTestValue(Kanji k, boolean value, int testNumber){
        for(int i = 0; i < myList.size(); i++){
            if(k.getId() == myList.get(i).getKanjiId()){
                switch(testNumber){
                    case 1:
                        myList.get(i).setTest1(value);
                        break;
                    case 2:
                        myList.get(i).setTest2(value);
                        break;
                    case 3:
                        myList.get(i).setTest3(value);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }
        return false;
    }
    private LearnMapItem findLearnMapItem (Kanji k){
        for(int i = 0; i < myList.size(); i++){
            if(k.getId() == myList.get(i).getKanjiId()){
                return myList.get(i);
            }
        }
        return null;
    }
    public String toString(){
        String temp = "";
        for(int i = 0; i < myList.size(); i++){
            temp += "ID: " + myList.get(i).getId() + " KanjiId: " + myList.get(i).getKanjiId() +
                    " Test1: " + myList.get(i).isTest1() + " Test2: " + myList.get(i).isTest2() +
                    " Test3: " + myList.get(i).isTest3() + "\n";

        }
        return temp;
    }
    public boolean[] getValues(Kanji k){
        LearnMapItem tempMap = findLearnMapItem(k);
        if(tempMap != null){
            return new boolean[] {tempMap.isTest1(), tempMap.isTest2(), tempMap.isTest3()};
        }
        return null;
    }


    private class LearnMapItem {
        private int id;
        private int kanjiId;
        private boolean test1;
        private boolean test2;
        private boolean test3;
        LearnMapItem(int id, int kanjiId){
            this.id = id;
            this.kanjiId = kanjiId;
            this.test1 = false;
            this.test2 = false;
            this.test3 = false;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getKanjiId() {
            return kanjiId;
        }

        public void setKanjiId(int kanjiId) {
            this.kanjiId = kanjiId;
        }

        public boolean isTest1() {
            return test1;
        }

        public void setTest1(boolean test1) {
            this.test1 = test1;
        }

        public boolean isTest2() {
            return test2;
        }

        public void setTest2(boolean test2) {
            this.test2 = test2;
        }

        public boolean isTest3() {
            return test3;
        }

        public void setTest3(boolean test3) {
            this.test3 = test3;
        }
    }
}
