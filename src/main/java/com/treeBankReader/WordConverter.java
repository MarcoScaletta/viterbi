package com.treeBankReader;

import com.utilities.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WordConverter {
    private static HashMap<String,String []> detAdpMap = new HashMap<>();
    static {
        detAdpMap.put("del", "di il".split(" "));
        detAdpMap.put("dello", "di lo".split(" "));
        detAdpMap.put("della", "di la".split(" "));
        detAdpMap.put("dei", "di i".split(" "));
        detAdpMap.put("degli", "di gli".split(" "));
        detAdpMap.put("delle", "di le".split(" "));

        detAdpMap.put("al", "a il".split(" "));
        detAdpMap.put("allo", "a lo".split(" "));
        detAdpMap.put("alla", "a la".split(" "));
        detAdpMap.put("ai", "a i".split(" "));
        detAdpMap.put("agli", "a gli".split(" "));
        detAdpMap.put("alle", "a le".split(" "));

        detAdpMap.put("dal", "da il".split(" "));
        detAdpMap.put("dallo", "da lo".split(" "));
        detAdpMap.put("dalla", "da la".split(" "));
        detAdpMap.put("dai", "da i".split(" "));
        detAdpMap.put("dagli", "da gli".split(" "));
        detAdpMap.put("dalle", "da le".split(" "));

        detAdpMap.put("nel", "in il".split(" "));
        detAdpMap.put("nello", "in lo".split(" "));
        detAdpMap.put("nella", "in la".split(" "));
        detAdpMap.put("nei", "in i".split(" "));
        detAdpMap.put("negli", "in gli".split(" "));
        detAdpMap.put("nelle", "in le".split(" "));


        detAdpMap.put("col", "con il".split(" "));
        detAdpMap.put("coi", "con i".split(" "));

        detAdpMap.put("sul", "su il".split(" "));
        detAdpMap.put("sullo", "su lo".split(" "));
        detAdpMap.put("sulla", "su la".split(" "));
        detAdpMap.put("sui", "su i".split(" "));
        detAdpMap.put("sugli", "su gli".split(" "));
        detAdpMap.put("sulle", "su le".split(" "));
    }

    public static String[] formatStringSentence(String [] sentenceString){
        List<String> stringList = new ArrayList<>(Arrays.asList(sentenceString));
        String [] temp;
        String [] res;
        for (int i = 0; i < stringList.size(); i++) {
            stringList.set(i,stringList.get(i).toLowerCase());
            temp = detAdpMap.get(stringList.get(i));
            if(temp != null){
                Log.log("contiene "+stringList.get(i));
                stringList.remove(i);
                for (int j = 0; j < temp.length; j++) {
                    stringList.add(i+j, temp[j]);
                }
            }
        }
        res = new String[stringList.size()];
        res = stringList.toArray(res);
        return res;
    }

    public static HashMap<String, String[]> getDetAdpMap() {
        return detAdpMap;
    }
}
