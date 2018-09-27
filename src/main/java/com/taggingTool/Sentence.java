package com.taggingTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a sentence as list of PoSTag
 */
public class Sentence {
    private final List<PoSTag> poSTags = new ArrayList<>();

    public Sentence() {
    }

    public List<PoSTag> getPoSTags() {
        return poSTags;
    }

    /**
     * Converts a Sentence object to a list of String
     * @param sentence the Sentence object to be converted into list of String
     * @return list of words contained in sentence
     */
    public static List<String> toWordsSingleSentence(Sentence sentence){
        List<String> list = new ArrayList<>();
        for(PoSTag poSTag : sentence.getPoSTags())
            list.add(poSTag.getWord());
        return list;
    }

    /**
     * Converts a List of Sentence to a List of String
     * @param sentences list of sentences to be converted into list of String
     * @return list of list of words contained in the Sentence objects of sentences
     */
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
