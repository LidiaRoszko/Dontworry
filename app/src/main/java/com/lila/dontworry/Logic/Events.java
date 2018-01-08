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
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static java.lang.Integer.valueOf;

/**
 * Created by Lidia on 08.01.2018.
 * Get async events from the web pages with urls
 */

public class Events extends AsyncTask<URL, Integer, Long> {

    ArrayList list = new ArrayList<Event>();

    @Override
    protected Long doInBackground(URL... urls) {
        int count = urls.length;
       if(count==1){ // TODO: for 3 days
           URL url = urls[0];
           InputStream is = null;
           String s = "";
           try {
               is = url.openStream();
           } catch (IOException e) {
               e.printStackTrace();
           }
           try( BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
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

                   Date date = new Date(); // your date
                   Calendar cal = Calendar.getInstance();
                   cal.setTime(date);
                   int hour = cal.get(Calendar.HOUR_OF_DAY);
                   for (int i = 0; i < n-1; i++) {
                       String link = link = url.toString();
                       try {
                           link = articles.get(i).select("a.link_article_more").first().attr("href");
                       }
                       catch(NullPointerException e){

                       }
                       Event event = new Event(dates.get(i).html(), articles.get(i).select("div.content_article").select("a").last().html(), articles.get(i).select("h2.title_event").first().html(),link);
                       this.list.add(event);

                   }
                   if (this.list.size() > 0) {
                       Singleton.getInstance(this.list);
                       System.out.println("new Singleton with events for " + cal.get(Calendar.DAY_OF_MONTH) + "." + cal.get(Calendar.MONTH)+1);
                   }
               }
           }
           catch (IOException e) {
               e.printStackTrace();
           }
       }
       return  null;
    }
}