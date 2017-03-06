package com.example.jgraham.kitabureg1;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.jgraham.kitabureg1.database.KitabuEntry;
import com.example.jgraham.kitabureg1.database.MySQLiteDbHelper;

import java.util.ArrayList;

/**
 * Created by maheshdevalla on 3/5/17.
 */

public class Kitabu1Loader extends AsyncTaskLoader<ArrayList<KitabuEntry>> {

    Context mContext;
    public Kitabu1Loader(Context context)
    {
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
        ArrayList<KitabuEntry> values = helper.fetchPublicEntries();
        return values;
    }
}
