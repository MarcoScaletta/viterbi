package com.main;



import com.poSTagger.PoSTagger;
import com.poSTagger.Viterbi;
import com.taggingTool.Tag;
import com.tester.PoSTaggerTester;
import com.treeBankReader.TreeBankReader;
import com.utilities.Log;

import java.io.*;
import java.nio.file.Paths;

@SuppressWarnings( value = "Duplicates")
public class Main {

    private static final String TREE_BANK_TRAINING = "training.txt";
    private static final String TREE_BANK_TESTING = "testing.txt";


    public static void main(String[] args) {

        Log.emptyLog();
        accuracyTestingVersion1();
        Log.emptyLog();
        accuracyTestingVersion2();
        Log.emptyLog();
        accuracyTestingVersion3();
        Log.emptyLog();
        accuracyTestingVersion4();
        Log.emptyLog();
        accuracyTestingVersion5();
    }

    public static String examplePoSTagVersion1(String sentence){
        String result;

        TreeBankReader treeBankReader = loadTreeBankReader();
        Tag nounAdjVerbPropn [] = {Tag.NOUN, Tag.ADJ, Tag.PROPN, Tag.VERB} ;
        Viterbi viterbi = new Viterbi(treeBankReader,nounAdjVerbPropn);
        result =viterbi.poSTagging(sentence.split(" ")).getPoSTags().toString();
        Log.log(result);
        return result;
    }

    private static TreeBankReader loadTreeBankReader(){
        try {
            FileInputStream fi = new FileInputStream(new File("tree-bank-reader.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            TreeBankReader treeBankReader = (TreeBankReader) oi.readObject();


            oi.close();
            fi.close();
            return treeBankReader;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.out.println("I'm in " + Paths.get("").toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static void saveTreeBankReader(){
        TreeBankReader treeBankReader = new TreeBankReader(TREE_BANK_TRAINING);

        try {
            FileOutputStream f = new FileOutputStream(new File("tree-bank-reader.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(treeBankReader);

            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }



    }

    private static void accuracyTestingVersion1(){
        TreeBankReader treeBankReader = new TreeBankReader(TREE_BANK_TRAINING,1);

        int viterbiVersion = 1;
        Tag nounAdjVerbPropn [] = {Tag.NOUN, Tag.ADJ, Tag.PROPN, Tag.VERB} ;
        Viterbi viterbi = new Viterbi(treeBankReader,1,nounAdjVerbPropn);
        PoSTagger poSTagger = new PoSTagger(treeBankReader,1);
        Log.log("-----------Versione 1.: solo parole minuscole");

        accuracyTesting(viterbiVersion,viterbi,poSTagger);
    }

    private static void accuracyTestingVersion2(){
        int viterbiVersion = 2;
        TreeBankReader treeBankReader = new TreeBankReader(TREE_BANK_TRAINING,2);

        Tag nounAdjVerbPropn [] = {Tag.NOUN, Tag.ADJ, Tag.PROPN, Tag.VERB} ;
        Viterbi viterbi = new Viterbi(treeBankReader,viterbiVersion,nounAdjVerbPropn);
        PoSTagger poSTagger = new PoSTagger(treeBankReader,viterbiVersion);
        Log.log("-----------Versione 2. UPGRADE: ");
        Log.log("DIFFERENZE CON VERSIONE 1: " +
                "solo PROPN possono essere maiuscoli");

        accuracyTesting(viterbiVersion,viterbi,poSTagger);
    }



    private static void accuracyTestingVersion3(){
        int viterbiVersion = 3;
        TreeBankReader treeBankReader = new TreeBankReader(TREE_BANK_TRAINING,3);

        Tag nounAdjVerbPropn [] = {Tag.NOUN, Tag.ADJ, Tag.PROPN, Tag.VERB} ;
        Viterbi viterbi = new Viterbi(treeBankReader,viterbiVersion,nounAdjVerbPropn);
        PoSTagger poSTagger = new PoSTagger(treeBankReader,viterbiVersion);
        Log.log("-----------Versione 3. UPGRADE: tutte la parole che non sono PROPN ");
        Log.log("DIFFERENZE CON VERSIONE 1: " +
                "vengono inserite sia con la lettera maiuscola che con la lettera minuscola");

        accuracyTesting(viterbiVersion,viterbi,poSTagger);
    }


    private static void accuracyTestingVersion4(){
        int viterbiVersion = 3;
        TreeBankReader treeBankReader = new TreeBankReader(TREE_BANK_TRAINING,4);

        Tag nounAdjVerbPropn [] = {Tag.NOUN, Tag.ADJ, Tag.PROPN, Tag.VERB} ;
        Viterbi viterbi = new Viterbi(treeBankReader,viterbiVersion,nounAdjVerbPropn);
        PoSTagger poSTagger = new PoSTagger(treeBankReader,viterbiVersion);
        Log.log("-----------Versione 4. DEPRECATED (ANOMALIA FILE DI TEST) ");
        Log.log("DIFFERENZE CON VERSIONE 1: " +
                "tutte e sole le parole taggate come non PROPN vengono salvate 2 volte");

        accuracyTesting(viterbiVersion,viterbi,poSTagger);
    }
    private static void accuracyTestingVersion5(){
        int viterbiVersion = 3;
        TreeBankReader treeBankReader = new TreeBankReader(TREE_BANK_TRAINING,5);

        Tag nounAdjVerbPropn [] = {Tag.NOUN, Tag.ADJ, Tag.PROPN, Tag.VERB} ;
        Viterbi viterbi = new Viterbi(treeBankReader,viterbiVersion,nounAdjVerbPropn);
        PoSTagger poSTagger = new PoSTagger(treeBankReader,viterbiVersion);
        Log.log("-----------Versione 5.: DEPRECATED (ANOMALIA FILE DI TEST)");
        Log.log("DIFFERENZE CON VERSIONE 1: " +
                "alcune delle parole taggate come non PROPN vengono salvate 2 volte");

        accuracyTesting(viterbiVersion,viterbi,poSTagger);
    }
    private static void accuracyTesting(int viterbiVersion, Viterbi viterbi, PoSTagger poSTagger){
        try {

            Log.log("ACCURACY BASELINE "+ (float)PoSTaggerTester.testPoSTagger(poSTagger, TREE_BANK_TESTING,viterbiVersion!=1)*100 + "%");
            Log.log("ACCURACY VITERBI "+ (float)PoSTaggerTester.testPoSTagger(viterbi, TREE_BANK_TESTING,viterbiVersion!=1)*100 + "%");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
