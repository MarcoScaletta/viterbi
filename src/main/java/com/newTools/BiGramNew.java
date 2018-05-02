package com.newTools;

import java.io.Serializable;

public class BiGramNew extends NGramNew implements Serializable{

    public BiGramNew(TagEnum tag1, TagEnum tag2){
        super(new TagEnum[] {tag1, tag2});
    }
}
