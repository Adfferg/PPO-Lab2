package com.example.task2;

import android.util.Pair;

import java.util.ArrayList;

public class StageInfo {
    private ArrayList<Pair<String, Integer>> stageInfo = new ArrayList<>();

    public Pair<String, Integer> getStageInfo(int pos){
        return stageInfo.get(pos);
    }
    public ArrayList<Pair<String, Integer>> getStagesInfo(){
        return  stageInfo;
    }
    public void setStageInfo(int preparation,int sets, int cycles, int workingTime, int rest, int restBetweenSets, String string1,
                             String string2, String string3, String string4){
        stageInfo.add(new Pair<String, Integer>(string1, preparation));
        for (int i = 0; i < sets; i++) {
            for (int j = 0; j < cycles; j++) {
                stageInfo.add(new Pair<String, Integer>(string2, workingTime));
                stageInfo.add(new Pair<String, Integer>(string3, rest));
            }
            stageInfo.add(new Pair<String, Integer>(string4, restBetweenSets));
        }
    }

}
