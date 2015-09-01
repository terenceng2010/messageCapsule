package com.terence;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherConditionItemAdapter extends ArrayAdapter<WeatherConditionItem> {
	private final Context mContext;
	
	private WeatherConditionItem[] items;
	public WeatherConditionItemAdapter(Context context, int resource, WeatherConditionItem[] objects) {
		super(context, resource, objects);
		this.items = objects; 
		// TODO Auto-generated constructor stub
		mContext = context;
	}


	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //User super class to create the View
        View v = super.getView(position, convertView, parent);
        ImageView Iv = (ImageView)v.findViewById(android.R.id.icon);

        //Put the image on the TextView
        Iv.setBackgroundResource(items[position].icon);

        return v;
    }



	

}
