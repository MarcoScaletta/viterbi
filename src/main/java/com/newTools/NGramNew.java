package com.newTools;

import java.util.Arrays;

public abstract class NGramNew {

    private final TagEnum tags[];

    protected NGramNew(TagEnum[] tags) {
        this.tags = tags;
    }

    public TagEnum[] getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NGramNew ngram = (NGramNew) o;
        return Arrays.equals(tags, ngram.tags);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tags);
    }
}
