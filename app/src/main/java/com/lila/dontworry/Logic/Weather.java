package com.lila.dontworry.Logic;
/**
 * Created by Lidia on 05.12.2017.
 */

public class Weather {

    private static Boolean isSunny = true;
    private static Boolean isSnow = false;
    private static String weather_city;
    private static String weather_description;
    private static String weather_temperature;
    private static String weather_humidity;
    private static String weather_pressure;

    private static Weather instance;
    private Weather () {}

    public static Boolean getIsSunny() {
        return isSunny;
    }

    public static Boolean getIsSnow() {
        return isSnow;
    }

    public static String getWeather_city() {
        return weather_city;
    }

    public static String getWeather_description() {
        return weather_description;
    }

    public static String getWeather_temperature() {
        return weather_temperature;
    }

    public static String getWeather_humidity() {
        return weather_humidity;
    }

    public static String getWeather_pressure() {
        return weather_pressure;
    }

    public static Weather getInstance(Boolean isSunny1, Boolean isSnow1, String weather_city1, String weather_description1, String weather_temperature1, String weather_humidity1, String weather_pressure1){
        if (Weather.instance == null) {
            Weather.instance = new Weather ();
        }
        isSunny = isSunny1;
        isSnow = isSnow1;
        weather_city = weather_city1;
        weather_description = weather_description1;
        weather_temperature = weather_temperature1;
        weather_humidity = weather_humidity1;

        weather_pressure = weather_pressure1;
        return Weather.instance;
    }
}

