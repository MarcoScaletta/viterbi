package com.poSTagger;

import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;
import com.taggingTool.Tag;
import com.treeBankReader.TreeBankReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PoSTagger {

    private final HashMap<PoSTag, Double> poSTagGivenWordProbs = new HashMap<>();
    protected final List<String> words= new ArrayList<>();

    public PoSTagger(TreeBankReader treeBankReader) {
        words.addAll(treeBankReader.getPoSTagPerWordNums().keySet());
        try {
            initProbs(treeBankReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public Sentence poSTagging(String [] wordsArray){
        Sentence sentence = new Sentence();
        double maxProb;
        double tempProb;
        PoSTag maxProbPT;
        for(String s : wordsArray){
            maxProbPT = null;
            maxProb = 0;
            if(words.contains(s)){
                for(PoSTag poSTag : poSTagGivenWordProbs.keySet()){
                    if(s.equals(poSTag.getWord())){

                        tempProb = poSTagGivenWordProbs.get(poSTag);
                        if(tempProb > maxProb){
                            maxProbPT = poSTag;
                            maxProb = tempProb;
                        }
                    }

                }
                sentence.getPoSTags().add(maxProbPT);
            }else
                sentence.getPoSTags().add(new PoSTag(s,Tag.PROPN));
        }
        return sentence;
    }

    public HashMap<PoSTag, Double> getPoSTagGivenWordProbs() {
        return poSTagGivenWordProbs;
    }
}

