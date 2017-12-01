package com.lila.dontworry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class WeatherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //initialisation of activity and toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
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
/*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("check");
        }
        //URL to get JSON Array
        String url = "https://query.yahooapis.com/v1/public/yql?q=select%20item%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22dresden%2C%20de%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";


        final OkHttpClient client = new OkHttpClient();

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {String MyResult = "nno";
                try {
                    Response response = client.newCall(request).execute();
                     MyResult = response.body().string();
                    System.out.println("postJSONRequest response.body : "+MyResult);

                } catch (IOException e) {
                    e.printStackTrace();
                }System.out.println("rrr"+MyResult);
            }
        });
    System.out.println("rrr");*/
    }


    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}
