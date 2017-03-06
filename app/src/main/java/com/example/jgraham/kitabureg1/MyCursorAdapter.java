package com.example.jgraham.kitabureg1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgraham.kitabureg1.database.KitabuEntry;
import com.example.jgraham.kitabureg1.database.MySQLiteDbHelper;

import java.util.List;

import static com.google.android.gms.wearable.DataMap.TAG;


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
    MySQLiteDbHelper mySQLiteDbHelper;
    KitabuEntry ke;
    KitabuEntry kes;
    public MyCursorAdapter(Context context, List<KitabuEntry> itemsArrayList) {
        super(context, R.layout.customlist, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
        Log.d(TAG, "MyCursorAdapter: ");
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final Tab1 tab1 = new Tab1();
//        Log.d("tab1", "tab1 called");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.customlist, parent, false);
        mySQLiteDbHelper = MySQLiteDbHelper.getInstance(getContext());

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
                // Log.d("Does delete work", String.valueOf(position));
                ke = getItem(position);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage("Confirm Delete");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mySQLiteDbHelper.removeEntry(ke.getmId());
//                        clear();
                        itemsArrayList.remove(ke);
                        addAll(itemsArrayList);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                notifyDataSetChanged();
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                notifyDataSetChanged();

                alertDialog.show();
                }

            });
        // Share button onClick listener
        final ImageButton button1 = (ImageButton) view.findViewById(R.id.share_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage("Share?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ke = getItem(position);
                        SharedPreferences sharedPreferences = context.getSharedPreferences(
                                "Kitabu_preferences",
                                Context.MODE_PRIVATE);
                        String phoneno = sharedPreferences.getString("phoneno", null);
                        ContactsUtil contactsUtil = new ContactsUtil(context);
                        contactsUtil.sendContacts(ke.getmId(), phoneno);
                        Toast.makeText(context, "Shared!", Toast.LENGTH_SHORT).show();

                    }
                });
                notifyDataSetChanged();
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                notifyDataSetChanged();
                alertDialog.show();
            }
        });
        // Reminder button onClick listener
        final ImageButton button2 = (ImageButton) view.findViewById(R.id.reminder_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Does reminder work", String.valueOf(position));
                Intent s_intent = new Intent(getContext(), AlarmScheduleActivity.class);
                int url_id = itemsArrayList.get(position).getmId();
                s_intent.putExtra("position", position);
                s_intent.putExtra("id", url_id);
                v.getContext().startActivity(s_intent);
            }
        });
        notifyDataSetChanged();
        return view;
    }
}