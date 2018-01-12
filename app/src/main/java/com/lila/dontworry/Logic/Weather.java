package com.lila.dontworry.Logic;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lidia on 12.01.2018.
 */

public class Weather {
        private Boolean isSunny;
        private Boolean isSnow;
        private String weather_city;
        private String weather_description;
        private String weather_temperature;
        private String weather_humidity;
        private String weather_pressure;
        private Date date;

        public Boolean getIsSunny() {
            return this.isSunny;
        }

        public Boolean getIsSnow() {
            return this.isSnow;
        }

        public String getWeather_city() {
            return this.weather_city;
        }

        public String getWeather_description() {
            return this.weather_description;
        }

        public String getWeather_temperature() {
            return this.weather_temperature;
        }

        public String getWeather_humidity() {
            return this.weather_humidity;
        }

        public String getWeather_pressure() {
            return this.weather_pressure;
        }

        public Date getDate() {
            return this.date;
        }

        public Weather(Boolean isSunny1, Boolean isSnow1, String weather_city1, String weather_description1, String weather_temperature1, String weather_humidity1, String weather_pressure1){
            isSunny = isSunny1;
            isSnow = isSnow1;
            weather_city = weather_city1;
            weather_description = weather_description1;
            weather_temperature = weather_temperature1;
            weather_humidity = weather_humidity1;
            weather_pressure = weather_pressure1;
            Date date = new Date(); // your date
            this.date = date;
            System.out.println("new weather date:" + date.getTime());
        }
}
