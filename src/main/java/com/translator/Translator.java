package com.translator;

import com.utilities.Log;
import com.utilitiesIO.UtilitiesIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Translator {
    private String dictionaryFile;
    private HashMap<String, String> dictionary = new HashMap<>();

    public Translator(String dictionaryFile) {
        this.dictionaryFile = dictionaryFile;
        initDictionary();
    }

    private void initDictionary(){
        FileReader fr = null;
        BufferedReader br = null;
        String currentLine;
        String columns[];
        try{
            fr = new FileReader(dictionaryFile);
            br = new BufferedReader(fr);

            while((currentLine = br.readLine()) != null){
                columns = currentLine.split(",");
                for(String s : columns)
                    Log.log(s);
                Log.log("column.length " + Arrays.toString(columns));
                if(columns.length == 2){
                    dictionary.put(columns[0],columns[1]);
                }

            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            UtilitiesIO.closeFile(fr,br);
        }
    }

    public HashMap<String, String> getDictionary() {
        return dictionary;
    }

    @Override
    public String toString() {
        String res = "";
        for(String s : dictionary.keySet()) {

            Log.log("res");
            res = res + "[" + s + "," + dictionary.get(s) + "]";
        }
        return res;
    }
}
