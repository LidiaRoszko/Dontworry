package com.lila.dontworry;
// https://github.com/Diolor/Swipecards - swiping of the question

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
    private final Context context = this;
    private String act_question_text;
    private ArrayList<String> list_of_questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initialisation of Activity and Toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qand_a);
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.questions);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);

        databaseHandler = DatabaseHandler.getInstance(this);
        n = databaseHandler.getNumberOfQuestions();
        al = new ArrayList<>(); //array of questions' strings
        a2 = new ArrayList<>(); //array of questions
        getAdapter(savedInstanceState);
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
                databaseHandler.answerQuestion(a2.get(count), false);
                System.out.println("answer:false");
                count++;
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                databaseHandler.answerQuestion(a2.get(count), true);
                System.out.println("answer:true");
                count++;
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if (count == n) {
                    System.out.println("No question more for now!");
                }
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });
    }

    private void getAdapter(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            //QUESTIONSs
            act_question = databaseHandler.nextQuestion();
            Log.d("vor n", String.valueOf(n));
            for (int i = 0; n > i; i++) {
                Log.d("i ", String.valueOf(i));
                al.add(act_question.createText());
                Log.d("question ", act_question.createText());
                a2.add(act_question);
                act_question = databaseHandler.nextQuestion();
            }
        } else {
            list_of_questions = savedInstanceState.getStringArrayList("list_of_questions");
            try{
            act_question_text = savedInstanceState.getString("act_question_text");}
            catch(NullPointerException e){};
            Log.i("onRestoreInstanceState", "onRestoreInstanceState()");
            n = list_of_questions.size();
            if (list_of_questions != null) {
                al.addAll(list_of_questions);
            }
            for (int i = 0; n > i; i++) {
                act_question = databaseHandler.searchQuestion(act_question_text);
                a2.add(act_question);
            }
        }
        arrayAdapter = new ArrayAdapter(this, R.layout.item, R.id.helloText, al);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("list_of_questions", this.al);
        if(act_question!=null){
        outState.putString("act_question_text", act_question.getText());}
        Log.i("onSaveInstanceState", "onSaveInstanceState()");
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

}
