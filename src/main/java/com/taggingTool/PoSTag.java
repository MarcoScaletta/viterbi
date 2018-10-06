package com.taggingTool;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the assignment of a tag to a word
 */
public class PoSTag implements Serializable {
    private String word;
    private Tag tag;

    public PoSTag(String word, Tag tag) {
        this.word = word;
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoSTag poSTag = (PoSTag) o;
        return Objects.equals(word, poSTag.word) &&
                tag == poSTag.tag;
    }

    @Override
    public int hashCode() {

        return Objects.hash(word, tag);
    }

    public String getWord() {
        return word;
    }

    public Tag getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "[" +
                word +
                "|" + tag +
                ']';
    }
}
