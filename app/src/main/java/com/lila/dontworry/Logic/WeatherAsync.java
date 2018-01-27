package com.lila.dontworry.Logic;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lidia on 25.01.2018.
 */

public class WeatherAsync extends AsyncTask<URL, Integer, JSONObject> {

    @Override
    protected JSONObject doInBackground(URL... urls) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, -3); // minus 3 hours
        Boolean b = true;
        if(WeatherSingleton.exist()){
            b = WeatherSingleton.getWeather().getDate().before(cal.getTime());
        }
        System.out.println("WeatherAsync: changed? - " + Localisation.isChange() + ", before? - " + b);
        if(Localisation.isChange() || WeatherSingleton.getWeather().getDate().before(cal.getTime())){
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Localisation.setChange(false);

        connection.addRequestProperty("x-api-key", "3cb");
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            try {
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject data = new JSONObject(json.toString());
                return data;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    private static boolean isSunny(int actualId, long sunrise, long sunset) { //a sunny or cloudy day during before sunset
        long currentTime = new Date().getTime();
        if (currentTime >= sunrise && currentTime < sunset) {
            if (actualId == 800 || actualId == 801 || actualId == 802 || actualId == 803) {
                return true;
            }
            return false;
        }
        return false;
    }

    private static boolean isSnow(int actualId, long sunrise, long sunset) {
        long currentTime = new Date().getTime();
        if (currentTime >= sunrise && currentTime < sunset) {
            if(actualId==602||actualId==601||actualId==600){
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        try {
            if(json != null){
                JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                JSONObject main = json.getJSONObject("main");
                DateFormat df = DateFormat.getDateTimeInstance();

                String city = json.getString("name");
                String description = details.getString("description").toUpperCase(Locale.US);
                String temperature = String.format("%.2f", main.getDouble("temp"))+ "°C";
                String humidity = main.getString("humidity") + "%";
                String pressure = main.getString("pressure") + " hPa";
                Boolean isSunny = isSunny(details.getInt("id"), json.getJSONObject("sys").getLong("sunrise") * 1000, json.getJSONObject("sys").getLong("sunset") * 1000);
                Boolean isSnow = isSnow(details.getInt("id"), json.getJSONObject("sys").getLong("sunrise") * 1000, json.getJSONObject("sys").getLong("sunset") * 1000);

                WeatherSingleton.getInstance(new Weather(isSunny, isSnow, city, description, temperature, "Humidity: " + humidity, pressure));
            }
        } catch (JSONException e) {
            //Log.e(LOG_TAG, "Cannot process JSON results", e);
        }
    }
}