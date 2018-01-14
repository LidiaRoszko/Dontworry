package com.lila.dontworry.Logic;

/**
 * Created by Lidia on 13.01.2018.
 */

public class Contact {
    private String name;
    private String number;
    private String photo;

    public Contact(String name, String number, String photo) {
        this.name = name;
        this.number = number;
        this.photo = photo;
        System.out.println("New Contact:" + name + number);
    }

    public String getName() {

        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getPhoto() {
        return photo;
    }
}
