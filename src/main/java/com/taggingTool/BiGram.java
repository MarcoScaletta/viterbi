package com.taggingTool;

import java.util.Objects;

public class BiGram {

    private final Tag fstTag;
    private final Tag sndTag;

    public BiGram(Tag fstTag, Tag sndTag){
        this.fstTag = fstTag;
        this.sndTag = sndTag;
    }

    public Tag getFstTag() {
        return fstTag;
    }

    public Tag getSndTag() {
        return sndTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiGram biGram = (BiGram) o;
        return fstTag == biGram.fstTag &&
                sndTag == biGram.sndTag;
    }

    @Override
    public int hashCode() {

        return Objects.hash(fstTag, sndTag);
    }
}
