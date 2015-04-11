package com.dmtaiwan.alexander.recipes.Utilities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dmtaiwan.alexander.recipes.R;

import java.util.List;

/**
 * Created by Alexander on 4/6/2015.
 */
public class CustomSpinnerAdapter extends ArrayAdapter<String>{
    private Activity mContext;
    List<String> mItems;

    public CustomSpinnerAdapter(Activity context, int resource,List<String> items) {
        super(context, resource, items);
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_style,parent,false);
        }
        String item = mItems.get(position);
        TextView textItem = (TextView) row.findViewById(R.id.spinner_text);
        textItem.setText(item);
        return row;
    }
}
