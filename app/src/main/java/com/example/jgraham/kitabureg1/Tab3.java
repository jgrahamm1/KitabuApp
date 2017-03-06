package com.example.jgraham.kitabureg1;

import android.content.Intent;
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

import java.util.List;

/**
 * Created by maheshdevalla on 2/24/17.
 */

public class Tab3 extends Fragment {
    private static MySQLiteDbHelper mySQLiteDbHelper;
    ListView listview;
    MyCursorAdapter adapter;
    List<KitabuEntry> values;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void updateEntries()
    {
        values = mySQLiteDbHelper.fetchPrivateEntries();
        adapter.notifyDataSetChanged();
        Log.d("Tab3", "Tab3");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mySQLiteDbHelper = MySQLiteDbHelper.getInstance(getContext());
        View rootView = inflater.inflate(R.layout.tab3, container, false);
        values = mySQLiteDbHelper.fetchNotificationEntries();
        //final ArrayAdapter<KitabuEntry> adapter = new ArrayAdapter<>(getActivity(), R.layout.customlist ,values);
        adapter = new MyCursorAdapter(getContext(), values);
        ListView lv= (ListView) rootView.findViewById(R.id.datalisttab3);
        lv.setAdapter(adapter);
        if(values.size() == 0)
        {
            TextView textView = (TextView) rootView.findViewById(R.id.tvi);
            textView.setVisibility(View.VISIBLE);
            textView.setText("Nothing to show!");
        }
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
        return rootView;
    }

}
