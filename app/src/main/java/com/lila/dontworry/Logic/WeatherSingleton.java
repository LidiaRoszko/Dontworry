package com.lila.dontworry.Logic;

import java.util.Date;

/**
 * Created by Lidia on 05.12.2017.
 */

public class WeatherSingleton {

    private static WeatherSingleton instance = null;

    public static Weather getWeather() {
        return weather;
    }

    private static Weather weather = null;

    private WeatherSingleton() {}

    public static Boolean exist(){
        if(instance!=null){
            return true;
        }
        return false;
    }
    public static WeatherSingleton getInstance(Weather w){
        if (WeatherSingleton.instance == null) {
            WeatherSingleton.instance = new WeatherSingleton();
        }
        weather = w;
        return WeatherSingleton.instance;
    }
}

