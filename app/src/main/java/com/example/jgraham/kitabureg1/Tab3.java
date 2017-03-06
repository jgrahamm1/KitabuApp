package com.example.jgraham.kitabureg1;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jgraham.kitabureg1.database.KitabuEntry;
import com.example.jgraham.kitabureg1.database.MySQLiteDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheshdevalla on 2/24/17.
 */

public class Tab3 extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<KitabuEntry>>{
    private static MySQLiteDbHelper mySQLiteDbHelper;
    ListView listview;
    MyCursorAdapter adapter;
    List<KitabuEntry> values;
    public static LoaderManager loaderManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loaderManager = getActivity().getLoaderManager();
        loaderManager.initLoader(1, null, this).forceLoad();

    }
    public void updateEntries()
    {
        loaderManager.initLoader(1, null, this).forceLoad();
        Log.d("Tab3", "Tab3");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mySQLiteDbHelper = MySQLiteDbHelper.getInstance(getContext());
        View rootView = inflater.inflate(R.layout.tab3, container, false);
        values = mySQLiteDbHelper.fetchNotificationEntries();
        adapter = new MyCursorAdapter(getContext(), values);
        ListView lv= (ListView) rootView.findViewById(R.id.datalisttab3);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("position",String.valueOf(position));
                KitabuEntry ke = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), DeleteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("RowID",ke.getmRowID());
                bundle.putInt("id",ke.getmId());
                bundle.putString("title",ke.getmTitle());
                bundle.putString("link",ke.getmLink());
                bundle.putString("tags",ke.getmTags());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if(values.size() == 0)
        {
            TextView textView = (TextView) rootView.findViewById(R.id.tvi);
            textView.setVisibility(View.VISIBLE);
            textView.setText("Nothing to show!");
        }
        adapter.notifyDataSetChanged();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("called", "onResume: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("called", "onDestroy: ");
    }

    @Override
    public Loader<ArrayList<KitabuEntry>> onCreateLoader(int id, Bundle args) {
        return new Kitabu2Loader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<KitabuEntry>> loader, ArrayList<KitabuEntry> data) {
        adapter.clear();
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<KitabuEntry>> loader) {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }
}
