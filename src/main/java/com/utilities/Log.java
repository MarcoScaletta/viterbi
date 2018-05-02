package com.utilities;

import java.time.LocalDateTime;

public class Log {

    public static void emptyLog(){
        System.out.println();
    }

    public static void log(String log){
        System.out.println("["+LocalDateTime.now()+"] " + "LOG: " + log);
    }

    public static void overLog(String log){
        System.out.print("\r"+"["+LocalDateTime.now()+"] " + "LOG: " + log);
    }
}
