package com.lila.dontworry;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lila.dontworry.Logic.CustomAdapter;
import com.lila.dontworry.Logic.Event;
import com.lila.dontworry.Logic.Localisation;
import com.lila.dontworry.Logic.Singleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback { //TODO: change orientation
    ListView listView;
    OnMapReadyCallback context = this;
    ArrayList<Event> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.list = Singleton.getList();
        setContentView(R.layout.activity_event);
        listView = findViewById(R.id.eventList);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo Wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && Wifi.isConnected()){
            SupportMapFragment supportMapFragment;
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
            supportMapFragment.onAttach(this);
            supportMapFragment.getMapAsync(context);
        }
        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Wifi.isConnected()){
            SupportMapFragment supportMapFragment;
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
            supportMapFragment.onDestroy();
        }

        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadList() {
        customAdapter();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Uri uriUrl = Uri.parse(list.get(position).getLink());
                view.setSelected(true);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        uriUrl
                );
                startActivity(browserIntent);
            }
        });
    }

    private void customAdapter() {
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> places = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();

        if(this.list!=null) {
            for (Event e : this.list) {
                dates.add(e.getDate());
                places.add(e.getPlace());
                titles.add(e.getTitle());
            }
        }
        CustomAdapter adapter = new CustomAdapter(this, dates, places, titles);
        listView.setAdapter(adapter);
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        mMap.setMinZoomPreference(10);
        mMap.setMaxZoomPreference(20);
        LatLng pos = Localisation.getPosition();
        mMap.addMarker(new MarkerOptions().position(pos).title("Your localisation!").icon(getMarkerIcon("#e6ff40")));
        if(this.list!=null) {
            for (Event e : this.list) {
                if(e.getPos()==null){
                    e.setPos(checkLatLng(e.getPlace()));
                }
                mMap.addMarker(new MarkerOptions().position(e.getPos()).title(e.getTitle()).icon(getMarkerIcon("#000000"))).setSnippet(e.getPlace() + ", " + e.getDate());
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        /*
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getTitle().equals("Your localisation!")){return false;}
                listView.performItemClick(listView, checkPosition(marker.getTitle()), listView.getItemIdAtPosition(checkPosition(marker.getTitle())));
                return false;
            }
        });*/
    }
    private LatLng checkLatLng(String place) {
        LatLng l = Localisation.getPosition();
        if(Geocoder.isPresent()){
            try {
                String location = place + " Dresden";
                Geocoder gc = new Geocoder(this);
                List<Address> addresses = gc.getFromLocationName(location, 1);

                 for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                       l = new LatLng(a.getLatitude(), a.getLongitude());
                    }
                }
            } catch (IOException e) {
            }
        }
        return l;
    }

    // changing color of tags
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}