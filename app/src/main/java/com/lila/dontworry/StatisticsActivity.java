package com.lila.dontworry;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.lila.dontworry.Logic.Mood;

import java.text.NumberFormat;
import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {  //TODO:FROM DATABASE

    private final Context context = this;
    private ArrayList<String> dates;
    private ArrayList<Mood> moods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //if landscape then graph
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            moods = new ArrayList<>();
            moods = getMoods(true);

            dates = new ArrayList<>();
            dates = getDates(true);

            int n = dates.size();

            ArrayList<DataPoint> datapoints = new ArrayList<>();
            ArrayList<DataPoint> datapointsVG = new ArrayList<>();
            ArrayList<DataPoint> datapointsG = new ArrayList<>();
            ArrayList<DataPoint> datapointsM = new ArrayList<>();
            ArrayList<DataPoint> datapointsB = new ArrayList<>();
            ArrayList<DataPoint> datapointsVB = new ArrayList<>();

            for(int i = 0 ; i< n ; i++){
              datapoints.add(new DataPoint(i, moods.get(i).ordinal()));
              datapointsVG.add(new DataPoint(i, 4));
              datapointsG.add(new DataPoint(i, 3));
              datapointsM.add(new DataPoint(i, 2));
              datapointsB.add(new DataPoint(i, 1));
              datapointsVB.add(new DataPoint(i, 0));
            }

            GraphView graph = (GraphView) findViewById(R.id.graph);
            NumberFormat nf = NumberFormat.getInstance();
            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));

            // custom label formatter to show currency "EUR"
            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX) {
                        int number =  (int) value;
                        return dates.get(number).substring(0,6);

                    } else {
                        return getMoodName(value);
                    }
                }

                private String getMoodName(double value) {
                    int number = (int) value;
                    switch(number) {
                        case 0:
                            return "Very bad";
                        case 1:
                            return "Bad";
                        case 2:
                            return "So so";
                        case 3:
                            return "Good";
                        case 4:
                            return "Very good";
                        default: return null;
                    }
                }
            });

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(datapoints.toArray( new DataPoint[]{}));
            series.setDrawDataPoints(true);
            series.setColor(Color.DKGRAY);
            LineGraphSeries<DataPoint> seriesVG = new LineGraphSeries<>(datapointsVG.toArray( new DataPoint[]{}));
            LineGraphSeries<DataPoint> seriesG = new LineGraphSeries<>(datapointsG.toArray( new DataPoint[]{}));
            LineGraphSeries<DataPoint> seriesM = new LineGraphSeries<>(datapointsM.toArray( new DataPoint[]{}));
            LineGraphSeries<DataPoint> seriesB = new LineGraphSeries<>(datapointsB.toArray( new DataPoint[]{}));
            LineGraphSeries<DataPoint> seriesVB = new LineGraphSeries<>(datapointsVB.toArray( new DataPoint[]{}));
            float[] vg = {90,80,100};
            seriesVG.setColor(Color.HSVToColor(vg));
            seriesVG.setThickness(15);
            float[] g = {66,69,100};
            seriesG.setColor(Color.HSVToColor(g));
            seriesG.setThickness(15);
            float[] m = {60,61,100};
            seriesM.setColor(Color.HSVToColor(m));
            seriesM.setThickness(15);
            float[] b = {43,73,100};
            seriesB.setColor(Color.HSVToColor(b));
            seriesB.setThickness(15);
            float[] vb = {0,83,100};
            seriesVB.setColor(Color.HSVToColor(vb));
            seriesVB.setThickness(15);

            graph.addSeries(seriesVG);
            graph.addSeries(seriesG);
            graph.addSeries(seriesM);
            graph.addSeries(seriesB);
            graph.addSeries(seriesVB);
            graph.addSeries(series);
        }
        else {
            ImageView mood1 = findViewById(R.id.mood1);
            ImageView mood2 = findViewById(R.id.mood2);
            ImageView mood3 = findViewById(R.id.mood3);
            ImageView mood4 = findViewById(R.id.mood4);

            TextView moodDay1 = findViewById(R.id.moodDay1);
            TextView moodDay2 = findViewById(R.id.moodDay2);
            TextView moodDay3 = findViewById(R.id.moodDay3);
            TextView moodDay4 = findViewById(R.id.moodDay4);

            moods = new ArrayList<>();
            moods = getMoods(false);

            dates = new ArrayList<>();
            dates = getDates(false);

            switch (dates.size()) {
                case 4:
                    mood4.setImageResource(getResource(moods.get(3)));
                    moodDay4.setText(dates.get(3));
                case 3:
                    mood3.setImageResource(getResource(moods.get(2)));
                    moodDay3.setText(dates.get(2));
                case 2:
                    mood2.setImageResource(getResource(moods.get(1)));
                    moodDay2.setText(dates.get(1));
                case 1:
                    mood1.setImageResource(getResource(moods.get(0)));
                    moodDay1.setText(dates.get(0));
                    break;
                default:
                    break;
            }
        }
    }

    private ArrayList<String> getDates(Boolean ifAll) { //TODO: from DB
        //  DatabaseHandler databaseHandler;
        // databaseHandler.getMoodTable().getDatesAsString();
        ArrayList<String> m = new ArrayList<>();
        ArrayList<String> m2 = new ArrayList<>();
        m.add("10.10.2002");m.add("11.10.2002");m.add("12.10.2002");m.add("13.10.2002");m.add("14.10.2002");
        if(ifAll){
            return m;
        }
        int n = m.size();
        if(n <= 4){
            return m;
        }
        else {
            m2.add(m.get(n-1));
            m2.add(m.get(n-2));
            m2.add(m.get(n-3));
            m2.add(m.get(n-4));
        }
        return m2;
    }

    private ArrayList<Mood> getMoods(Boolean ifAll) { //TODO: from DB
        //  DatabaseHandler databaseHandler;
        // databaseHandler.getMoodTable().getMoods();
        ArrayList<Mood> m = new ArrayList<>();
        ArrayList<Mood> m2 = new ArrayList<>();
        m.add(Mood.BAD);m.add(Mood.BAD);m.add(Mood.VERY_BAD);m.add(Mood.VERY_BAD);m.add(Mood.VERY_GOOD);
        if(ifAll){
            return m;
        }
        int n = m.size();
        if(n <= 4){
            return m;
        }
        else {
            m2.add(m.get(n-1));
            m2.add(m.get(n-2));
            m2.add(m.get(n-3));
            m2.add(m.get(n-4));
        }
        return m2;
    }

    private int getResource(Mood mood) {
        switch(mood){
            case MODERATE: return R.mipmap.moderat;
            case BAD:return R.mipmap.bad;
            case GOOD:return R.mipmap.good;
            case VERY_BAD:return R.mipmap.verybad;
            case VERY_GOOD:return R.mipmap.verygood;
            default:break;
        }
        return -1;
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
                    Intent i1 = new Intent(context, MoodReviewActivity.class);
                    i1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i1);
                    return true;
                case R.id.hints:
                    Intent i2 = new Intent(context, MainActivity.class);
                    i2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i2);
                    return true;
                case R.id.questions:
                    return true;
            }
            return false;
        }
    };
    //create a menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}
