package com.example.jgraham.kitabureg1;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.jgraham.kitabureg1.database.KitabuEntry;
import com.example.jgraham.kitabureg1.database.MySQLiteDbHelper;

import java.util.ArrayList;


/*
    * Defined a class to handle the dynamic listview population
    * for the public entries i.e. last 20 inserted records.
 */


public class KitabuLoader extends AsyncTaskLoader<ArrayList<KitabuEntry>> {

    Context mContext;

    public KitabuLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<KitabuEntry> loadInBackground() {
        MySQLiteDbHelper helper = MySQLiteDbHelper.getInstance(getContext());
        ArrayList<KitabuEntry> values = helper.fetchPrivateEntries();
        return values;
    }
}
