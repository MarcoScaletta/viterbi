package com.newTools;

import com.taggingTool.PoSTag;
import com.utilities.Log;

import java.util.List;

public class AccuracyTester {

    public static double accuracySingleList(List<PoSTagNew> correctList, List<PoSTagNew> toTestList){
        double correct = 0;
        PoSTagNew correctPT;
        PoSTagNew toTestPT;
        if(correctList.size() != toTestList.size() || correctList.size() < 1)
            return 0;
        for (int i = 0; i < correctList.size(); i++) {
            correctPT = correctList.get(i);
            toTestPT = toTestList.get(i);
            if(!correctPT.getWord().equals(toTestPT.getWord()))
                return 0;
            if(correctPT.getTag().equals(toTestPT.getTag()))
                correct++;
        }

        Log.log("correct: "+correct);
        return correct/(double)correctList.size();
    }
}
