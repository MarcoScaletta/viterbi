package com.tester;

import com.poSTagger.PoSTagger;
import com.taggingTool.Sentence;
import com.treeBankReader.TreeBankSentenceReader;
import com.utilities.Log;
import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.List;

public class PoSTaggerTester extends AccuracyTester{


    public static double testPoSTagger(PoSTagger poSTagger, String fileTestingPath){
        long total;
        long partial = 0;
        float time;
        long numWord = 0;

        Log.log("TestPoSTagger ["+poSTagger.getClass()+ "] started");
        StopWatch stopWatch = StopWatch.createStarted();
        List<Sentence> correctLists = TreeBankSentenceReader.readSentence(fileTestingPath);
        List<Sentence> tpTestLists = new ArrayList<>();
        List<List <String>> stringSentences = Sentence.toWordsMoreSentences(correctLists);

        total = correctLists.size();
        for(List <String> strings : stringSentences){
            tpTestLists.add(poSTagger.poSTagging(strings.toArray(new String [strings.size()])));
            partial++;
            numWord += strings.size();
            Log.overLog("Tagged " + partial+"/"+total+ " sentences");
        }
        Log.emptyLog();
        stopWatch.stop();
        time = (float)stopWatch.getTime()/(float)1000;

        Log.log("TestPoSTagger ["+poSTagger.getClass()+ "] finished in " + time + "seconds. Avg time per words: " + time/(float)numWord);


        try {
            return accuracyMultipleSentence(correctLists, tpTestLists);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



}
