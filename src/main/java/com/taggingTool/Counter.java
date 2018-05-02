package com.taggingTool;

import java.io.Serializable;

public class Counter implements Serializable{

    private long num = 0;

    public void incNum(){
        ++num;
    }
    public void decNum(){
        --num;
    }
    public long getNum() {
        return num;
    }
}
