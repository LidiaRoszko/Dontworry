package com.lila.dontworry;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Random;
import com.lila.dontworry.Logic.DatabaseHandler;
import com.lila.dontworry.Logic.Question;

public class MainActivity extends AppCompatActivity {

    DatabaseHandler databaseHandler;
    Question act_question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
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
                    Intent intent1 = new Intent(context, HintActivity.class);
                    startActivity(intent1);
                }
                else if(number==3){
                    Intent intent2 = new Intent(context, WeatherActivity.class);
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