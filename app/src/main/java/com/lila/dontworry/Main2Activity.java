package com.lila.dontworry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.lila.dontworry.Logic.Event;
import com.lila.dontworry.Logic.Events;
import com.lila.dontworry.Logic.Singleton;
import com.lila.dontworry.Logic.Weather;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {

    final Context context = this;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.statistics:
                    Intent i1 = new Intent(context, StatisticActivity.class);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        try {
            getEvents();
        } catch (IOException e) {
            e.printStackTrace();
        }
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        addListenerOnButton();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.hints);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void getEvents() throws IOException {
    if (Singleton.getList()==null) {
            Date date = new Date(); // your date
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String monthString = String.valueOf(month);
            String dayString = String.valueOf(day);
            if(month<10){
                monthString = "0" + monthString;
            }
            if(day<10){
                dayString = "0" + dayString;
            }

            System.out.println("eeeeeeeeeee https://www.kulturkalender-dresden.de/alle-veranstaltungen/" + year +"-" + monthString + "-" +dayString);
            new Events().execute(new URL("https://www.kulturkalender-dresden.de/alle-veranstaltungen/" + year +"-" + monthString + "-" +dayString));
        } /*
        ArrayList<Event> l = new ArrayList<>();
        l.add(new Event("22.01.2003", "Semper Oper", "Gesprach" , "https://code.tutsplus.com/tutorials/launching-the-browser-from-your-android-applications-the-easy-way--mobile-2414")); l.add(new Event("24.01.2003", "Theaterplatz", "Gesprach2" , "https://code.tutsplus.com"));
        l.add(new Event("225.01.2003", "Zeunerstrasse", "Gesprac3h" , "https://code.tutsplus.com/tutorials/launching-the-browser-from-your-android-applications-the-easy-way--mobile-2414"));
        l.add(new Event("24.01.2003", "APB", "Gesp4rach" , "https://code.tutsplus.com/tutorials/launching-the-browser-from-your-android-applications-the-easy-way--mobile-2414"));
        l.add(new Event("27.01.2003", "Frauenkirche", "Ges5prach" , "https://code.tutsplus.com/tutorials/launching-the-browser-from-your-android-applications-the-easy-way--mobile-2414"));
        l.add(new Event("29.01.2003", "Lukaskirche", "Gespra7ch" , "https://code.tutsplus.com/tutorials/launching-the-browser-from-your-android-applications-the-easy-way--mobile-2414"));
        Singleton.getInstance(l);*/
    }
    public void addListenerOnButton() {
        //new activity should start after click on the bulb
        final Context context = this;
        ImageButton imageButton = (ImageButton) findViewById(R.id.bulb);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                    Intent intent1 = new Intent(context, EventActivity.class);
                    startActivity(intent1);
               Random randomGenerator = new Random();
                int number = randomGenerator.nextInt(5);
                if(number==1) {
                    if(Weather.getIsSunny()||Weather.getIsSnow()){
                        Intent i1 = new Intent(context, WeatherActivity.class);
                        startActivity(i1);
                    }
                    else{
                        Intent intent11 = new Intent(context, WeatherActivity.class);
                        startActivity(intent11);
                    }
                }
                else if(number==2 && Singleton.getList()!=null){
                    Intent intent2 = new Intent(context, Event.class);
                    startActivity(intent2);
                }
                else if(number==2 && Singleton.getList()==null){
                    Intent intent2 = new Intent(context, HintActivity.class);
                    startActivity(intent2);
                }
                else if(number==3){
                    Intent intent2 = new Intent(context, HintActivity.class);
                    startActivity(intent2);
                }
                else if(number==4){
                    Intent intent2 = new Intent(context, YoutubePlayer.class);
                    startActivity(intent2);
                }
                else{
                    Intent intent3 = new Intent(context, PhoneActivity.class);
                    startActivity(intent3);
                }
            }
        });
    }

    //create a menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.setLocation:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                return true;
        }
        switch(item.getItemId()){
            case R.id.help:
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }
}
