package com.lila.dontworry.Logic;


/**
 * Created by Gustav on 25.11.2017.
 */

public class Question {
    private String text;
    private int id;
    private int answer;
    private boolean permanent = false;
    private DisplayObject displayObject;

    public Question(String text, int id, int answer) {
        this.text = text;
        this.id = id;
        this.answer = answer;
    }

    public Question(String text, int id, int answer, DisplayObject displayObject) {
        this.text = text;
        this.id = id;
        this.answer = answer;
        this.displayObject = displayObject;
    }

    public Question(String text) {
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

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public DisplayObject getDisplayObject() {
        return displayObject;
    }

    public void setDisplayObject(DisplayObject displayObject) {
        this.displayObject = displayObject;
    }

    public String createText() {
        String createdText = "";

        String objectContent = (displayObject == null) ? "" : displayObject.getContent();
        createdText = String.format(getText(), objectContent);
        /*
        switch (getType()) {
            case LINK:
                createdText = getText();
                break;
            case PLACE:
                createdText = String.format(getText(), displayObject.getContent());
                break;
            case CONTACT:
                createdText = String.format(getText(), displayObject.getContent());
                break;
            default:
                createdText = getText();
                break;
        }
        */
        return createdText;
    }

    @Override
    public String toString() {
        return "Question " + text + " " + id + " " + answer + " " + (permanent ? "permanent":"not permanent");
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
        return new Question("Keine Frage vorhanden.", Integer.MAX_VALUE, 0 );
    }

    private ObjectType getType() {
        if (displayObject == null)
            return ObjectType.EMPTY;
        return displayObject.getObjectType();
    }
}
