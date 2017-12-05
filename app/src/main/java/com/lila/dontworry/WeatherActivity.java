package com.lila.dontworry;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.lila.dontworry.Logic.Weather;

public class WeatherActivity extends AppCompatActivity {

    private Context self = this;
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
                Intent intent = new Intent(self, MainActivity.class);
                startActivity(intent);
            }
        });

        final TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, title;
        detailsField = findViewById(R.id.details_field);
        currentTemperatureField = findViewById(R.id.current_temperature_field);
        humidity_field = findViewById(R.id.humidity_field);
        cityField = findViewById(R.id.city);
        pressure_field = findViewById(R.id.pressure);
        title = findViewById(R.id.title_weather);
        final ImageView icon = findViewById(R.id.weather_icon);

        detailsField.setText(Weather.getWeather_description());
        currentTemperatureField.setText(Weather.getWeather_temperature());
        humidity_field.setText(Weather.getWeather_humidity());
        cityField.setText(Weather.getWeather_city());
        pressure_field.setText(Weather.getWeather_pressure());

        if(Weather.getIsSnow()){
            title.setText("Do you want to build a snowman?");
            icon.setImageResource(R.mipmap.snow);
        }

        if(Weather.getIsSunny()){
            title.setText("Go for a walk!");
            icon.setImageResource(R.mipmap.sun);
        }




    }
}
