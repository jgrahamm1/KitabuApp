package com.example.jgraham.kitabureg1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jgraham.kitabureg1.database.MySQLiteDbHelper;

/**
 * Created by maheshdevalla on 2/24/17.
 */

public class Tab3 extends Fragment{
    private static MySQLiteDbHelper mySQLiteDbHelper;
    ListView listview;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Tab3","Tab3");
        View rootView = inflater.inflate(R.layout.tab3, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
