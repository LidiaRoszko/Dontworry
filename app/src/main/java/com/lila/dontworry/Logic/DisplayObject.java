package com.lila.dontworry.Logic;

/**
 * Created by Gustav on 25.11.2017.
 */

public class DisplayObject {
    private ObjectType objectType;
    private String content;

    public DisplayObject(ObjectType objectType, String content) {
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
}
