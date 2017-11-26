package com.lila.dontworry.Logic;

/**
 * Created by Gustav on 25.11.2017.
 */

public class Question {
    private String text;
    private int id;
    private boolean answer;
    DisplayObject displayObject;

    public Question(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public Question(String text, int id, DisplayObject displayObject) {
        this.text = text;
        this.id = id;
        this.displayObject = displayObject;
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

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public DisplayObject getDisplayObject() {
        return displayObject;
    }

    public void setDisplayObject(DisplayObject displayObject) {
        this.displayObject = displayObject;
    }
}
