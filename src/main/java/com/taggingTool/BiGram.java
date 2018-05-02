package com.taggingTool;

import java.io.Serializable;

public class BiGram extends NGram implements Serializable{

    public BiGram(Tag tag1, Tag tag2){
        super(new Tag[] {tag1, tag2});
    }
}
