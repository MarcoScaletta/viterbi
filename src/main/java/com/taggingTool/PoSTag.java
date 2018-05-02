package com.taggingTool;

import java.io.Serializable;
import java.util.Objects;

public class PoSTag extends Counter implements Serializable{
    private String word;
    private Tag tag;
    private Double prob;
    private Double probFin;


    public PoSTag(String word, Tag tag) {
        this.word = word;
        this.tag = tag;
    }

    public PoSTag(String word, Tag tag, Double prob) {
        this.word = word;
        this.tag = tag;
        this.prob = prob;
    }

    public Double getProbFin() {
        return probFin;
    }

    public void setProbFin(Double probFin) {
        this.probFin = probFin;
    }

    public Double getProb() {
        return prob;
    }

    public void setProb(Double prob) {
        this.prob = prob;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoSTag poSTag = (PoSTag) o;
        return Objects.equals(word, poSTag.word) &&
                Objects.equals(tag, poSTag.tag);
    }


    public boolean equalsByWord(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoSTag poSTag = (PoSTag) o;
        return Objects.equals(word, poSTag.word);
    }

    @Override
    public int hashCode() {

        return Objects.hash(word, tag);
    }

    @Override
    public String toString() {
        return "PoSTag{" +
                "word='" + word + '\'' +
                ", tag='" + tag + '\'' +
                ", prob=" + prob +
                ", num=" + getNum() +
                '}';
    }

    public String lazyToString(){
        return "["+word+" "+ ((tag==null)?"NULL":tag.lazyToString())+"]";
    }
}
