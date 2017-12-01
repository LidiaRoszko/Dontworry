package com.lila.dontworry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import com.lila.dontworry.Logic.DatabaseHandler;
import com.lila.dontworry.Logic.Hint;

public class HintActivity extends AppCompatActivity {

    DatabaseHandler databaseHandler;
    Hint act_hint = Hint.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        databaseHandler = new DatabaseHandler(this);

        //initialisation of Activity and Toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationIcon(R.drawable.back);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //get hint with object of type EMPTY, PLACE*when there is no google maps integration to gos there
        act_hint = databaseHandler.nextHint();
        TextView hint = findViewById(R.id.hint);
        hint.setText(act_hint.createText());
    }

    //initialisation of menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }



    }
