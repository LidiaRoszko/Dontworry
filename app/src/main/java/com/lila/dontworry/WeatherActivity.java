package com.lila.dontworry;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.lila.dontworry.Logic.WeatherSingleton;

public class WeatherActivity extends AppCompatActivity { //TODO:from DB

    private Context self = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //initialisation of activity and toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, title;
        detailsField = findViewById(R.id.details_field);
        currentTemperatureField = findViewById(R.id.current_temperature_field);
        humidity_field = findViewById(R.id.humidity_field);
        cityField = findViewById(R.id.city);
        pressure_field = findViewById(R.id.pressure);
        title = findViewById(R.id.title_weather);
        final ImageView icon = findViewById(R.id.weather_icon);

        detailsField.setText(WeatherSingleton.getWeather().getWeather_description());
        currentTemperatureField.setText(WeatherSingleton.getWeather().getWeather_temperature());
        humidity_field.setText(WeatherSingleton.getWeather().getWeather_humidity());
        cityField.setText(WeatherSingleton.getWeather().getWeather_city());
        pressure_field.setText(WeatherSingleton.getWeather().getWeather_pressure());

        if(WeatherSingleton.getWeather().getIsSnow()){
            title.setText("Do you want to build a snowman?");
            icon.setImageResource(R.mipmap.snow);
        }

        if(WeatherSingleton.getWeather().getIsSunny()){
            title.setText("Go for a walk!");
            icon.setImageResource(R.mipmap.sun);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
