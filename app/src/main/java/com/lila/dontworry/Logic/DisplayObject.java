package com.lila.dontworry.Logic;

/**
 * Created by Gustav on 25.11.2017.
 */

public class DisplayObject {
    private int id;
    private ObjectType objectType;
    private String content;

    public DisplayObject(ObjectType objectType, String content) {
        this.objectType = objectType;
        this.content = content;
    }

    public DisplayObject(int id, ObjectType objectType, String content) {
        this.id = id;
        this.objectType = objectType;
        this.content = content;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public static DisplayObject getDefault() {
        return new DisplayObject(ObjectType.EMPTY, "Nothingness");

    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DisplayObject[" + id + "] " + objectType + " " + content;
    }

}
