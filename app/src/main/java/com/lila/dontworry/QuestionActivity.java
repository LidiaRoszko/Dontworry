package com.lila.dontworry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lila.dontworry.Logic.DatabaseHandler;
import com.lila.dontworry.hints.ExtraHint;
import com.lila.dontworry.hints.Hint;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {
    private String place,name;

    private ArrayList<Hint> dataBaseOfHints = new ArrayList<Hint>();
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHandler = new DatabaseHandler(getApplicationContext());

        setContentView(R.layout.activity_question);
        //Toolbar
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

        setPlace();
        setName();
        ExtraHint e = new ExtraHint(this.place, "cat6hint", "nocat6hint", this.place);
       final ExtraHint[] hints = {new ExtraHint("catoo", "cathint", "nocathint"), new ExtraHint("cat2", "cat2hint", "nocat2hint"), new ExtraHint("cat3", "cat3hint", "nocat3hint"), new ExtraHint("cat4", "cat4hint", "nocat4hint"), new ExtraHint(this.name, "cat5hint", "nocat5hint", this.name), e};

        //Question2
        TextView t = findViewById(R.id.question2);
        t.setText(databaseHandler.nextQuestion().getText());
        //t.setText(" how was in " + e.getName());


        addListenerOnButton(hints);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    public void addListenerOnButton(final ExtraHint[] hints) {

        TextView a = findViewById(R.id.question2);

        a.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(QuestionActivity.this,
                        "Question2 is clicked!", Toast.LENGTH_SHORT).show();

            }
        });


        ImageView vb = findViewById(R.id.vb);
        vb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Intent is what you use to start another activity
                Toast.makeText(QuestionActivity.this,
                        "vb is clicked!", Toast.LENGTH_SHORT).show();

                boolean[] list2 = {true,false,true,false,true,false};
                sendToDB(list2,Mood.VB, hints);
            }
        });

        ImageView b = findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Intent is what you use to start another activity
                Toast.makeText(QuestionActivity.this,
                        "b is clicked!", Toast.LENGTH_SHORT).show();
                boolean[] list2 = {true,false,true,false,true,false};
                sendToDB(list2,Mood.B, hints);
            }
        });

        ImageView m = findViewById(R.id.m);
        m.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Intent is what you use to start another activity
                Toast.makeText(QuestionActivity.this,
                        "m is clicked!", Toast.LENGTH_SHORT).show();
                boolean[] list2 = {true,false,true,false,true,false};
                sendToDB(list2,Mood.M, hints);
            }
        });

        ImageView g = findViewById(R.id.g);
        g.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Intent is what you use to start another activity
                Toast.makeText(QuestionActivity.this,
                        "g is clicked!", Toast.LENGTH_SHORT).show();
                boolean[] list2 = {true,false,true,false,true,false};
                sendToDB(list2,Mood.G, hints);
            }
        });

        ImageView vg = findViewById(R.id.vg);
        vg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(QuestionActivity.this,
                        "vg is clicked!", Toast.LENGTH_SHORT).show();
                boolean[] list2 = {true,false,true,false,true,false};
                sendToDB(list2,Mood.VG, hints);

            }
        });
    }

    public void setPlace(){
        this.place = "APB";
    }
    public void setName(){
        this.name = "Landi";
    }

    public void sendToDB(boolean[] list, Mood mood, ExtraHint[] hints){
        System.out.println("SEND");
        for(int i = 0; i < 4; i++ ) {
            boolean answer = list[i];
            if (mood == Mood.M) {
                break;
            } else if (mood == Mood.G && answer|| mood == Mood.VG && answer|| mood == Mood.B && !answer || mood == Mood.VB && !answer) {
                this.dataBaseOfHints.add(new Hint(hints[i].getCode(), hints[i].getHintString()));
            } else {
                this.dataBaseOfHints.add(new Hint(hints[i].getCode(), hints[i].getHintString2()));
            }
        }
        //phone and place
        for(int j = 4; j<6 ; j++) {

            if (list[j]) {
                dataBaseOfHints.add(new Hint(hints[j].getName(), hints[j].getHintString()));
            }

            else {
                dataBaseOfHints.add(new Hint(hints[j].getName(), hints[j].getHintString2()));
            }

        }

    }

    private enum Mood{
        VG,G,M,B,VB;
    }
}

