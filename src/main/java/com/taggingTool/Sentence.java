package com.taggingTool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sentence implements Serializable, Cloneable{
    private List<PoSTag> poSTags = new ArrayList<>();

    public Sentence() {
    }

    public Sentence(String [] words){
        for(String s : words)
            poSTags.add(new PoSTag(s,null));
    }

    public Sentence(List<PoSTag> poSTags) {
        this.poSTags = poSTags;
    }


    public List<PoSTag> getPoSTags() {
        return poSTags;
    }

    public boolean equalsByWord(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sentence sentence = (Sentence) o;
        if(!(sentence.poSTags.size() == poSTags.size()))
            return false;
        for (int i = 0; i < poSTags.size(); i++) {
            if(!sentence.poSTags.get(i).equalsByWord(poSTags.get(i)))
                return false;
        }
        return true;
    }

    public PoSTag getPoSTag(int i){
        return poSTags.get(i);
    }

    public void setPoSTags(List<PoSTag> poSTags) {
        this.poSTags = poSTags;
    }

    public Sentence cloneSentence(){
        Sentence s = new Sentence();
        Tag t;
        for(PoSTag p : this.getPoSTags()){
            t = (p.getTag()==null)?null:new Tag(p.getTag().getTag());
            s.getPoSTags().add(new PoSTag(p.getWord(), t));
        }
        return s;
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

    public String lazyToString(){
        String res = "";
        for(PoSTag p : poSTags)
            res = res + p.lazyToString();
        return res;

    }

    public String toStringByWord(){
        String res = "";
        for(PoSTag p : poSTags)
            res= res + p.getWord() + " ";
        return res;
    }
}


