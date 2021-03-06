package com.treeBankReader;

import com.taggingTool.PoSTag;
import com.taggingTool.Sentence;
import com.taggingTool.Tag;
import com.utilities.UtilitiesIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a method to read a list of Sentence objects from a TreeBank
 */
public class TreeBankSentenceReader {

    /**
     * Reads and return a list of Sentence objects from a TreeBank
     * @param filePath the TreeBank file
     * @return the list of Sentence objects
     */
    public static List<Sentence>  readSentence(String filePath, boolean upperCase){
        List<Sentence> poSTagList = new ArrayList<>();
        FileReader fr = null;
        BufferedReader br = null;
        Integer numRow;
        String currentLine;
        String[] columns;

        Sentence sentenceTemp = new Sentence();

        try{
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);
            while((currentLine = br.readLine()) != null) {
                columns = currentLine.split("\t");
                numRow = TreeBankReader.getIntegerString(columns[0]);
                if (numRow != null && numRow >= 1) {
                    if (numRow == 1)
                        sentenceTemp = new Sentence();
                    //sentenceTemp.getPoSTags().add(new PoSTag(columns[1], Tag.valueOf(columns[3])));

                    if (!upperCase)
                        sentenceTemp.getPoSTags().add(new PoSTag(columns[1].toLowerCase(), Tag.valueOf(columns[3])));
                    else
                        sentenceTemp.getPoSTags().add(new PoSTag(columns[1], Tag.valueOf(columns[3])));

                }else if(columns[0].equals(""))
                    poSTagList.add(sentenceTemp);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            UtilitiesIO.closeFile(fr,br);
        }
        return poSTagList;
    }
}
