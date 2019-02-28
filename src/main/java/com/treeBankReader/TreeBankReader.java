package com.treeBankReader;

import com.taggingTool.BiGram;
import com.taggingTool.PoSTag;
import com.taggingTool.Tag;
import com.utilities.Log;
import com.utilities.UtilitiesIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Implements methods to read a TreeBank file and save
 * the number of tags, PoSTags, PoSTags per word and BiGrams.
 */
public class TreeBankReader {

    private final HashMap<Tag, Long> tagMap = new HashMap<>();
    private final HashMap<String, Long> poSTagPerWordMap = new HashMap<>();
    private final HashMap<PoSTag, Long> poSTagMap = new HashMap<>();
    private final HashMap<BiGram, Long> biGramMap = new HashMap<>();

    public TreeBankReader(String filePath) {
        saveMissingTagAndBiGram();
        fileToPoSTags(filePath, 1);
    }

    public TreeBankReader(String filePath, int version) {
        saveMissingTagAndBiGram();
        fileToPoSTags(filePath, version);
    }

    /**
     * Reads from file the TreeBank and store information
     * @param filePath TreeBank file
     */
    private void fileToPoSTags(String filePath,int version){
        Log.log("Reading TreeBank file <" + filePath +">");

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
                if(numRow != null && numRow >= 1){
                    if(numRow == 1) {
                        tag1 = Tag.valueOf("START");
                        saveTag(tag1);
                    }
                    try{
                        tag2 = Tag.valueOf(columns[3]);
                        saveTag(tag2);
                        word = columns[1];
                        switch (version){
                            case 1:
                                savePoSTag(new PoSTag(word.toLowerCase(),tag2));
                                break;
                            case 2:
                                if(!tag2.equals(Tag.PROPN))
                                    savePoSTag(new PoSTag(word.toLowerCase(),tag2));
                                else if(Character.isUpperCase(word.charAt(0)))
                                    savePoSTag(new PoSTag(word,tag2));
                                break;
                            case 3:

                                if(tag2.equals(Tag.PROPN))
                                    savePoSTag(new PoSTag(word,tag2));
                                else{
                                    if(Character.isUpperCase(word.charAt(0))){
                                        savePoSTag(new PoSTag(word,tag2));
                                        savePoSTag(new PoSTag(word.toLowerCase(),tag2));
                                    }else{
                                        savePoSTag(new PoSTag(word,tag2));
                                        savePoSTag(new PoSTag(
                                                word.substring(0,1).toUpperCase()
                                                        +word.substring(1),tag2));
                                    }

                                }

                                break;
                            case 4:

                                if(!tag2.equals(Tag.PROPN))
                                    savePoSTag(new PoSTag(word,tag2));
                                savePoSTag(new PoSTag(word,tag2));
                                break;
                            case 5:

                                if(!tag2.equals(Tag.PROPN))
                                    savePoSTag(new PoSTag(word.toLowerCase(),tag2));
                                savePoSTag(new PoSTag(word,tag2));
                                break;

                        }
                        saveBigram(new BiGram(tag1,tag2));
                        tag1 = tag2;
                    }catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            UtilitiesIO.closeFile(fr,br);
        }
    }

    /**
     * Stores missing tags and bigrams
     */
    private void saveMissingTagAndBiGram(){
        BiGram newBiGram;
        for(Tag tag1 : Tag.values()){
            if(!tagMap.containsKey(tag1))
                tagMap.put(tag1, (long)1);
            for (Tag tag2 :Tag.values()){
                newBiGram = new BiGram(tag1, tag2);
                if(!biGramMap.containsKey(newBiGram))
                    biGramMap.put(newBiGram, (long)1);
            }
        }
    }

    /**
     * Stores the tag in tagMap that contains the count of occurrences
     * of the tags
     * @param tag Tag object to be saved
     */
    private void saveTag(Tag tag){
        Long num = tagMap.get(tag);
        tagMap.put(tag, (num == null) ? (long)1 : num+1);
    }

    /**
     * Stores the postag in poSTagPerWordMap that contains the count
     * of occurrences of the postags
     * @param poSTag PoSTag object to be saved
     */
    private void savePoSTag(PoSTag poSTag){
        Long num;
        num = poSTagPerWordMap.get(poSTag.getWord());
        poSTagPerWordMap.put(poSTag.getWord(), (num == null) ? (long)1 : num+1);

        num = poSTagMap.get(poSTag);
        poSTagMap.put(poSTag, (num == null) ? (long)1 : num+1);


    }

    /**
     * Stores the bigram in biGramMap that contains the count
     * of occurrences of the bigrams
     * @param biGram Bigram object to be saved
     */
    private void saveBigram(BiGram biGram){
        Long num = biGramMap.get(biGram);
        biGramMap.put(biGram, (num == null) ? (long)1 : num+1);
    }

    /**
     * Convert String object to Integer object
     * @param string String object to be converted
     * @return String object converted to Integer
     */
    protected static Integer getIntegerString(String string) {
        Integer i;
        try {
            i = Integer.parseInt(string);
        } catch(Exception e) {
            return null;
        }

        return i;
    }

    public HashMap<Tag, Long> getTagMap() {
        return tagMap;
    }

    public HashMap<String, Long> getPoSTagPerWordMap() {
        return poSTagPerWordMap;
    }

    public HashMap<PoSTag, Long> getPoSTagMap() {
        return poSTagMap;
    }

    public HashMap<BiGram, Long> getBiGramMap() {
        return biGramMap;
    }

}
