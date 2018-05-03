package com.treeBankReader;

import com.taggingTool.BiGram;
import com.taggingTool.PoSTag;
import com.taggingTool.Tag;
import com.utilities.Log;
import com.utilities.UtilitiesIO;
import org.apache.commons.lang3.time.StopWatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class TreeBankReader {

    private final HashMap<Tag, Long> tagNums = new HashMap<>();
    private final HashMap<String, Long> poSTagPerWordNums = new HashMap<>();
    private final HashMap<PoSTag, Long> poSTagNums = new HashMap<>();
    private final HashMap<BiGram, Long> bigramNums = new HashMap<>();

    public TreeBankReader(String filePath) {
        fileToPoSTags(filePath);
    }

    private void fileToPoSTags(String filePath){
        Log.log("Reading TreeBank file <" + filePath +">");

        StopWatch s = StopWatch.createStarted();
        FileReader fr = null;
        BufferedReader br = null;

        try{
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);

            String currentLine;
            String[] columns;

            Tag tag1 = null;
            Tag tag2;
            String word;

            Integer numRow;
            while((currentLine = br.readLine()) != null){
                columns = currentLine.split("\t");
                numRow = getIntegerString(columns[0]);
                if(numRow != null && numRow >= 1){ // sto leggendo una riga con [NUM WORD WORD_ORIGIN TAG ...]
                    if(numRow == 1) {
                        tag1 = Tag.valueOf("START");
                        saveTag(tag1);
                    }
                    try{
                        tag2 = Tag.valueOf(columns[3]);
                        saveTag(tag2);
                        word = columns[1].toLowerCase();
                        savePoSTag(new PoSTag(word,tag2));

                        saveBigram(new BiGram(tag1,tag2));
                        tag1 = tag2;
                    }catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }
                }
            }

            saveMissingTagAndBigram();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            UtilitiesIO.closeFile(fr,br);
        }
    }

    private void saveMissingTagAndBigram(){
        BiGram newBiGram;
        for(Tag tag1 : Tag.values()){
            if(!tagNums.containsKey(tag1))
                tagNums.put(tag1, (long)1);
            for (Tag tag2 :Tag.values()){
                newBiGram = new BiGram(tag1, tag2);
                if(!bigramNums.containsKey(newBiGram))
                    bigramNums.put(newBiGram, (long)1);
            }
        }
    }


    private void saveTag(Tag tag){
        Long num = tagNums.get(tag);
        tagNums.put(tag, (num == null) ? (long)1 : num+1);
    }

    private void savePoSTag(PoSTag poSTag){
        Long num;
        num = poSTagPerWordNums.get(poSTag.getWord());
        poSTagPerWordNums.put(poSTag.getWord(), (num == null) ? (long)1 : num+1);

        num = poSTagNums.get(poSTag);
        poSTagNums.put(poSTag, (num == null) ? (long)1 : num+1);


    }

    private void saveBigram(BiGram biGram){
        Long num = bigramNums.get(biGram);
        bigramNums.put(biGram, (num == null) ? (long)1 : num+1);
    }

    private static Integer getIntegerString(String string) {
        Integer i;
        try {
            i = Integer.parseInt(string);
        } catch(Exception e) {
            return null;
        }

        return i;
    }

    public HashMap<Tag, Long> getTagNums() {
        return tagNums;
    }

    public HashMap<String, Long> getPoSTagPerWordNums() {
        return poSTagPerWordNums;
    }

    public HashMap<PoSTag, Long> getPoSTagNums() {
        return poSTagNums;
    }

    public HashMap<BiGram, Long> getBigramNums() {
        return bigramNums;
    }

}
