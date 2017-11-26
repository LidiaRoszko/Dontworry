package com.lila.dontworry.Logic;

/**
 * Created by Gustav on 25.11.2017.
 */

public class Hint {
    private String text;
    private int id;
    DisplayObject displayObject;

    public Hint(String text, int id, DisplayObject displayObject) {
        this.text = text;
        this.id = id;
        this.displayObject = displayObject;
    }

    public Hint(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public Hint(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DisplayObject getDisplayObject() {
        return displayObject;
    }

    public void setDisplayObject(DisplayObject displayObject) {
        this.displayObject = displayObject;
    }


    @Override
    public String toString() {
        return "Hint " + text + " " + id;
    }

    public boolean equals(Hint hint) {
        boolean equal = true;
        if (hint.getText() != getText())
            equal = false;
        if (hint.getId() != getId())
            equal = false;
        //if (question.isAnswer() != isAnswer())
        //    equal = false;
        return equal;
    }

    public static Hint getDefault() {
        return new Hint("Kein Tipp vorhanden.", Integer.MAX_VALUE );
    }
}
