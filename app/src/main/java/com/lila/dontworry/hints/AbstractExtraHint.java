package com.lila.dontworry.hints;

/**
 * Created by Lidia on 13.11.2017.
 */

public abstract class AbstractExtraHint extends AbstractHint{
    private String hintString2;
    private int counter;
    private String name;

    public String getHintString2(){
        return this.hintString2;
    }

    public void setHintString2(String n){
        this.hintString2 = n;
    }

    public int getCounter(){
        return this.counter;
    }

    public void setCounter(int n){
        this.counter = n;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String n){
        this.name = n;
    }
}
