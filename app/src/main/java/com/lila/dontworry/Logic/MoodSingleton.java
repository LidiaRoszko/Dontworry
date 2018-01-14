package com.lila.dontworry.Logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Lidia on 14.01.2018.
 */

public class MoodSingleton {
        private static MoodSingleton ourInstance = null;
        private static HashMap<Calendar,Mood> l = null;
        public static MoodSingleton getInstance() {
            if(ourInstance == null){
                ourInstance = new MoodSingleton();
                l = new HashMap<>();
            }
            return ourInstance;
        }

    public static void put(Mood mood) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        l.put(c,mood);
        }

    public static HashMap<Calendar, Mood> getMap() {
        return l;
    }

    public MoodSingleton() {

    }
}
