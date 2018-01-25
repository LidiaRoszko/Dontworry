package com.lila.dontworry.Logic;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Lidia on 07.01.2018.
 */

public class Event {
    private String date;
    private String place;
    private String title;
    private String link;
    private LatLng pos;

    public Event() {}

    public Event(String date, String place, String title, String link) {
        this.date = date;
        this.place = place;
        this.title = title;
        this.link = link;
        this.pos = null;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LatLng getPos() {
        return pos;
    }

    public void setPos(LatLng pos) {
        this.pos = pos;
        System.out.println("Event " + this.title + " has a position " + pos.latitude + ", " + pos.longitude);
    }
}
