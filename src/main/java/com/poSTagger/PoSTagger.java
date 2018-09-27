package com.poSTagger;

import com.exceptions.BiGramException;
import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;
import com.taggingTool.Tag;
import com.treeBankReader.TreeBankReader;

import java.util.*;


/**
 * This class is the most important one.
 * PoSTagger implements the baseline PoSTagging method,
 * it is possibile extends this class and override this method
 * with a costumized one.
 *
 * */
public class PoSTagger {

    private final HashMap<PoSTag, Double> poSTagGivenWordProbs = new HashMap<>();
    private final HashMap<String, HashSet<PoSTag>> poSTagForWord = new HashMap<>();
    protected final HashSet<PoSTag> poSTags = new HashSet<>();
    protected final HashSet<String> words= new HashSet<>();

    public PoSTagger(TreeBankReader treeBankReader) {
        words.addAll(treeBankReader.getPoSTagPerWordMap().keySet());
        try {
            initProbs(treeBankReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
    }

    /**
     * This method initializes the map poSTagGivenWordProbs.
     *
     * @param treeBankReader this is the class that contains the resources
     *                       from the training set
     */

    protected void initProbs(TreeBankReader treeBankReader) throws BiGramException{
        HashMap<PoSTag, Long>  poSTagsNums = treeBankReader.getPoSTagMap();
        HashMap<String, Long>  posTagPerWordNum = treeBankReader.getPoSTagPerWordMap();
        Double d;
        for (PoSTag poSTag : poSTagsNums.keySet()){
            d =(double)poSTagsNums.get(poSTag)/(double)posTagPerWordNum.get(poSTag.getWord());
            poSTagGivenWordProbs.put(poSTag, d);
        }
    }

    /**
     * This method initializes the map poSTagForWord.
     */
    private void init(){
        for(String word : words)
            poSTagForWord.put(word, new HashSet<>());

        for(PoSTag poSTag : poSTagGivenWordProbs.keySet()){
            poSTags.add(poSTag);
            poSTagForWord.get(poSTag.getWord()).add(poSTag);
        }
    }

    /**
     * This method perform the PoSTagging function.
     * @param wordsArray the sentence to PoSTag
     * @return the result of the PoSTagging
     */
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

