package com.tester;

import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;

import java.util.List;

public class AccuracyTester {

    public static double accuracyMultipleSentence(List<Sentence> correctLists, List<Sentence> toTestLists)
            throws Exception {

        double correct = 0;
        double totalPoSTag = 0;
        if(correctLists.size() != toTestLists.size() || correctLists.size() < 1)
            throw new Exception("lists have to have same size"
                    + "correctLists.size() "+correctLists.size()+" toTestLists.size(): "+ toTestLists.size());
        for (int i = 0; i < correctLists.size(); i++) {
            correct += correctPoSTags(correctLists.get(i).getPoSTags(), toTestLists.get(i).getPoSTags());
            totalPoSTag += correctLists.get(i).getPoSTags().size();
        }

        return correct/totalPoSTag;
    }

    public static double accuracySingleSentence(List<PoSTag> correctList, List<PoSTag> toTestList)
            throws Exception {

        double correct = (double) correctPoSTags(correctList, toTestList);
        double total = (double)correctList.size();

        return correct/(total);
    }

    public static int correctPoSTags(List<PoSTag> correctList, List<PoSTag> toTestList) throws Exception {
        int correct = 0;
        PoSTag correctPT;
        PoSTag toTestPT;

        if(correctList.size() != toTestList.size() || correctList.size() < 1)
            throw new Exception("lists have to have same size");
        for (int i = 0; i < correctList.size(); i++) {
            correctPT = correctList.get(i);
            toTestPT = toTestList.get(i);
            if(!correctPT.getWord().equals(toTestPT.getWord()))
                return 0;
            if(correctPT.getTag().equals(toTestPT.getTag()))
                correct++;
        }
        return correct;

    }
}
