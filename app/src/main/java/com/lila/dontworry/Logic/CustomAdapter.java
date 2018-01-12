package com.lila.dontworry.Logic;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lila.dontworry.R;
import java.util.ArrayList;

/**
 * Created by Lidia on 08.01.2018.
 * Create a new Adapter to set in TextView textViewdp and textViewt in EventActivty:
 * Title of event
 * Place, Date
 */

public class CustomAdapter extends android.widget.ArrayAdapter<String> {
    private ArrayList<String> dates;
    private ArrayList<String> places;
    private ArrayList<String> titles;
    private Activity context;

    public CustomAdapter(@NonNull Activity context, ArrayList<String> d,  ArrayList<String> p,  ArrayList<String> t) {
        super(context, R.layout.event, t);
        this.context = context;
        this.dates = d;
        this.places = p;
        this.titles = t;
}
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;
        if(view == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.event, null, true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textViewdp.setText(this.places.get(position) + ", " + this.dates.get(position));
        viewHolder.textViewt.setText(this.titles.get(position));
        return view;
    }

    class ViewHolder
    {
        TextView textViewdp;
        TextView textViewt;
        ViewHolder(View v){
            textViewdp = v.findViewById(R.id.textViewdp);
            textViewt = v.findViewById(R.id.textViewt);
        }
    }
}