package com.treeBankReader;

import com.newTools.BiGramNew;
import com.newTools.PoSTagNew;
import com.newTools.TagEnum;
import com.utilities.Log;
import com.utilitiesIO.UtilitiesIO;
import org.apache.commons.lang3.time.StopWatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TreeBankReader {
    private final HashMap<TagEnum, Long> tagNums = new HashMap<>();
    private final HashMap<String, Long> poSTagPerWordNums = new HashMap<>();
    private final HashMap<PoSTagNew, Long> poSTagNums = new HashMap<>();
    private final HashMap<BiGramNew, Long> bigramNums = new HashMap<>();




    public void fileToPoSTags(String filePath, int nGramLength){
        Log.log("starting reading file: " + filePath);

        StopWatch s = StopWatch.createStarted();
        FileReader fr = null;
        BufferedReader br = null;

        try{
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);

            String currentLine;
            String[] columns;

            TagEnum tag1 = null;
            TagEnum tag2;
            String word;

            Integer numRow;
            while((currentLine = br.readLine()) != null){
                columns = currentLine.split("\t");
                numRow = getIntegerString(columns[0]);
                if(numRow != null && numRow >= 1){ // sto leggendo una riga con [NUM WORD WORD_ORIGIN TAG ...]
                    if(numRow == 1) {
                        tag1 = TagEnum.valueOf("START");
                        saveTag(tag1);
                    }
                    try{
                        tag2 = TagEnum.valueOf(columns[3]);
                        saveTag(tag2);
                        word = columns[1].toLowerCase();
                        savePoSTag(new PoSTagNew(word,tag2));

                        if(nGramLength == 2){
                            saveBigram(new BiGramNew(tag1,tag2));
                            tag1 = tag2;
                        }
                    }catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }

                }else if(columns[0].equals("")){
                    tag2 = TagEnum.valueOf("END");
                    saveTag(tag2);
                    if(nGramLength == 2)
                        saveBigram(new BiGramNew(tag1,tag2));
                }
            }

            if(nGramLength == 2)
                saveMissingTagAndBigram();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            UtilitiesIO.closeFile(fr,br);
        }
        Log.log("\nstop reading file: " + filePath + " time: "+ (double)s.getTime()/1000 + " seconds");
    }

    private void saveMissingTagAndBigram(){
        BiGramNew newBiGram;
        for(TagEnum tagEnum : TagEnum.values()){
            if(!tagNums.containsKey(tagEnum))
                tagNums.put(tagEnum, (long)1);
            for (TagEnum tagEnum1 :TagEnum.values()){
                newBiGram = new BiGramNew(tagEnum, tagEnum1);
                if(!bigramNums.containsKey(newBiGram))
                    bigramNums.put(newBiGram, (long)1);
            }
        }
    }


    private void saveTag(TagEnum tag){
        Long num = tagNums.get(tag);
        tagNums.put(tag, (num == null) ? (long)1 : num+1);
    }

    private void savePoSTag(PoSTagNew poSTag){
        Long num;
        num = poSTagPerWordNums.get(poSTag.getWord());
        poSTagPerWordNums.put(poSTag.getWord(), (num == null) ? (long)1 : num+1);

        num = poSTagNums.get(poSTag);
        poSTagNums.put(poSTag, (num == null) ? (long)1 : num+1);


    }

    private void saveBigram(BiGramNew biGram){
        Long num = bigramNums.get(biGram);
        bigramNums.put(biGram, (num == null) ? (long)1 : num+1);
    }

    private Integer getIntegerString(String string) {
        Integer i;
        try {
            i = Integer.parseInt(string);
        } catch(Exception e) {
            return null;
        }

        return i;
    }

    public HashMap<TagEnum, Long> getTagNums() {
        return tagNums;
    }

    public HashMap<String, Long> getPoSTagPerWordNums() {
        return poSTagPerWordNums;
    }

    public HashMap<PoSTagNew, Long> getPoSTagNums() {
        return poSTagNums;
    }

    public HashMap<BiGramNew, Long> getBigramNums() {
        return bigramNums;
    }

}
