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
}
