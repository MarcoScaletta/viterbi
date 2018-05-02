package com.main;



import com.newTools.*;
import com.poSTagger.PoSTagger;
import com.poSTagger.Viterbi;
import com.poSTaggerNew.PoSTaggerNew;
import com.taggingTool.BiGram;
import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;
import com.taggingTool.Tag;
import com.treeBankReader.FileReaderPoSTagging;
import com.treeBankReader.TreeBankReader;
import com.utilities.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Main {

    private static final String DICT_ITA_ENG = "dict_ita_eng.txt";

    private static final String TREE_BANK_TEST_DEV = "test_dev.txt";
    private static final String TREE_BANK_DEV = "dev.txt";
    private static final String TREE_BANK_TRAINING = "training.txt"; //Per esempio , queste persone sono gli stranieri immigrati in Italia da poco tempo , che conoscono poco la lingua italiana .
    private static final String TREE_BANK_TESTING = "testing.txt";

    private static final String POS_TAGGER_TRAINING = "pos_tagger_training.ser";
    private static final String POS_TAGGER_TESTING = "pos_tagger_testing.ser";
    private static final String POS_TAGGER_DEV = "pos_tagger_dev.ser";
    private static final String POS_TAGGER_TEST_DEV = "pos_tagger_test_dev.ser";

    public static void main(String[] args) {


        TreeBankReader t = new TreeBankReader();
        t.fileToPoSTags(TREE_BANK_DEV, 2);


        ViterbiNew viterbiNew = new ViterbiNew(t);

        for(PoSTagNew poSTagNew : viterbiNew.poSTagging("Mariuzzo ha fatto".split(" "))) {
            Log.log(poSTagNew.getWord() + " " + poSTagNew.getTag());
        }
/**/




    }

    private static FileReaderPoSTagging readingFileIfNeeded(String treeBankFile, String objectFile){
        FileReaderPoSTagging taggerTraining = FileReaderPoSTagging.setSavedPoSTagger(treeBankFile,objectFile);
        if(taggerTraining == null){
            taggerTraining = new FileReaderPoSTagging();
            taggerTraining.updatePoSTagger(treeBankFile,objectFile);
        }
        return taggerTraining;
    }

}
