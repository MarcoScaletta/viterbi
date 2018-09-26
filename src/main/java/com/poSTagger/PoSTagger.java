package com.poSTagger;

import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;
import com.taggingTool.Tag;
import com.treeBankReader.TreeBankReader;

import java.util.*;

public class PoSTagger {

    private final HashMap<PoSTag, Double> poSTagGivenWordProbs = new HashMap<>();
    private final HashMap<String, HashSet<PoSTag>> poSTagForWord = new HashMap<>();
    protected final HashSet<PoSTag> poSTags = new HashSet<>();
    protected final HashSet<String> words= new HashSet<>();

    public PoSTagger(TreeBankReader treeBankReader) {
        words.addAll(treeBankReader.getPoSTagPerWordNums().keySet());
        try {
            initProbs(treeBankReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
    }

    protected void initProbs(TreeBankReader treeBankReader) throws Exception {
        HashMap<PoSTag, Long>  poSTagsNums = treeBankReader.getPoSTagNums();
        HashMap<String, Long>  posTagPerWordNum = treeBankReader.getPoSTagPerWordNums();
        Double d;
        for (PoSTag poSTag : poSTagsNums.keySet()){
            d =(double)poSTagsNums.get(poSTag)/(double)posTagPerWordNum.get(poSTag.getWord());
            poSTagGivenWordProbs.put(poSTag, d);
        }
    }

    private void init(){
        for(String word : words)
            poSTagForWord.put(word, new HashSet<>());

        for(PoSTag poSTag : poSTagGivenWordProbs.keySet()){
            poSTags.add(poSTag);
            poSTagForWord.get(poSTag.getWord()).add(poSTag);
        }
    }

    public Sentence poSTagging(String [] wordsArray){
        Sentence sentence = new Sentence();
        double maxProb;
        double tempProb;
        PoSTag [] poSTagsList = new PoSTag[wordsArray.length];
        PoSTag maxProbPT;

        for (int i = 0; i < wordsArray.length; i++) {
            maxProbPT = null;
            maxProb = 0;
            if(words.contains(wordsArray[i])){
                for(PoSTag poSTag : poSTagForWord.get(wordsArray[i])){

                    tempProb = poSTagGivenWordProbs.get(poSTag);
                    if(tempProb > maxProb){
                        maxProbPT = poSTag;
                        maxProb = tempProb;
                    }
                }

                poSTagsList[i] = (maxProbPT);
            }else
                poSTagsList[i] =(new PoSTag(wordsArray[i],Tag.PROPN));
        }
        sentence.getPoSTags().addAll(Arrays.asList(poSTagsList));

        return sentence;
    }

    public HashMap<PoSTag, Double> getPoSTagGivenWordProbs() {
        return poSTagGivenWordProbs;
    }

    public HashSet<PoSTag> getPoSTags() {
        return poSTags;
    }

    public HashSet<String> getWords() {
        return words;
    }
}

