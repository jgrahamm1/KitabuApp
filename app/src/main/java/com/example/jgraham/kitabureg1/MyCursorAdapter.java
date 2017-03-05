package com.example.jgraham.kitabureg1;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.customlist, parent, false);

            TextView textView = (TextView) view.findViewById(R.id.firstLine);
            TextView textView1 = (TextView) view.findViewById(R.id.secondLine);
            textView.setText(itemsArrayList.get(position).getmTitle());
            textView1.setText(itemsArrayList.get(position).getmLink());
        // Delete button onClick Listener
            final ImageButton button = (ImageButton) view.findViewById(R.id.delete_button);
            button.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    Log.d("Does delete work", String.valueOf(position));
                }
            });
        // Share button onClick listener
        final ImageButton button1 = (ImageButton) view.findViewById(R.id.share_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Does share work", String.valueOf(position));
            }
        });
        // Reminder button onClick listener
        final ImageButton button2 = (ImageButton) view.findViewById(R.id.reminder_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Does reminder work", String.valueOf(position));
                Intent s_intent = new Intent(getContext(), SendDataActivity.class);
                int url_id = itemsArrayList.get(position).getmId();
                s_intent.putExtra("position", position);
                s_intent.putExtra("url_id", url_id);
                v.getContext().startActivity(s_intent);
            }
        });
        return view;
    }
}