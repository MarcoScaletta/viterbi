package com.utilitiesIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UtilitiesIO {

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
