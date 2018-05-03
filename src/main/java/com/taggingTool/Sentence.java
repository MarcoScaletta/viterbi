package com.taggingTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sentence {
    private final List<PoSTag> poSTags = new ArrayList<>();

    public Sentence() {
    }

    public List<PoSTag> getPoSTags() {
        return poSTags;
    }

    public static List<String> toWordsSingleSentence(Sentence sentence){
        List<String> list = new ArrayList<>();
        for(PoSTag poSTag : sentence.getPoSTags())
            list.add(poSTag.getWord());
        return list;
    }

    public static List<List<String>> toWordsMoreSentences(List<Sentence> sentences){
        List<List<String>> list = new ArrayList<>();
        for(Sentence sentence : sentences)
            list.add(toWordsSingleSentence(sentence));
        return list;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sentence sentence = (Sentence) o;
        return Objects.equals(poSTags, sentence.poSTags);
    }

    @Override
    public int hashCode() {

        return Objects.hash(poSTags);
    }

}
