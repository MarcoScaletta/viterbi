package com.newTools;

import java.io.Serializable;
import java.util.Objects;

public class PoSTagNew implements Serializable {
    private String word;
    private TagEnum tag;

    public PoSTagNew(String word, TagEnum tag) {
        this.word = word;
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoSTagNew poSTagNew = (PoSTagNew) o;
        return Objects.equals(word, poSTagNew.word) &&
                tag == poSTagNew.tag;
    }

    @Override
    public int hashCode() {

        return Objects.hash(word, tag);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public TagEnum getTag() {
        return tag;
    }

    public void setTag(TagEnum tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "PoSTagNew{" +
                "word='" + word + '\'' +
                ", tag=" + tag +
                '}';
    }
}
