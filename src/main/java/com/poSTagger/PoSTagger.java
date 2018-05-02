package com.poSTagger;

import com.treeBankReader.FileReaderPoSTagging;
import com.utilities.Log;
import com.taggingTool.Sentence;
import com.taggingTool.Counter;
import com.taggingTool.PoSTag;
import com.taggingTool.Tag;
import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class PoSTagger {
    protected FileReaderPoSTagging training;
    private FileReaderPoSTagging testing;

    public PoSTagger(FileReaderPoSTagging training, FileReaderPoSTagging testing) {
        this.training = training;
        this.testing = testing;
    }

    public Sentence poSTagging(Sentence sentenceToTag){
        double maxProb;
        Tag tag;
        for(PoSTag sentencePT : sentenceToTag.getPoSTags()){
            maxProb = 0;
            tag = new Tag("NOUN");
            for(PoSTag trainingPT : training.getPoSTags())
                if(sentencePT.equalsByWord(trainingPT) && maxProb < trainingPT.getProb()){
                    //maxProb = trainingPT.getProb();
                    maxProb = trainingPT.getProb();
                    tag = trainingPT.getTag();
                }
            sentencePT.setTag(tag);
        }
        return sentenceToTag;
    }

    public double accuracy(Sentence s) {
        double accuracy = 0;
        double accuracyMax = 0;
        int num = 0;
        int correct =0;
        int sNum = 0;
        if(testing.getSentences().contains(s)){
            return 1;
        }
        for(Sentence sentence : testing.getSentences()){
            if(s == null)
                System.out.println("SENTENCE NULL");

            if(Objects.requireNonNull(s).equalsByWord(sentence)){
                for (int i = 0; i < s.getPoSTags().size(); i++) {

                    if(s.getPoSTags().get(i).equals(sentence.getPoSTags().get(i)))
                        correct++;
                    accuracyMax = ((double)(correct))/((double)(s.getPoSTags().size()));
                }
            }

        }
        return accuracyMax;
    }

    private double accuracy(Sentence s, Sentence testing) {
        int correct =0;
        if(testing.equals(s)){
            if(testing == s)
                Log.log("ERRORE");
            return 1;
        }

        if(s == null)
            System.out.println("SENTENCE NULL");
        if(s.equalsByWord(testing)){
            for (int i = 0; i < s.getPoSTags().size(); i++) {
                if(s.getPoSTags().get(i).equals(testing.getPoSTags().get(i)))
                    correct++;
                /* else if(s.getPoSTags().get(i).equals(new PoSTag(s.getPoSTags().get(i).getWord(), new Tag("PROPN")))){
                    Log.log("WRONG PROPN: " + s.getPoSTags().get(i).lazyToString());
                }else if(s.getPoSTags().get(i).equals(new PoSTag(s.getPoSTags().get(i).getWord(), new Tag("NOUN")))){
                    Log.log("WRONG NOUN: " + s.lazyToString());
                }*/
            }
        }
        if(Double.isNaN(((double)(correct))/((double)(s.getPoSTags().size())))){
            Log.log("NaN: correct: " + correct+", sizeList"+s.getPoSTags().size());
        }


        return ((double)(correct))/((double)(s.getPoSTags().size()));
    }

    public double accuracy(){
        double temp = 0;
        Sentence tempSentence;
        long numSentence = testing.getSentences().size();
        long numDone = 0;
        long numWord = 0;
        long totWord = 0;
        float timeToFinish;
        float avgTime;

        for(Sentence sentence : testing.getSentences()){
            totWord += sentence.getPoSTags().size();
        }
        Log.log("start accuracy ("+ this.getClass() + ")");
        StopWatch stopWatch = StopWatch.createStarted();

        final List<Double> listAccuracy = new ArrayList<>();
        for(Sentence sentence : testing.getSentences()){

            numWord += sentence.getPoSTags().size();

            tempSentence = sentence.cloneSentence();
            for(PoSTag p : tempSentence.getPoSTags())
                p.setTag(null);
            poSTagging(tempSentence);
            listAccuracy.add(accuracy(tempSentence,sentence));
            avgTime = (float)stopWatch.getTime()/(1000*numWord);
            timeToFinish = (float)(totWord-numWord) * avgTime;
            Log.overLog(+ ++numDone+"/"+numSentence +
                    ", avg time for word: "+ avgTime+" seconds"+
                    ", remain "+ (totWord-numWord) +" words"+
                    ", finish in: "+ timeToFinish+" seconds");
        }
        for(Double d : listAccuracy)
            temp+= d;
        Log.emptyLog();
        Log.log("LIST ACCURACY SIZE: " + listAccuracy.size());
        Log.log("ACCURACY ON " +testing.getSentences().size() + " SENTENCES : "+(double)stopWatch.getTime()/1000+" seconds");
        return temp/(double)testing.getSentences().size();
    }

    public double accuracyParallel(){
        StopWatch stopWatch = StopWatch.createStarted();
        Log.log("start accuracy ("+ this.getClass() + ")");
        double temp = 0;
        long numSentence = testing.getSentences().size();

        Counter c = new Counter();
        final List<Double> listAccuracy = new ArrayList<>();
        testing.getSentences().parallelStream().forEach(sentence -> {
                    c.incNum();
                    Log.overLog(c.getNum()+"/"+numSentence);
                    Sentence tempSentence1 = sentence.cloneSentence();
                    for(PoSTag p : tempSentence1.getPoSTags())
                        p.setTag(null);
                    if(! (tempSentence1.getPoSTags().size() > 0))
                        System.out.println("before postagging size == 0");
                    poSTagging(tempSentence1);
                    if(! (tempSentence1.getPoSTags().size() > 0))
                        System.out.println("after postagging size == 0");
                    double d = accuracy(tempSentence1,sentence);
                    synchronized(this) {
                        //int limit = 2;
                        if(!Double.isNaN(d))
                            listAccuracy.add(d);/*
                            Log.log("NaN: " + sentence.lazyToString());
                        while(!(d>0) && limit-- > 0){
                            poSTagging(tempSentence1);
                            d = accuracy(tempSentence1,sentence);
                        }*/
                    }
                }
        );
        for(Double d : listAccuracy)
            temp+= d;
        Log.emptyLog();
        Log.log("ACCURACY_PARALLEL ON " +listAccuracy.size() + " SENTENCES : "+(double)stopWatch.getTime()/1000+" seconds");
        return temp/(double)testing.getSentences().size();
    }
}
