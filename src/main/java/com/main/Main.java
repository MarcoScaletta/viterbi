package com.main;



import com.poSTagger.PoSTagger;
import com.taggingTool.Tag;
import com.tester.*;
import com.poSTagger.Viterbi;
import com.treeBankReader.TreeBankReader;
import com.utilities.Log;
import org.apache.commons.lang3.time.StopWatch;

import java.io.FileNotFoundException;


public class Main {

    private static final String TREE_BANK_TRAINING = "training.txt";
    private static final String TREE_BANK_TESTING = "testing.txt";


    public static void main(String[] args) {
        accuracyTesting();
    }

    private static void accuracyTesting(){
        try {
            TreeBankReader treeBankReader = new TreeBankReader(TREE_BANK_TRAINING);
            Tag nounAdjVerbPropn [] = {Tag.NOUN, Tag.ADJ, Tag.PROPN, Tag.VERB} ;

            Viterbi viterbi = new Viterbi(treeBankReader,nounAdjVerbPropn);
            PoSTagger poSTagger = new PoSTagger(treeBankReader);

            Log.log("ACCURACY BASELINE "+ (float)PoSTaggerTester.testPoSTagger(poSTagger, TREE_BANK_TESTING)*100 + "%");
            Log.log("ACCURACY VITERBI "+ (float)PoSTaggerTester.testPoSTagger(viterbi, TREE_BANK_TESTING)*100 + "%");

        }catch(Exception e){
            e.printStackTrace();
        }
        }

}
