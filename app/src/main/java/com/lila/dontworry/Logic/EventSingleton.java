package com.lila.dontworry.Logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Lidia on 07.01.2018.
 * saves events TODO:to DB, Hashmap with Calendar c and List of Events
 */

public class EventSingleton {
    private static EventSingleton ourInstance = null;
    private static HashMap map = null;
    private static Calendar cal;
    private static ArrayList<Event> l;
    public static EventSingleton getInstance(ArrayList<Event> events, Calendar c) {
        if(events==null && ourInstance == null){}
        else {
            if(map == null){
                map = new HashMap();
                l = null;
            }
            map.put(c, events);
            System.out.println(map);
            cal = c;
            if(l==null){
            l = events;}
        }
        return ourInstance;
    }

    public static HashMap getMap() {
        return map;
    }

    public static ArrayList<Event> getList() {
        return l;
    }

    private EventSingleton(HashMap map1) {
        map = map1;
    }

    public static Calendar getCal() {
        return cal;
    }
}
