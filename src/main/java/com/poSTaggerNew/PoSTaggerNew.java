package com.poSTaggerNew;

import com.newTools.PoSTagNew;
import com.newTools.TagEnum;
import com.sun.istack.internal.NotNull;
import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;
import com.treeBankReader.TreeBankReader;
import com.utilities.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PoSTaggerNew {

    private final HashMap<PoSTagNew, Double> poSTagGivenWordProbs = new HashMap<>();
    protected final List<String> words= new ArrayList<>();

    public PoSTaggerNew(TreeBankReader treeBankReader) {
        words.addAll(treeBankReader.getPoSTagPerWordNums().keySet());
        try {
            initProbs(treeBankReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initProbs(TreeBankReader treeBankReader) throws Exception {
        HashMap<PoSTagNew, Long>  poSTagsNums = treeBankReader.getPoSTagNums();
        HashMap<String, Long>  posTagPerWordNum = treeBankReader.getPoSTagPerWordNums();
        Double d;
        for (PoSTagNew poSTag : poSTagsNums.keySet()){
            d =(double)poSTagsNums.get(poSTag)/(double)posTagPerWordNum.get(poSTag.getWord());
            poSTagGivenWordProbs.put(poSTag, d);
        }
    }

    public List<PoSTagNew> poSTagging(String [] words){
        List<PoSTagNew> poSTags = new ArrayList<>();
        double maxProb;
        double tempProb;
        PoSTagNew maxProbPT;
        for(String s : words){
            maxProbPT = null;
            maxProb = 0;
            for(PoSTagNew poSTagNew : poSTagGivenWordProbs.keySet()){
                if(s.equals(poSTagNew.getWord())){

                    tempProb = poSTagGivenWordProbs.get(poSTagNew);
                    if(tempProb > maxProb){
                        maxProbPT = poSTagNew;
                        maxProb = tempProb;
                    }
                }

            }
            poSTags.add(maxProbPT);
        }
        return poSTags;
    }

    public HashMap<PoSTagNew, Double> getPoSTagGivenWordProbs() {
        return poSTagGivenWordProbs;
    }
}

