package com.poSTagger;

import com.taggingTool.BiGram;
import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;
import com.taggingTool.Tag;
import com.treeBankReader.TreeBankReader;

import java.util.*;

public class Viterbi extends PoSTagger {


    private HashMap<PoSTag, Double> wordGivenPoSTagProbs;
    private HashMap<BiGram, Double> biGramProbs;
    private HashMap<Tag, HashSet<BiGram>> fstTagBigrams;
    private final Tag tagForMissingWordArray [];

    public Viterbi(TreeBankReader treeBankReader) {
        super(treeBankReader);
        this.tagForMissingWordArray =  new Tag[1];
        tagForMissingWordArray[0] = Tag.NOUN;

    }
    public Viterbi(TreeBankReader treeBankReader, Tag[] tagsForMissingWord) {
        super(treeBankReader);
        this.tagForMissingWordArray = tagsForMissingWord;
        initFstTagBigrams();
    }

    @Override
    protected void initProbs(TreeBankReader treeBankReader) throws Exception {

        wordGivenPoSTagProbs= new HashMap<>();
        biGramProbs = new HashMap<>();
        fstTagBigrams = new HashMap<>();

        HashMap<Tag, Long>  tagNums = treeBankReader.getTagNums();
        HashMap<PoSTag, Long>  poSTagsNums = treeBankReader.getPoSTagNums();
        HashMap<BiGram, Long>  biGramSNums = treeBankReader.getBigramNums();

        if(treeBankReader.getBigramNums().isEmpty())
            throw new Exception("BiGrams counters is empty");

        for(BiGram biGram : biGramSNums.keySet())
            biGramProbs.put(biGram, (double) biGramSNums.get(biGram) / (double) tagNums.get(biGram.getSndTag()));


        for(PoSTag poSTag : poSTagsNums.keySet())
            wordGivenPoSTagProbs.put(poSTag,(double) poSTagsNums.get(poSTag)/(double)tagNums.get(poSTag.getTag()));

    }

    private void initFstTagBigrams(){
        for(Tag tag : Tag.values()){
            fstTagBigrams.put(tag,new HashSet<>());
            for(BiGram biGram : biGramProbs.keySet())
                if(tag.equals(biGram.getFstTag()))
                    fstTagBigrams.get(tag).add(biGram);
        }
    }

    @Override
    public Sentence poSTagging(String[] sentenceString) {
        Sentence sentence = new Sentence();
        double viterbi[][] = new double[Tag.values().length][sentenceString.length+2];
        int backpointer[][] = new int[Tag.values().length][sentenceString.length+2];
        double newScore;
        int indexTag;
        HashSet<Tag> tagForMissingWord = new HashSet<>(Arrays.asList(tagForMissingWordArray));
        int tagForMissingWordSize = tagForMissingWord.size();
        PoSTag poSTag;
        for (int i = 0; i < viterbi.length; i++) {
            for (int j = 0; j < viterbi[0].length; j++) {
                viterbi[i][j] = Double.NEGATIVE_INFINITY;

            }
        }
        viterbi[0][0]=0;


        for (int t = 0; t < sentenceString.length; t++) {
            for (int s = 0; s < Tag.values().length; s++) {

                for(BiGram biGram : fstTagBigrams.get(Tag.values()[s])){
                        Tag tag = biGram.getSndTag();
                        indexTag = Arrays.asList(Tag.values()).indexOf(tag);
                        if (!wordGivenPoSTagProbs.keySet().contains(new PoSTag(sentenceString[t], tag))) {
                            if (!words.contains(sentenceString[t]) && tagForMissingWord.contains(tag)) {
                                newScore = viterbi[s][t]
                                        + Math.log(biGramProbs.get(biGram))
                                        + Math.log((double) 1 / (double) tagForMissingWordSize);
                            } else
                                newScore = Double.NEGATIVE_INFINITY;
                        } else {
                            poSTag = new PoSTag(sentenceString[t], tag);

                            newScore = viterbi[s][t]
                                    + Math.log(biGramProbs.get(biGram))
                                    + Math.log(wordGivenPoSTagProbs.get(poSTag));
                        }
                        if (viterbi[indexTag][t + 1] == Double.NEGATIVE_INFINITY || newScore > viterbi[indexTag][t + 1]) {
                            viterbi[indexTag][t + 1] = newScore;
                            backpointer[indexTag][t + 1] = s;
                        }
                }
            }
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
            for (int j = backpointer[0].length-2; j > 0; j--) {
                sentence.getPoSTags().add(0,new PoSTag(sentenceString[j-1],Tag.values()[indexMax]));
                indexMax = backpointer[indexMax][j];
            }

        return sentence;
    }


    public HashMap<PoSTag, Double> getWordGivenPoSTagProbs() {
        return wordGivenPoSTagProbs;
    }

    public HashMap<BiGram, Double> getBiGramProbs() {
        return biGramProbs;
    }
}
