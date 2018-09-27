package com.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Implements utilities for IO operations
 */
public class UtilitiesIO {
    /**
     * Closes safely a file a FileReader and a BufferReader
     * @param fr the FileReader object to be closed
     * @param br the BufferReader object to be closed
     */
    public static void closeFile(FileReader fr, BufferedReader br){
        try{
            if(fr!=null)
                fr.close();
            if(br!=null)
                br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
