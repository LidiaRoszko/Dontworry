package com.lila.dontworry;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Random;
import com.lila.dontworry.Logic.DatabaseHandler;
import com.lila.dontworry.Logic.Function;
import com.lila.dontworry.Logic.Localisation;
import com.lila.dontworry.Logic.Question;
import com.lila.dontworry.Logic.Weather;

public class MainActivity extends AppCompatActivity {

    DatabaseHandler databaseHandler;
    Question act_question;

    //getting with rest the weather
    private void getWeather(){
        Function.placeIdTask asyncTask = new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(Boolean isSunny, Boolean isSnow, String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure) {
                Weather.getInstance(isSunny, isSnow, weather_city, weather_description, weather_temperature, weather_humidity, weather_pressure);
                System.out.println("new weather");
            }
        });
        asyncTask.execute(String.valueOf(Localisation.getL1()), String.valueOf(Localisation.getL2())); //  asyncTask.execute("Latitude", "Longitude")
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWeather(); //only one per hour
        //initialisation of Activity and Toolbar
        super.onCreate(savedInstanceState);
        databaseHandler = new DatabaseHandler(getApplicationContext());
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        addListenerOnButton();

        //set question to answer
        act_question = databaseHandler.nextQuestion();
        TextView question = (TextView) findViewById(R.id.question);
        question.setText(act_question.createText());

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
                int number = randomGenerator.nextInt(4);
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
                else{
                    Intent intent3 = new Intent(context, PhoneActivity.class);
                    startActivity(intent3);
                }
            }
        });

        //Questions
        ImageButton trueButton = (ImageButton) findViewById(R.id.trueButton);
        ImageButton falseButton = (ImageButton) findViewById(R.id.falseButton);

        final TextView question = (TextView) findViewById(R.id.question);

        //true is clicked
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                databaseHandler.answerQuestion(act_question,true);
                System.out.println("answer:true");
                act_question = databaseHandler.nextQuestion();
                question.setText(act_question.createText());
            }
        });

        //false is clicked
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                databaseHandler.answerQuestion(act_question,false);
                System.out.println("answer:false");
                act_question = databaseHandler.nextQuestion();
                question.setText(act_question.createText());
            }
        });
    }


}