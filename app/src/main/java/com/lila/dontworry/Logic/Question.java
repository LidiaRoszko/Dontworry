package com.lila.dontworry.Logic;


/**
 * Created by Gustav on 25.11.2017.
 */

public class Question {
    private String text;
    private int id;
    private boolean answer;
    DisplayObject displayObject;

    public Question(String text, int id, boolean answer) {
        this.text = text;
        this.id = id;
        this.answer = answer;
    }

    public Question(String text, int id, boolean answer, DisplayObject displayObject) {
        this.text = text;
        this.id = id;
        this.answer = answer;
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

    @Override
    public String toString() {
        return "Question " + text + " " + id + " " + answer;
    }

    public boolean equals(Question question) {
        boolean equal = true;
        if (question.getText() != getText())
            equal = false;
        if (question.getId() != getId())
            equal = false;
        //if (question.isAnswer() != isAnswer())
        //    equal = false;
        return equal;
    }

    public static Question getDefault() {
        return new Question("Keine Frage vorhanden.", Integer.MAX_VALUE, false );
    }
}
