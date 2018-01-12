package com.lila.dontworry.Logic;

import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * Created by Lidia on 08.01.2018.
 * Get async events from the web pages with urls
 */

public class EventAsync extends AsyncTask<URL, Integer, Long> {

    ArrayList list = new ArrayList<Event>();
    ArrayList days = new ArrayList<Calendar>();

    @Override
    protected Long doInBackground(URL... urls) {
        // check in DB which days have already events and return List with days which not have (to have +3 days)

            ArrayList<Calendar> results = new ArrayList<>();
            boolean d2 = false;
            boolean d1 = false;
            boolean d0 = false;
            Set<Calendar> set = null;
            int flag = 1;
            try{set = EventSingleton.getMap().keySet();}
            catch(NullPointerException e){
                flag = 0;
            }   Calendar now = Calendar.getInstance();

            if(flag==1) {


                for (Calendar c : set) {
                    now.setTime(new Date());
                    if (c.before(now)) {
                        //delete from DB
                    } else {
                        if (c.compareTo(now) == 0) {
                            d0 = true;
                            break;
                        }
                        now.add(Calendar.DAY_OF_MONTH, 1);
                        if (c.compareTo(now) == 0) {
                            d1 = true;
                        }

                        now.add(Calendar.DAY_OF_MONTH, 1);
                        if (c.compareTo(now) == 0) {
                            d2 = true;
                        }
                    }
                }
            }
                if (!d0) {
                    now.setTime(new Date());
                    results.add(now);
                }
               /* if (!d1) {
                    now.setTime(new Date());
                    now.add(Calendar.DAY_OF_MONTH, 1);
                    results.add(now);
                }
                if (!d2) {
                    now.setTime(new Date());
                    now.add(Calendar.DAY_OF_MONTH, 2);
                    results.add(now);}*/

                this.days = results;


        if (urls.length < 1) {
            return null;
        }
        int daysSize = 0;
        if(this.days!=null) {
            daysSize = this.days.size();
        }
        for (int i = 0; i < daysSize; i++) { // TODO: for 3 days
            this.list = new ArrayList();

            URL url = null;
            try {
                url = new URL(urls[0].toString() + changeToUrlEnd((Calendar) days.get(i)));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            InputStream is = null;
            String s = "";
            try {
                is = url.openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    s = s + line + "\n";
                }

                if (!s.equals("")) {
                    Document doc = Jsoup.parse(s);
                    Element body = doc.body().getElementsByTag("body").first();
                    Elements articles = body.select("article.box_article");

                    Elements places = body.select("div.content_article"); //.first().select("a").last().html()
                    Elements titles = body.select("h2.title_event");//.first().html()
                    Elements links = articles.select("a.link_article_more");//.first().attr("href")
                    Elements dates = body.select("span.item_article_time");//.first().html()

                    int n = dates.size();

                    for (int j = 0; j < n - 1; j++) {
                        String link = link = url.toString();
                        try {
                            link = articles.get(j).select("a.link_article_more").first().attr("href");
                        } catch (NullPointerException e) {

                        }
                        Event event = new Event(dates.get(j).html(), articles.get(j).select("div.content_article").select("a").last().html(), articles.get(j).select("h2.title_event").first().html(), link);
                        this.list.add(event);

                    }
                    if (this.list.size() > 0) {
                        EventSingleton.getInstance(this.list, (Calendar) days.get(i));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String changeToUrlEnd(Calendar c) {
        int month = c.get(Calendar.MONTH)+1;
        String monthString = String.valueOf(month);
        if(month<10){
            monthString = "0" + monthString;
        }
        int day = c.get(Calendar.DAY_OF_MONTH);
        String dayString = String.valueOf(day);
        if(day<10){
            dayString = "0" + dayString;
        }
        return c.get(Calendar.YEAR) + "-" + monthString + "-" + dayString;
    }

    private void checkDays() {
        // check in DB which days have already events and return List with days which not have (to have +3 days)
        if (EventSingleton.getMap() != null) {
            ArrayList<Calendar> results = new ArrayList<>();
            boolean d2 = false;
            boolean d1 = false;
            boolean d0 = false;
            Set<Calendar> set = null;
            try{set = EventSingleton.getMap().keySet();}
            catch(NullPointerException e){}
            Calendar now = Calendar.getInstance();

            for (Calendar c : set) {
                now.setTime(new Date());
                if (c.before(now)) {
                    //delete from DB
                } else {
                    if (c.compareTo(now) == 0) {
                        d0 = true;
                        break;
                    }
                    now.add(Calendar.DAY_OF_MONTH, 1);
                    if (c.compareTo(now) == 0) {
                        d1 = true;
                    }

                    now.add(Calendar.DAY_OF_MONTH, 1);
                    if (c.compareTo(now) == 0) {
                        d2 = true;
                    }
                }
            }
            if (!d0) {
                now.setTime(new Date());
                results.add(now);
            }
            if (!d1) {
                now.setTime(new Date());
                now.add(Calendar.DAY_OF_MONTH, 1);
                results.add(now);
            }
            if (!d2) {
                now.setTime(new Date());
                now.add(Calendar.DAY_OF_MONTH, 2);
                results.add(now);
            }
            this.days = results;
        }
    }

}