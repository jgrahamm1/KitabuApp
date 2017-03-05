package com.example.jgraham.kitabureg1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jgraham.kitabureg1.database.KitabuEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prashant on 3/4/17.
 */

public class MyCursorAdapter extends ArrayAdapter<KitabuEntry> {

    /*
     * Initially named it CursorAdapter, but then realised it was a bad idea to use CursorAdapter
     * and switched back to ArrayAdapter
     */
    private final Context context;
    private final List<KitabuEntry> itemsArrayList;
    public MyCursorAdapter(Context context, List<KitabuEntry> itemsArrayList) {
        super(context, R.layout.customlist, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.customlist, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.firstLine);
        TextView textView1 = (TextView) view.findViewById(R.id.secondLine);
        textView.setText(itemsArrayList.get(position).getmTitle());
        textView1.setText(itemsArrayList.get(position).getmLink());
        return view;
    }
}