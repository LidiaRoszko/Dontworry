package com.lila.dontworry;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.lila.dontworry.Logic.DatabaseHandler;
import com.lila.dontworry.Logic.Event;
import com.lila.dontworry.Logic.EventAsync;
import com.lila.dontworry.Logic.MoodSingleton;
import com.lila.dontworry.Logic.Utility;
import com.lila.dontworry.Logic.Weather;
import com.lila.dontworry.Logic.WeatherAsync;
import com.lila.dontworry.Logic.Localisation;
import com.lila.dontworry.Logic.EventSingleton;
import com.lila.dontworry.Logic.WeatherSingleton;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.hints);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        addListenerOnButton();

        try {
            getEvents();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            getWeather();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void getEvents() throws IOException { //TODO: FROM DATABASE, CHECK IF THERE ARE FOR NEXT 2 DAYS(when wifi avaiilable), when phone internet and no for this day then has to be downloaded in another case nothing

        if (Utility.getConnectionType(this) == Utility.TYPE_DISCONNECTED)
            return;
        new EventAsync().execute(new URL("https://www.kulturkalender-dresden.de/alle-veranstaltungen/"));

    }

    private void getWeather() throws MalformedURLException { // www.androstock.com - getting the weather TODO:FROM DATABASE, if Location changed then new request, if 3 hours old then new request (with wifi and with normal internet)

        if (Utility.getConnectionType(this) == Utility.TYPE_DISCONNECTED)
            return;

        if (!WeatherSingleton.exist()) {
            WeatherAsync.placeIdTask asyncTask = new WeatherAsync.placeIdTask(new WeatherAsync.AsyncResponse() {
                public void processFinish(Boolean isSunny, Boolean isSnow, String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure) {
                    Weather w = new Weather(isSunny, isSnow, weather_city, weather_description, weather_temperature, weather_humidity, weather_pressure);
                    WeatherSingleton.getInstance(w);
                }
            });
            /*
            asyncTask.execute(String.valueOf(Localisation.getL1()), String.valueOf(Localisation.getL2())); //  asyncTask.execute("Latitude", "Longitude")
            if (Utility.getConnectionType(this) != Utility.TYPE_DISCONNECTED)
                new EventAsync().execute(new URL("https://www.kulturkalender-dresden.de/alle-veranstaltungen/"));
                */
        }
    }

    // getHint() TODO:probability for getting normal hint etc.
    public void addListenerOnButton() {
        //new activity should start after click on the bulb
        final Context context = this;
        ImageButton imageButton = (ImageButton) findViewById(R.id.bulb);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Random randomGenerator = new Random();
                int number = randomGenerator.nextInt(5);
                if (number == 4 && Utility.getConnectionType(context) != ConnectivityManager.TYPE_WIFI)
                    number = 3;

                if (number == 1) {
                    if (WeatherSingleton.exist()) {
                        if (WeatherSingleton.getWeather().getIsSunny() || WeatherSingleton.getWeather().getIsSnow()) {
                            Intent i1 = new Intent(context, WeatherActivity.class);
                            startActivity(i1);
                        } else {
                            Intent intent11 = new Intent(context, WeatherActivity.class);
                            startActivity(intent11);
                        }
                    }
                }
                //else if(number==2 && EventSingleton.getMap()!=null){
                else if (number == 2 && DatabaseHandler.eventsFetched) {
                    Intent intent2 = new Intent(context, EventActivity.class);
                    startActivity(intent2);
                }
                //else if(number==2 && EventSingleton.getMap()==null){
                else if (number == 2 && !DatabaseHandler.eventsFetched) {
                    Intent intent2 = new Intent(context, HintActivity.class);
                    startActivity(intent2);
                } else if (number == 3) {
                    Intent intent2 = new Intent(context, HintActivity.class);
                    startActivity(intent2);
                } else if (number == 4) {
                    Intent intent2 = new Intent(context, YoutubePlayer.class);
                    startActivity(intent2);
                } else {
                    Intent intent3 = new Intent(context, PhoneActivity.class);
                    startActivity(intent3);
                }
            }
        });
    }

    //create a menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setLocation:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                return true;
        }
        switch (item.getItemId()) {
            case R.id.help:
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    //bottom menu
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.statistics:
                    Intent i1;
                    if (true) { //TODO: check in DB if the mood review was done, when not then to mood review activity in another cas to statistics
                        i1 = new Intent(context, MoodReviewActivity.class);
                    } else {
                        i1 = new Intent(context, StatisticsActivity.class);
                    }
                    i1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i1);
                    return true;

                case R.id.hints:
                    return true;

                case R.id.questions:
                    Intent i3 = new Intent(context, QandAActivity.class);
                    i3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i3);
                    return true;
            }
            return false;
        }
    };
}
