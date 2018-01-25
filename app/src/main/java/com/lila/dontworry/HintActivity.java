package com.lila.dontworry;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import com.lila.dontworry.Logic.DatabaseHandler;
import com.lila.dontworry.Logic.Hint;

public class HintActivity extends AppCompatActivity { //written hint

    DatabaseHandler databaseHandler;
    Hint act_hint = Hint.getDefault();
    private Context self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initialisation of Activity and Toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView hint = findViewById(R.id.hint);
        if(savedInstanceState!=null){
            hint.setText(savedInstanceState.getString("act_hint_text"));
        }
        else{
            databaseHandler = DatabaseHandler.getInstance(this);
            //get hint with object of type EMPTY, PLACE*when there is no google maps integration to gos there
            act_hint = databaseHandler.nextHint();
            System.out.println(act_hint);
            hint.setText(act_hint.createText());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("act_hint_text", act_hint.createText());
        Log.i("onSaveInstanceState", "onSaveInstanceState()");
    }

    //back arrow
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
