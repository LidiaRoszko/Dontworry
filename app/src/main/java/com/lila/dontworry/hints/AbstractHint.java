package com.lila.dontworry.hints;

/**
 * Created by Lidia on 13.11.2017.
 */

public abstract class AbstractHint {
    private String code;
    private String hintString;

    public String getHintString(){
        return this.hintString;
    }

    public void setHintString(String n){
        this.hintString = n;
    }

    public String getCode(){
        return this.code;
    }

    public void setCode(String n){
        this.code = n;
    }
}
