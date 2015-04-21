package com.bk.fm.findmeeting;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        if (getItem(position).toString().contains("availability"))
        {
            view.setBackgroundColor(Color.GREEN);
        }
        else if (getItem(position).toString().contains("obligation"))
        {
            view.setBackgroundColor(Color.RED);
        }

        text.setText(values.get(position));

        return view;
    }
}