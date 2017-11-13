package com.lila.dontworry.hints;

/**
 * Created by Lidia on 13.11.2017.
 */

public class ExtraHint extends AbstractExtraHint {
    public ExtraHint(String code, String hintString, String hintString2, String name){
        setCode(code);
        setCounter(0);
        setHintString(hintString);
        setHintString2(hintString2);
        setName(name);
        System.out.println(hintString);
    }

    public ExtraHint(String code, String hintString, String hintString2){
        setCode(code);
        setCounter(0);
        setHintString(hintString);
        setHintString2(hintString2);
        setName(null);
        System.out.println(hintString );
    }
}
