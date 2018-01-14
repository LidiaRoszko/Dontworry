package com.lila.dontworry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import com.lila.dontworry.Logic.DatabaseHandler;
import com.lila.dontworry.Logic.Question;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

public class QandAActivity extends AppCompatActivity {
    private int n; //number of hints
    DatabaseHandler databaseHandler;
    private int count = 0;
    private Question act_question;
    private ArrayList<String> al;
    private ArrayList<Question> a2;
    private ArrayAdapter<String> arrayAdapter;
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
                    Intent i2 = new Intent(context, Main2Activity.class);
                    i2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i2);
                case R.id.questions:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qand_a);

        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.questions);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //initialisation of Activity and Toolbar

        databaseHandler = DatabaseHandler.getInstance(this);
        n = databaseHandler.getNumberOfQuestions();
        n = 1;
        //QUESTIONSs
        //act_question = databaseHandler.nextQuestion();
        al = new ArrayList<>(); //array of questions' strings
        a2 = new ArrayList<>(); //array of questions
        Log.d("vor n", String.valueOf(n));
        for(int i = 0; n > i ; i++){
            addQuestion();
            /*
            Log.d("i ", String.valueOf(i));
            al.add(act_question.createText());
            Log.d("question ",act_question.createText());
            a2.add(act_question);
            act_question = databaseHandler.nextQuestion();
            */
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
                databaseHandler.answerQuestion(a2.get(count),false);
                System.out.println("answer:false");
                count++;
                makeToast(QandAActivity.this, "No!");
                addQuestion();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                databaseHandler.answerQuestion(a2.get(count),true);
                System.out.println("answer:true");
                count++;
                makeToast(QandAActivity.this, "Yes!");
                addQuestion();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if(count==n){
                    makeToast(QandAActivity.this, "No question more for now!");}
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

    }

    private void addQuestion() {
        act_question = databaseHandler.nextQuestion();
        al.add(act_question.createText());
        a2.add(act_question);
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
}

