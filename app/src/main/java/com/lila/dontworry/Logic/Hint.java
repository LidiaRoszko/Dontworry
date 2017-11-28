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
        return "Hint " + text + " " + id;
    }

    public boolean equals(Hint hint) {
        boolean equal = true;
        if (hint.getText().equals(getText()))
            equal = false;
        if (hint.getId() != getId())
            equal = false;
        //if (question.isAnswer() != isAnswer())
        //    equal = false;
        return equal;
    }

    private ObjectType getType() {
        if (displayObject == null)
            return ObjectType.EMPTY;
        return displayObject.getObjectType();
    }

    static Hint getDefault() {
        return new Hint("Kein Tipp vorhanden.", Integer.MAX_VALUE );
    }
}
