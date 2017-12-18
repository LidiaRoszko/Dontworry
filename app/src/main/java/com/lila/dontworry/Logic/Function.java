package com.lila.dontworry.Logic;
// www.androstock.com

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Function {
    private static final String OPEN_WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    private static final String OPEN_WEATHER_MAP_API = ""; //!!!

    private static boolean isSunny(int actualId, long sunrise, long sunset) {
        long currentTime = new Date().getTime();
        if (currentTime >= sunrise && currentTime < sunset) {
            if (actualId == 800 || actualId == 801) {
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

    public interface AsyncResponse {
        void processFinish(Boolean bool1, Boolean bool2, String output1, String output2, String output3, String output4, String output5);
    }

    public static class placeIdTask extends AsyncTask<String, Void, JSONObject> {

        public AsyncResponse delegate = null;//Call back interface

        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jsonWeather = null;
            try {
                jsonWeather = getWeatherJSON(params[0], params[1]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }
            return jsonWeather;
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

                    delegate.processFinish(isSunny, isSnow, city, description, temperature, "Humidity: " + humidity, pressure);
                }
            } catch (JSONException e) {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }
        }
    }

    public static JSONObject getWeatherJSON(String lat, String lon){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, lat, lon));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }
            return data;
        }catch(Exception e){
            return null;
        }
    }
}
