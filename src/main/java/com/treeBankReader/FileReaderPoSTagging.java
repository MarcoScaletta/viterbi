package com.treeBankReader;

import com.taggingTool.*;
import com.utilities.Log;
import com.utilities.ReadObject;
import com.utilities.WriteObject;
import com.utilitiesIO.UtilitiesIO;
import org.apache.commons.lang3.time.StopWatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileReaderPoSTagging implements Serializable{

    private List<Tag> tags = new ArrayList<>();

    private List<PoSTag> poSTags = new ArrayList<>();

    private List<BiGram> biGrams = new ArrayList<>();

    private HashMap<Tag, Double> tagsMap= new HashMap<>();

    private HashMap<String, List<PoSTag>> words = new HashMap<>();
    //private List<String> words = new ArrayList();
    private List<Sentence> sentences = new ArrayList<>();
    private int numPoSTags;
    private int numTags;
    private int numWord;

    public FileReaderPoSTagging(String treeBankFile) {
        fileToPoSTags(treeBankFile);
    }

    public FileReaderPoSTagging() {
    }

    public FileReaderPoSTagging updatePoSTagger(String treeBankFile, String objectFile){
        fileToPoSTags(treeBankFile);
        WriteObject.writeObject(objectFile, this);
        return this;
    }

    public static FileReaderPoSTagging setSavedPoSTagger(String treeBankFile, String objectFile){

        Object obj = ReadObject.readObject(objectFile);
        if(obj instanceof FileReaderPoSTagging)
            return (FileReaderPoSTagging) obj;
        else
            return null;
    }

    public void fileToSentences(String filePath){
        FileReader fr = null;
        BufferedReader br = null;
        try{
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);

            String currentLine;
            String[] columns;
            List<PoSTag> sentence = new ArrayList<>();

            Integer numRow;
            while((currentLine = br.readLine()) != null){
                columns = currentLine.split("\t");
                numRow = getIntegerString(columns[0]);
                if(numRow != null && numRow >= 1){ // sto leggendo una riga con [NUM WORD WORD_ORIGIN TAG ...]
                    if(numRow == 1)
                        sentence = new ArrayList<>();
                    sentence.add(new PoSTag(columns[1],new Tag(columns[3])));
                }else if(columns[0].equals(""))
                    sentences.add(new Sentence(sentence));

            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(fr!=null)
                    fr.close();
                if(br!=null)
                    br.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public int moreEqualSentences(){
        int equal = 0;
        for (int i = 0; i < sentences.size(); i++) {
            for (int j = 0; j < sentences.size(); j++) {
                if(j>i && sentences.get(i).equalsByWord(sentences.get(j))){
                    if(!sentences.get(i).equals(sentences.get(j)))
                        equal++;
                }

            }
        }
        return equal;
    }

    public void fileToPoSTags(String filePath){
        Log.log("starting reading file: " + filePath);
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
            List<PoSTag> sentence = new ArrayList<>();
            String word;

            Integer numRow;
            while((currentLine = br.readLine()) != null){
                columns = currentLine.split("\t");
                numRow = getIntegerString(columns[0]);
                if(numRow != null && numRow >= 1){ // sto leggendo una riga con [NUM WORD WORD_ORIGIN TAG ...]

                    if(numRow == 1) {
                        sentence = new ArrayList<>();
                        tag1 = saveTag(new Tag("S_0"));
                        //word = saveWord("$_");
                        //savePoSTag(new PoSTag(word,tag1));
                    }
                    tag2 = saveTag(new Tag(columns[3]));
                    word = saveWord(columns[1]);
                    saveElements1(tag1,word,tag2,sentence);
                    tag1 = tag2;


                }else if(columns[0].equals("")){
                    tag2 = saveTag(new Tag("S_1"));
                    saveBigram(new BiGram(tag1,tag2));
                    sentences.add(new Sentence(sentence));
                }
            }

            tags.add(0,tags.remove(tags.indexOf(new Tag("S_0"))));
            tags.add(tags.remove(tags.indexOf(new Tag("S_1"))));

            numPoSTags = poSTags.size();
            numTags = tags.size();
            numWord = words.size();
            setProb();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            UtilitiesIO.closeFile(fr,br);
        }
        Log.log("\nstop reading file: " + filePath + " time: "+ (double)s.getTime()/1000 + " seconds");
    }

    public String saveWord(String word){
        if(!words.containsKey(word))
            words.put(word, new ArrayList<>());
        return word;
    }

    private Tag saveTag(Tag t){
        if(tags.contains(t)){
            t = tags.get(tags.indexOf(t));}
        else
            tags.add(t);
        t.incNum();
        return t;
    }

    public PoSTag savePoSTag(PoSTag poSTag){
        if(poSTags.contains(poSTag)){
            poSTag = poSTags.get(poSTags.indexOf(poSTag));
        }else{
            words.get(poSTag.getWord()).add(poSTag);
            poSTags.add(poSTag);
        }
        poSTag.incNum();

        return poSTag;
    }

    private BiGram saveBigram(BiGram biGram){
        if(biGrams.contains(biGram)){
            biGram = biGrams.get(biGrams.indexOf(biGram));}
        else
            biGrams.add(biGram);
        biGram.incNum();
        return biGram;

    }

    public Tag getSavedTag(String s){
        Tag t = new Tag(s);
        if(tags.contains(t)){
            return tags.get(tags.indexOf(t));
        }
        else return t;

    }

    private void saveElements1(Tag tag1, String word, Tag tag2, List<PoSTag> list) {
        list.add(savePoSTag(new PoSTag(word,tag2)));
        saveBigram(new BiGram(tag1,tag2));
    }

    private <E> void addGenericCounterType(List<E> list, E val){
        if(Counter.class.isInstance(val)){
            if(!list.contains(val))
                list.add(val);
            else
                val = list.get(list.indexOf(val));
            ((Counter) val).incNum();
        }
    }

    private void setProb(){
        setPoSTagsProb();
        setBiGramsProb();
    }

    private void setPoSTagsProb(){

        for(PoSTag poSTag : poSTags) {
            double posTagNum = (double) poSTag.getNum();
            double tagNum = (double) poSTag.getTag().getNum();
            poSTag.setProb(((posTagNum/tagNum)));//poSTag.getNum()
            poSTag.setProbFin((double)1/(double)words.get(poSTag.getWord()).size());
        }
    }

    private void setBiGramsProb(){

        for(Tag t1 : tags)
            for(Tag t2 : tags)
                if(!biGrams.contains(new BiGram(t1,t2)))
                    saveBigram(new BiGram(t1,t2));

        for(BiGram biGram : biGrams) {
            double biGramNum = (double) biGram.getNum();
            double tagNum = (double) biGram.getTags()[0].getNum();
            if(biGramNum == tagNum)
                tagNum+= Math.exp(-10);
            biGram.setProb((biGramNum/tagNum));
            //biGram.setProb(Math.log(biGramNum/tagNum));
        }

    }

    public List<Sentence> getSentences() {
        return sentences;
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

    public int getNumPoSTags() {
        return numPoSTags;
    }

    public int getNumTags() {
        return numTags;
    }

    public int getNumWord() {
        return numWord;
    }

    public List<PoSTag> getPoSTags() {
        return poSTags;
    }

    public List<BiGram> getBiGrams() {
        return biGrams;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public HashMap<String, List<PoSTag>> getWords() {
        return words;
    }
}
