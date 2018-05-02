package com.poSTagger;

import com.treeBankReader.FileReaderPoSTagging;
import com.utilities.Log;
import com.taggingTool.Sentence;
import com.taggingTool.BiGram;
import com.taggingTool.PoSTag;
import com.taggingTool.Tag;

import java.util.List;
import java.util.ArrayList;

public class Viterbi extends PoSTagger{

    public Viterbi(FileReaderPoSTagging training, FileReaderPoSTagging testing) {
        super(training, testing);
    }

    @Override
    public Sentence poSTagging(Sentence sentence){
        Sentence res = new Sentence();
        List<PoSTag> poSTagList = res.getPoSTags();

        List<String> wordsSentence = new ArrayList();
        for(PoSTag p : sentence.getPoSTags())
            wordsSentence.add(p.getWord());
        int sentenceLength = sentence.getPoSTags().size();
        double viterbi[][] = new double[training.getNumTags()][sentenceLength+2];
        int backpointer[][] = new int[training.getNumTags()][sentenceLength+2];
        double newScore;
        String newTagString;
        viterbi[0][0]=0;
        for (int i = 1; i < viterbi.length; i++) {
            viterbi[i][0] = Double.NEGATIVE_INFINITY;
            viterbi[0][viterbi[0].length-1] = Double.NEGATIVE_INFINITY;
        }
        for (int i = 1; i < viterbi[0].length; i++) {
            viterbi[0][i] = Double.NEGATIVE_INFINITY;
        }

        for (int t = 0; t < sentenceLength; t++) {
            if(!training.getWords().containsKey(wordsSentence.get(t))){
                String wordToAdd [] = {"NOUN", "VERB", "ADJ"};
                for(String s :wordToAdd){

                    PoSTag p = new PoSTag(wordsSentence.get(t), training.getSavedTag(s));

                    training.saveWord(wordsSentence.get(t));
                    training.savePoSTag(p);
                    double posTagNum = (double) p.getNum();
                    double tagNum = (double) p.getTag().getNum();
                    p.setProb((posTagNum/tagNum));
                    p.setProbFin((double)1/(double)wordToAdd.length);
                }
            }

            for (int s = 0; s < training.getNumTags(); s++) {
                //for(PoSTag p : training.getPoSTags())

                for(BiGram biGram : training.getBiGrams()){
                    if(biGram.getTags()[0].equals(training.getTags().get(s))) {
                        Tag tag = biGram.getTags()[1];
                        int indexTag = training.getTags().indexOf(tag);
                        if (!training.getPoSTags().contains(new PoSTag(wordsSentence.get(t), tag))) {
                            newScore = Double.NEGATIVE_INFINITY;
                        }else {
                            PoSTag poSTag = training.getPoSTags()
                                    .get(training.getPoSTags().indexOf(new PoSTag(wordsSentence.get(t), tag)));
                            //System.out.format("newScore %20s%30s%30s\n","viterbi["+s+"]["+t+"]","Math.log(biGram.getProb())", "Math.log(poSTag.getProb())");
                            //System.out.format("newScore %20s%30s%30s\n",viterbi[s][t],Math.log(biGram.getProb()), Math.log(poSTag.getProb()));


                            newScore = viterbi[s][t] + Math.log(biGram.getProb()) + Math.log(poSTag.getProb());

                        }
                        if(viterbi[indexTag][t+1]==0 || newScore>viterbi[indexTag][t+1]){
                            viterbi[indexTag][t+1] = newScore;
                            backpointer[indexTag][t+1]=s;
                        }

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
        }/*
        System.out.format("%40s","");
        for (int i = 1; i < viterbi[0].length-1; i++) {
            System.out.format("%30s",sentence.getPoSTag(i-1).getWord());
        }
        System.out.println();
        for (int i = 0; i < viterbi.length; i++) {
            System.out.format("%10s",training.getTags().get(i).lazyToString());
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

        }*/

        if(maxProb > Double.NEGATIVE_INFINITY)
            for (int j = backpointer[0].length-2 ; j > 0; j--) {
                poSTagList.add(0, new PoSTag(wordsSentence.get(j-1), training.getTags().get(indexMax)));
                indexMax = backpointer[indexMax][j];
            }
        else{
            Log.log("maxProb < Double.NEGATIVE_INFINITY");
            System.out.format("%40s","");
            for (int i = 1; i < viterbi[0].length-1; i++) {
                System.out.format("%30s",sentence.getPoSTag(i-1).getWord());
            }
            System.out.println();
            for (int i = 0; i < viterbi.length; i++) {
                System.out.format("%10s",training.getTags().get(i).lazyToString());
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
        }
        sentence.setPoSTags(poSTagList);
        return sentence;
    }

    public Sentence parallelPoSTagging(Sentence sentence){
        Sentence res = new Sentence();
        List<PoSTag> poSTagList = res.getPoSTags();

        List<String> wordsSentence = new ArrayList();
        for(PoSTag p : sentence.getPoSTags())
            wordsSentence.add(p.getWord());
        int sentenceLength = sentence.getPoSTags().size();
        double viterbi[][] = new double[training.getNumTags()][sentenceLength+2];
        int backpointer[][] = new int[training.getNumTags()][sentenceLength+2];
        double newScore;
        String newTagString;
        viterbi[0][0]=0;
        for (int i = 1; i < viterbi.length; i++) {
            viterbi[i][0] = Double.NEGATIVE_INFINITY;
            viterbi[0][viterbi[0].length-1] = Double.NEGATIVE_INFINITY;
        }
        for (int i = 1; i < viterbi[0].length; i++) {
            viterbi[0][i] = Double.NEGATIVE_INFINITY;
        }

        for (int t = 0; t < sentenceLength; t++) {
            if(!training.getWords().containsKey(wordsSentence.get(t))){
                /*if(t>0
                        && !wordsSentence.get(t-1).equals(".")
                        && !wordsSentence.get(t-1).equals("\"")
                        && Character.isUpperCase(wordsSentence.get(t).charAt(0))){
                    newTagString = "PROPN";
                }else*/
                String wordToAdd [] = {"NOUN", "VERB", "ADJ"};

                newTagString = "NOUN";
                for(String s :wordToAdd){
                    PoSTag p = new PoSTag(wordsSentence.get(t), training.getSavedTag(s));
                /*if(newTagString.equals("PROPN"))
                    Log.log("NEW PROPN: " + p.lazyToString());
                if(newTagString.equals("NOUN"))
                    Log.log("NEW NOUN: " + p.lazyToString());*/
                    training.saveWord(wordsSentence.get(t));
                    if(training.getWords().get(wordsSentence.get(t)) == null)
                        Log.log("INSERT DIDN'T WORK");
                    else
                        Log.log("training.getWords().get("
                                +wordsSentence.get(t)+") == " + training.getWords().get(wordsSentence.get(t)));
                    training.savePoSTag(p);
                    double posTagNum = (double) p.getNum();
                    double tagNum = (double) p.getTag().getNum();
                    p.setProb((posTagNum/tagNum));
                    p.setProbFin((double)1/(double)training.getWords().get(p.getWord()).size());
                }


                //Log.log(p.toString());
            }

            for (int s = 0; s < training.getNumTags()-2; s++) {
                //for(PoSTag p : training.getPoSTags())

                for(BiGram biGram : training.getBiGrams()){
                    if(biGram.getTags()[0].equals(training.getTags().get(s))) {
                        Tag tag = biGram.getTags()[1];
                        int indexTag = training.getTags().indexOf(tag);
                        if (!training.getPoSTags().contains(new PoSTag(wordsSentence.get(t), tag))) {
                            newScore = Double.NEGATIVE_INFINITY;
                        }else {
                            PoSTag poSTag = training.getPoSTags().get(training.getPoSTags().indexOf(new PoSTag(wordsSentence.get(t), tag)));


                            // old --> newScore = viterbi[s][t] + Math.log(biGram.getProb()) + Math.log(poSTag.getProb());
                            newScore = viterbi[s][t] + Math.log(biGram.getProb()) + Math.log(poSTag.getProbFin());

                        }
                        if(viterbi[indexTag][t+1]==0 || newScore>viterbi[indexTag][t+1]){
                            viterbi[indexTag][t+1] = newScore;
                            backpointer[indexTag][t+1]=s;
                        }

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
        System.out.format("%40s","");
        for (int i = 1; i < viterbi[0].length-1; i++) {
            System.out.format("%30s",sentence.getPoSTag(i-1).getWord());
        }
        System.out.println();
        for (int i = 0; i < viterbi.length; i++) {
            System.out.format("%10s",training.getTags().get(i).lazyToString());
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

        if(maxProb > Double.NEGATIVE_INFINITY)
            for (int j = backpointer[0].length-2 ; j > 0; j--) {
                poSTagList.add(0, new PoSTag(wordsSentence.get(j-1), training.getTags().get(indexMax)));
                indexMax = backpointer[indexMax][j];
            }
        else
            Log.log("maxProb < Double.NEGATIVE_INFINITY");
        sentence.setPoSTags(poSTagList);
        return sentence;
    }

}
