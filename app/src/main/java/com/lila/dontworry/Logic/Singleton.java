package com.lila.dontworry.Logic;

import java.util.ArrayList;

/**
 * Created by Lidia on 07.01.2018.
 * saves events TODO:to DB
 */

public class Singleton {
    private static Singleton ourInstance = null;
    private static ArrayList<Event> list = null;
    public static ArrayList<Event> getList() {
        return list;
    }

    public static Singleton getInstance(ArrayList<Event> events) {
        if(events==null && ourInstance == null){}
        else {
            ourInstance = new Singleton(events);
        }
        return ourInstance;
    }
    private Singleton(ArrayList<Event> list) {
        this.list = list;
    }
}
