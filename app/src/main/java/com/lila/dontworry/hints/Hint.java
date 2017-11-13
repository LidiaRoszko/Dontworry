package com.lila.dontworry.hints;

/**
 * Created by Lidia on 13.11.2017.
 */

public class Hint extends AbstractHint{
    public Hint(String code){
        setHintString("What about "+code+"?");
        setCode(code);
        System.out.println(getHintString() + getCode());
    }

    public Hint(String code, String hintString){
        setHintString(hintString);
        setCode(code);
        System.out.println(hintString + getCode());
    }
}
