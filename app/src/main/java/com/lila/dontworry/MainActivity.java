package com.lila.dontworry;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    //DatabaseHandler databaseHandler = new DatabaseHandler(this);
    //Question act_question = databaseHandler.nextQuestion();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        addListenerOnButton();
        TextView question = (TextView) findViewById(R.id.question);
        //question.setText(act_question.getText());
        question.setText("Do you like1?");

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.clearCheck();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public void addListenerOnButton() {

        final Context context = this;
        ImageButton imageButton1 = (ImageButton) findViewById(R.id.bulb);
        imageButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Random randomGenerator = new Random();
                if(randomGenerator.nextInt(2)==1) {
                    Intent intent = new Intent(context, PhoneActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent2 = new Intent(context, WeatherActivity.class);
                    startActivity(intent2);
                }
            }
        });

        ImageButton imageButton2 = (ImageButton) findViewById(R.id.next);
        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                TextView question = (TextView) findViewById(R.id.question);
                onRadioButtonClicked(arg0);
                //act_question = databaseHandler.nextQuestion();
                //question.setText(act_question.getText());
                question.setText("Do you like2?");
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
                radioGroup.clearCheck();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        RadioButton yes = (RadioButton) findViewById(R.id.yes);
        RadioButton no = (RadioButton) findViewById(R.id.no);

        if (no.isChecked()) {
            //databaseHandler.answerQuestion(act_question,false);
            System.out.println("answer:false");
        }
        if (yes.isChecked()) {
            //databaseHandler.answerQuestion(act_question,true);
            System.out.println("answer:true");
        }
    }
};