package com.utilities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriteObject {

    public static void writeObject(String path, Object objectToWrite){
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = new FileOutputStream(path);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(objectToWrite);
        } catch (IOException e) {
            e.printStackTrace();
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

        }
    }
}
