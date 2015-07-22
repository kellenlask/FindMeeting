package com.bk.fm.findmeeting;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/*
*   The following is the custom ListView ArrayAdapter that allows for color-coding of the
*   availability/obligation entries on the Availability Summary Page
*   (PersonalSummary.java / activity_availability.xml)
 */

public class AvailabilityArrayAdapter extends ArrayAdapter {

    List<String> values;

    public AvailabilityArrayAdapter(Context context, List<String> values) {
		super(context, android.R.layout.simple_list_item_1, values);
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView text = (TextView) view;

        if (getItem(position).toString().contains("Availability"))
        {
            view.setBackgroundColor(Color.GREEN);
        }
        else if (getItem(position).toString().contains("Obligation"))
        {
            view.setBackgroundColor(Color.RED);
        }

        text.setText(values.get(position));

        return view;
    }
}