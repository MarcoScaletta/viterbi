package com.utilities;

import java.io.*;

public class ReadObject {

    public static Object readObject(String path){
        FileInputStream fout = null;
        ObjectInputStream oos = null;
        Object object = null;
        try {
            fout = new FileInputStream(path);
            oos = new ObjectInputStream(fout);
            object = oos.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {

            if (fout != null)
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            if (oos != null)
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return object;
        }
    }
}
