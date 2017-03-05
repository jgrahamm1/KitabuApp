package com.example.jgraham.kitabureg1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jgraham.kitabureg1.database.KitabuEntry;
import com.example.jgraham.kitabureg1.database.MySQLiteDbHelper;

import java.util.List;

/**
 * Created by maheshdevalla on 2/24/17.
 */

public class Tab1 extends Fragment {
    private static MySQLiteDbHelper mySQLiteDbHelper;
    ListView listview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mySQLiteDbHelper = new MySQLiteDbHelper(getContext());
        View rootView = inflater.inflate(R.layout.tab1, container, false);
        List<KitabuEntry> values = mySQLiteDbHelper.fetchPrivateEntries();
        //final ArrayAdapter<KitabuEntry> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_expandable_list_item_1,values);
        final MyCursorAdapter adapter = new MyCursorAdapter(getContext(), values);
        ListView lv= (ListView) rootView.findViewById(R.id.datalist);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KitabuEntry ke = adapter.getItem(position);
//                Log.d("position",String.valueOf(position));
//                Log.d("ID in list view", String.valueOf(ke.getmId()));
//                Log.d("Link in list view", ke.getmLink());
//                Log.d("RowId in list view",String.valueOf(ke.getmRowID()));

                Log.d("type",String.valueOf(ke.getmType()));
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
