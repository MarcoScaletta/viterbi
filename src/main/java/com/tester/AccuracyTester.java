package com.tester;

import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;
import com.taggingTool.Tag;

import java.util.HashMap;
import java.util.List;

/**
 * Implements method to test the accuracy of PoS tagging
 */
public class AccuracyTester {
    /**
     *
     * @param correctLists list of correct Sentence objects for the test
     * @param toTestLists list of Sentence objects to be tested
     * @return the accuracy of assignments
     * @throws Exception when the lists are empty or the lists have different size
     */
    public static double accuracyMultipleSentence(List<Sentence> correctLists, List<Sentence> toTestLists)
            throws Exception {

        double correct = 0;
        double totalPoSTag = 0;
        if(correctLists.size() != toTestLists.size() || correctLists.size() < 1)
            throw new Exception("error lists size:"
                    + "size of correctLists is "+correctLists.size()+"; size of toTestLists is "+ toTestLists.size());
        for (int i = 0; i < correctLists.size(); i++) {
            correct += correctPoSTags(correctLists.get(i).getPoSTags(), toTestLists.get(i).getPoSTags());
            totalPoSTag += correctLists.get(i).getPoSTags().size();
        }

        return correct/totalPoSTag;
    }

    /**
     *
     * @param correctSentence correct Sentence object for the test
     * @param toTestSentence Sentence object to be tested
     * @return the accuracy of assignments
     * @throws Exception if the Sentence objects haven't same size or are empty
     */
    public static double accuracySingleSentence(Sentence correctSentence, Sentence toTestSentence)
            throws Exception {
        List<PoSTag> poSTagsCorrect = correctSentence.getPoSTags();
        List<PoSTag> poSTagsToTest = toTestSentence.getPoSTags();
        if(poSTagsCorrect.size() != poSTagsToTest.size() || poSTagsCorrect.size() < 1)
            throw new Exception("error lists size: "
                + "size of poSTagsCorrect is " + poSTagsCorrect.size()
                + "; size of poSTagsToTest is " + poSTagsToTest.size());

        double correct = (double) correctPoSTags(poSTagsCorrect, poSTagsToTest);
        double total = (double)poSTagsCorrect.size();

        return correct/(total);
    }

    /**
     *
     * @param correctList list of correct assignment word-tag
     * @param toTestList list of assignment word-tag to be tested
     * @return number of correct assignment in toTestList
     * @throws Exception if the lists haven't same size or are empty
     */
    public static int correctPoSTags(List<PoSTag> correctList, List<PoSTag> toTestList) throws Exception {
        int correct = 0;
        PoSTag correctPT;
        PoSTag toTestPT;
        if(correctList.size() != toTestList.size() || correctList.size() < 1)
            throw new Exception("lists have to have same size");
        for (int i = 0; i < correctList.size(); i++) {
            correctPT = correctList.get(i);
            toTestPT = toTestList.get(i);
            if(!correctPT.getWord().toLowerCase().equals(toTestPT.getWord().toLowerCase()))
                throw new Exception("list have to contain same words");
            if(correctPT.getTag().equals(toTestPT.getTag()))
                correct++;
        }
        return correct;

    }
}
