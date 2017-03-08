package com.example.jgraham.kitabureg1;

import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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


public class Tab1 extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<KitabuEntry>>, ServiceConnection {
    public static LoaderManager loaderManager;
    public static FragmentActivity context;
    public static int onCreateCheck = 0;
    public GcmBroadcastReceiver broadcastReceiver;
    private static MySQLiteDbHelper mySQLiteDbHelper;
    ListView listview;
    MyCursorAdapter  adapter;
    static List<KitabuEntry> values;

    // Service bound flag
    public boolean mBound;
    public ServiceConnection mConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getActivity();
        super.onCreate(savedInstanceState);
        Log.d("Tab3", "oncreate");
        loaderManager = getActivity().getLoaderManager();
        loaderManager.initLoader(1, null, this).forceLoad();
    }

    public void updateEntries() {
        loaderManager.initLoader(1, null, this).forceLoad();
        Log.d("Tab1", "intloader");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("called", "onCreateView: ");
        mySQLiteDbHelper = MySQLiteDbHelper.getInstance(getContext());
        View rootView = inflater.inflate(R.layout.tab1, container, false);
        values = mySQLiteDbHelper.fetchPrivateEntries();
        adapter = new MyCursorAdapter(getContext(), values, 1);
        ListView lv = (ListView) rootView.findViewById(R.id.datalist);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv.invalidateViews();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KitabuEntry ke = adapter.getItem(position);
                Log.d("type", String.valueOf(ke.getmType()));
                Intent intent = new Intent(getActivity(), DeleteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("RowID", ke.getmRowID());
                bundle.putInt("id", ke.getmId());
                bundle.putString("title", ke.getmTitle());
                bundle.putString("link", ke.getmLink());
                bundle.putString("tags", ke.getmTags());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if (values.size() == 0) {
            TextView textView = (TextView) rootView.findViewById(R.id.tv1);
            textView.setVisibility(View.VISIBLE);
            textView.setText("Nothing to show!");
        }
        adapter.notifyDataSetChanged();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(broadcastReceiver, new IntentFilter("gcm_unique"));
        Log.d("called", "onResume: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("called", "onDestroy: ");
    }

    @Override
    public Loader<ArrayList<KitabuEntry>> onCreateLoader(int id, Bundle args) {

        return new KitabuLoader(getContext());

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<KitabuEntry>> loader, ArrayList<KitabuEntry> data) {
        values = data;
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(data);
            adapter.notifyDataSetChanged();
        }

        // Remove the TextView
        if (!values.isEmpty()) {
            TextView nts_view = (TextView) context.findViewById(R.id.tv1);
            nts_view.setVisibility(View.INVISIBLE);
            nts_view.setText(" ");
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<KitabuEntry>> loader) {
        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
    }

    /*
     * Lifecycle methods added to register GcmIntentService to the MainActivity
     * Having
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d("SERVICE", "Tab1 --> onStart binding service");
        // Bind to LocalService
        Intent intent = new Intent(getContext(), GcmIntentService.class);
        getActivity().getApplicationContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            getActivity().getApplicationContext().unbindService(mConnection);
            mBound = false;
        }
    }

    /*
     * Service methods
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        GcmIntentService.GcmBind binder = (GcmIntentService.GcmBind) service;
        mBound = true;
        Log.d("SERVICE", "Tab1 --> onServiceConnected!!!!!!!!!!!!!!!");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d("SERVICE", "Tab1 --> onServiceDisconnected");
    }
}
