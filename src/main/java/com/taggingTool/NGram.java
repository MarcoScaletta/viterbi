package com.taggingTool;


import java.io.Serializable;
import java.util.*;


public abstract class NGram extends Counter implements Serializable{

    private final Tag tags[];
    private Double prob;

    public NGram(Tag[] afterTag) {
        prob = (double) 0;
        tags = afterTag;
    }

    public Tag[] getTags() {
        return tags;
    }

    public Double getProb() {
        return prob;
    }

    public void setProb(Double prob) {
        this.prob = prob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NGram ngram = (NGram) o;
        return Arrays.equals(tags, ngram.tags);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tags);
    }

    @Override
    public String toString() {
        return "NGram{" +
                "tags=" + Arrays.toString(tags) +
                ", prob=" + prob +
                ", num=" + getNum() +
                '}';
    }

    public String lazyToString(){
        String ret = "";
        for(Tag t : tags){

            ret= ret + " "+t.lazyToString();
        }
        return ret;
    }
}
