package com.newTools;

import com.poSTaggerNew.PoSTaggerNew;
import com.treeBankReader.TreeBankReader;
import com.utilities.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ViterbiNew extends PoSTaggerNew{


    private HashMap<PoSTagNew, Double> wordGivenPoSTagProbs;
    private HashMap<BiGramNew, Double> bigramProbs;
    protected String string = "";


    public ViterbiNew(TreeBankReader treeBankReader) {
        super(treeBankReader);
    }

    @Override
    protected void initProbs(TreeBankReader treeBankReader) throws Exception {

        wordGivenPoSTagProbs= new HashMap<>();
        bigramProbs= new HashMap<>();

        HashMap<TagEnum, Long>  tagNums = treeBankReader.getTagNums();
        HashMap<PoSTagNew, Long>  poSTagsNums = treeBankReader.getPoSTagNums();
        HashMap<BiGramNew, Long>  biGramSNums = treeBankReader.getBigramNums();

        if(treeBankReader.getBigramNums().isEmpty())
            throw new Exception("BiGrams counters is empty");

        for(BiGramNew biGramNew : biGramSNums.keySet())
            bigramProbs.put(biGramNew,(double) biGramSNums.get(biGramNew)/(double)tagNums.get(biGramNew.getTags()[0]));
        Log.log(""+ bigramProbs.keySet().size());

        for(PoSTagNew poSTagNew : poSTagsNums.keySet())
            wordGivenPoSTagProbs.put(poSTagNew,(double) poSTagsNums.get(poSTagNew)/(double)tagNums.get(poSTagNew.getTag()));


        Log.log("OVERRIDDEN ");
    }

    @Override
    public List<PoSTagNew> poSTagging(String[] sentence) {
        List<PoSTagNew> poSTagList = new ArrayList<>();
        double viterbi[][] = new double[TagEnum.values().length][sentence.length+2];
        int backpointer[][] = new int[TagEnum.values().length][sentence.length+2];
        double newScore;
        int indexTag;
        TagEnum tagForMissingWordArray []= {TagEnum.PROPN, TagEnum.NOUN, TagEnum.ADJ,TagEnum.VERB};
        List<TagEnum> tagForMissingWord = Arrays.asList(tagForMissingWordArray);
        PoSTagNew poSTag;
        for (int i = 0; i < viterbi.length; i++) {
            for (int j = 0; j < viterbi[0].length; j++) {
                viterbi[i][j] = Double.NEGATIVE_INFINITY;

            }
        }
        viterbi[0][0]=0;

        for (int t = 0; t < sentence.length; t++) {

            for (int s = 0; s < TagEnum.values().length; s++) {
                for(BiGramNew biGram : bigramProbs.keySet()){
                    if(biGram.getTags()[0].equals(TagEnum.values()[s])){
                        TagEnum tag = biGram.getTags()[1];
                        indexTag = Arrays.asList(TagEnum.values()).indexOf(tag);
                        if (!wordGivenPoSTagProbs.keySet().contains(new PoSTagNew(sentence[t], tag))) {
                            if (!words.contains(sentence[t]) && tagForMissingWord.contains(tag)) {
                                newScore = viterbi[s][t]
                                        + Math.log(bigramProbs.get(biGram))
                                        + Math.log((double)1/(double)tagForMissingWord.size());
                            }else
                                newScore = Double.NEGATIVE_INFINITY;
                        }else {
                            poSTag = new PoSTagNew(sentence[t], tag);

                            newScore = viterbi[s][t]
                                    + Math.log(bigramProbs.get(biGram))
                                    + Math.log(wordGivenPoSTagProbs.get(poSTag));
                        }
                        if(viterbi[indexTag][t+1]==Double.NEGATIVE_INFINITY || newScore>viterbi[indexTag][t+1]){
                            viterbi[indexTag][t+1] = newScore;
                            backpointer[indexTag][t+1]=s;
                        }
                    }

                }
            }
        }

        System.out.format("%40s","");
        for (int i = 1; i < viterbi[0].length-1; i++) {
            System.out.format("%30s",sentence[i-1]);
        }
        System.out.println();
        for (int i = 0; i < viterbi.length; i++) {
            System.out.format("%10s",TagEnum.values()[i]);
            for (int j = 0; j < viterbi[0].length; j++) {
                System.out.format("%30s",viterbi[i][j]);
            }
            System.out.println();

        }
        for (int i = 0; i < backpointer.length; i++) {
            for (int j = 0; j < backpointer[0].length; j++) {
                System.out.format("%30s",backpointer[i][j]);
            }
            System.out.println();

        }

        double maxProb = Double.NEGATIVE_INFINITY;
        int indexMax = -1;

        for (int i = 0; i < viterbi.length; i++) {
            double temp = viterbi[i][viterbi[0].length-2];
            if(temp>maxProb){
                maxProb = temp;
                indexMax = i;
            }
        }

        if(maxProb > Double.NEGATIVE_INFINITY)
            for (int j = backpointer[0].length-2 ; j > 0; j--) {
                poSTagList.add(0,
                        new PoSTagNew(sentence[j-1],
                                TagEnum.values()[indexMax]));
                indexMax = backpointer[indexMax][j];
            }

        return poSTagList;
    }

    public HashMap<PoSTagNew, Double> getWordGivenPoSTagProbs() {
        return wordGivenPoSTagProbs;
    }

    public HashMap<BiGramNew, Double> getBigramProbs() {
        return bigramProbs;
    }
}
