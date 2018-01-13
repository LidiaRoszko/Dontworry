package com.lila.dontworry;
// https://github.com/Diolor/Swipecards - swiping of the question
// www.androstock.com - getting the weather
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import com.lila.dontworry.Logic.DatabaseHandler;
import com.lila.dontworry.Logic.Events;
import com.lila.dontworry.Logic.Function;
import com.lila.dontworry.Logic.Localisation;
import com.lila.dontworry.Logic.Question;
import com.lila.dontworry.Logic.Weather;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

public class MainActivity extends AppCompatActivity { // answering the questions or getting hints + downloading of the events, weather and saving in DB

    private int n; //number of hints
    DatabaseHandler databaseHandler;
    private int count = 0;
    private Question act_question;
    private ArrayList<String> al;
    private ArrayList<Question> a2;
    private ArrayAdapter<String> arrayAdapter;

    //getting with rest the weather
    private void getWeather(){
        Function.placeIdTask asyncTask = new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(Boolean isSunny, Boolean isSnow, String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure) {
                Weather.getInstance(isSunny, isSnow, weather_city, weather_description, weather_temperature, weather_humidity, weather_pressure, new Date());
                System.out.println("new weather");
            }
        });
        asyncTask.execute(String.valueOf(Localisation.getL1()), String.valueOf(Localisation.getL2())); //  asyncTask.execute("Latitude", "Longitude")
    }

    private void getEvents() throws IOException {
        new Events().execute(new URL("https://www.kulturkalender-dresden.de/alle-veranstaltungen/2018-01-07"));
         }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Weather.getDate()==null){getWeather();} //only one per hour, without connection ist still true up to 2 hours
        // getEvents(); // only one per day with wifi, when older than 2 days then can be downloaded with mobile Internet
        try {
            getEvents();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //initialisation of Activity and Toolbar
        super.onCreate(savedInstanceState);
        databaseHandler = DatabaseHandler.getInstance(this);
        n = databaseHandler.getNumberOfQuestions();
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        addListenerOnButton();

        //QUESTIONSs
        act_question = databaseHandler.nextQuestion();
        al = new ArrayList<>(); //array of questions' strings
        a2 = new ArrayList<>(); //array of questions
        Log.d("vor n", String.valueOf(n));
        for(int i = 0; n > i ; i++){
            Log.d("i ", String.valueOf(i));
            al.add(act_question.createText());
            Log.d("question ",act_question.createText());
            a2.add(act_question);
            act_question = databaseHandler.nextQuestion();
        }

        arrayAdapter = new ArrayAdapter(this, R.layout.item, R.id.helloText, al );
        final SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {

            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Question question = databaseHandler.getQuestion((String)dataObject);
                databaseHandler.answerQuestion(question,false);
                System.out.println("answer:false");
                count++;
                makeToast(MainActivity.this, "No!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Question question = databaseHandler.getQuestion((String)dataObject);
                // a2.get(count)
                databaseHandler.answerQuestion(question,true);
                System.out.println("answer:true");
                count++;
                makeToast(MainActivity.this, "Yes!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if(count==n){
                makeToast(MainActivity.this, "No question more for now!");}
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

    }
    static void makeToast(Context ctx, String s){
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
    public void addListenerOnButton() {
        //new activity should start after click on the bulb
        final Context context = this;
        ImageButton imageButton = (ImageButton) findViewById(R.id.bulb);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Random randomGenerator = new Random();
                int number = randomGenerator.nextInt(5);
                if(number==1||number==2) {
                    if(Weather.getIsSunny()||Weather.getIsSnow()){
                        Intent intent1 = new Intent(context, WeatherActivity.class);
                        startActivity(intent1);
                    }
                    else{
                        Intent intent11 = new Intent(context, WeatherActivity.class);
                        startActivity(intent11);
                    }
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


}