package com.lila.dontworry.Logic;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Lidia on 05.12.2017.
 */

public class Localisation {
    //dresden 51.06814779850998 13.763458840548992

    private static double l1 = 51.06814779850998;
    private static double l2 = 13.763458840548992;
    private static LatLng position = new LatLng(l1,l2);
    private static boolean change = true;
    private static Localisation instance;

    private Localisation () {}
    public static Localisation getInstance(LatLng l) {
        if (Localisation.instance == null) {
            Localisation.instance = new Localisation ();
        }
        setLocalisation(l);
        setChange(true);
        return Localisation.instance;
    }

    public static void setLocalisation(LatLng l){
        l1 = l.latitude;
        l2 = l.longitude;
        position = l;
    }

    public static void setChange(boolean change) {
        Localisation.change = change;
    }


    public static double getL1() {
        return l1;
    }

    public static double getL2() {
        return l2;
    }

    public static boolean isChange() {
        return change;
    }

    public static LatLng getPosition(){
            return position;
        }

    }

