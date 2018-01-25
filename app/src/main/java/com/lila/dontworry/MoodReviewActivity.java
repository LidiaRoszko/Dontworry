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
import com.lila.dontworry.Logic.DatabaseHandler;
import com.lila.dontworry.Logic.Mood;
import java.util.Calendar;
import java.util.Date;

public class MoodReviewActivity extends AppCompatActivity {
    final Context context = this;
    static Mood tempMood = Mood.VERY_BAD;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        this.date = cal.getTime().toString();
        setContentView(R.layout.activity_moodreview);
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.statistics);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        addListenerOnButton();
    }

    // mood review
    public void addListenerOnButton() {
        final Context context = this;
        final DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
        ImageButton moodb = (ImageButton) findViewById(R.id.moodb);
        final Intent intent = new Intent(context, StatisticsActivity.class);
        moodb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                databaseHandler.addMood(Mood.BAD, date);
                //moodSingleton.put(Mood.BAD);
                startActivity(intent);
            }
        });
        ImageButton moodg = (ImageButton) findViewById(R.id.moodg);
        moodg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                databaseHandler.addMood(Mood.GOOD, date);
                //moodSingleton.put(Mood.GOOD);
                startActivity(intent);
            }
        });
        ImageButton moodm = (ImageButton) findViewById(R.id.moodm);
        moodm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                databaseHandler.addMood(Mood.MODERATE, date);
//                moodSingleton.put(Mood.MODERATE);
                startActivity(intent);
            }
        });
        ImageButton moodvg = (ImageButton) findViewById(R.id.moodvg);
        moodvg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                databaseHandler.addMood(Mood.GOOD, date);
//                moodSingleton.put(Mood.VERY_GOOD);
                startActivity(intent);
            }
        });
        ImageButton moodvb = (ImageButton) findViewById(R.id.moodvb);
        moodvb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                databaseHandler.addMood(Mood.VERY_BAD, date);
//                moodSingleton.put(Mood.VERY_BAD);
                startActivity(intent);
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
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.statistics:
                    return true;
                case R.id.hints:
                    Intent i2 = new Intent(context, MainActivity.class);
                    i2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i2);
                    return true;
                case R.id.questions:
                    Intent i1 = new Intent(context, QandAActivity.class);
                    i1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i1);
            }
            return false;
        }
    };
}
