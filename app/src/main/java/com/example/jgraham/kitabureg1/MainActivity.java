package com.example.jgraham.kitabureg1;

/*

 .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.
| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |
| |  ___  ____   | || |     _____    | || |  _________   | || |      __      | || |   ______     | || | _____  _____ | |
| | |_  ||_  _|  | || |    |_   _|   | || | |  _   _  |  | || |     /  \     | || |  |_   _ \    | || ||_   _||_   _|| |
| |   | |_/ /    | || |      | |     | || | |_/ | | \_|  | || |    / /\ \    | || |    | |_) |   | || |  | |    | |  | |
| |   |  __'.    | || |      | |     | || |     | |      | || |   / ____ \   | || |    |  __'.   | || |  | '    ' |  | |
| |  _| |  \ \_  | || |     _| |_    | || |    _| |_     | || | _/ /    \ \_ | || |   _| |__) |  | || |   \ `--' /   | |
| | |____||____| | || |    |_____|   | || |   |_____|    | || ||____|  |____|| || |  |_______/   | || |    `.__.'    | |
| |              | || |              | || |              | || |              | || |              | || |              | |
| '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |
 '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'

 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgraham.backend.myApi.MyApi;
import com.example.jgraham.kitabureg1.database.KitabuEntry;
import com.example.jgraham.kitabureg1.database.MySQLiteDbHelper;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "Kitabu_preferences";
    Tab1 tab1;
    Tab2 tab2;
    Tab3 tab3;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab1 = new Tab1();
        tab2 = new Tab2();
        tab3 = new Tab3();


    /*
     * If I want to use Runnable, I should be allowed to use runnable
     * Noone on earth should have the right to stop me from doing what I want!
     * ~ PA
     */

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT);
                // Get the Snackbar's layout view
                Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
                // Hide the text
                TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
                textView.setVisibility(View.INVISIBLE);

                // Inflate our custom view

                View snackView = getLayoutInflater().inflate(R.layout.snackbar_layout, null);

                // Add the view to the Snackbar's layout
                layout.addView(snackView, 0);
                // Show the Snackbar
                snackbar.show();

            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            String sender = intent.getStringExtra("sender");
            if (sender != null && sender.equals("login")) {
                SharedPreferences sharedPreferences = getSharedPreferences("Kitabu_preferences",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id", intent.getStringExtra("id"));
                editor.putString("phoneno", intent.getStringExtra("phoneno"));
                editor.putString("email", intent.getStringExtra("email"));
                editor.putString("name", intent.getStringExtra("name"));
                editor.commit();

                /*
                  TODO: Add ShowCaseView here.
                */
                new ShowcaseView.Builder(this)
                        //.setTarget(new ActionViewTarget(this, ActionViewTarget.Type.TITLE))
                        .setTarget(new Target() {
                            @Override
                            public Point getPoint() {
                                return new Point(100, 100);
                            }
                        })
                        .setContentTitle("ShowcaseView")
                        .setContentText("This is highlighting the Home button")
                        .hideOnTouchOutside()
                        .build();

            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("Kitabu_preferences",
                        Context.MODE_PRIVATE);
                String name = sharedPreferences.getString("name", null);
                if (name != null)
                    Toast.makeText(this, "Welcome back, " + name, Toast.LENGTH_SHORT).show();
            }
            new GcmRegistrationAsyncTask(this).execute();
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Log.d("SELECTED", "from main method");
                        tab1.updateEntries();
                        break;
                    case 1:
//                        Log.d("SELECTED", String.valueOf(tab.getPosition()));
                        tab2.updateEntries();
                        break;
                    case 2:
//                        Log.d("SELECTED", String.valueOf(tab.getPosition()));
                        tab3.updateEntries();
                        break;
                    default:
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                Log.d("UNSELECTED",  String.valueOf(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                Log.d("RESELECTED", String.valueOf(tab.getPosition()));
                switch (tab.getPosition()) {
                    case 0:
                        tab1.updateEntries();
                        break;
                    case 1:
                        tab2.updateEntries();
                        break;
                    case 2:
                        tab3.updateEntries();
                        break;
                    default:
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
     * Clear all shared preferences and go to the Welcome Screen.
     */
    void signout() {
        SharedPreferences sharedPreferences = getSharedPreferences("Kitabu_preferences",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        Toast.makeText(this, "Sign out successful,\n sorry to see you go! :(", Toast.LENGTH_SHORT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void onAddReminderClicked(View view) {
        Toast.makeText(getApplicationContext(), "Add Reminder Clicked", Toast.LENGTH_SHORT).show();
    }

    public void onSaveLinkClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), SendDataActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_signout) {
            signout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
//                    tab1 = new Tab1();
                    Log.d("Tab1", "Reselected: ");
                    return tab1;
                case 1:
                    Log.d("Tab1", "Reselected: ");
//                    tab2 = new Tab2();
                    return tab2;
                case 2:
                    Log.d("Tab1", "Reselected: ");
//                    tab3 = new Tab3();
                    return tab3;
            }
            return null;

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Private";
                case 1:
                    return "Public";
                case 2:
                    return "Notifications";
            }
            return null;
        }
    }
}


/*
 * This asynctask handles GCM registration.
 * GCM server is stored on AppSpot.
 * Send a post request to the server.
 */
class GcmRegistrationAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String SENDER_ID = "609569899467";
    private MyApi regService = null;
    private GoogleCloudMessaging gcm;
    private Context context;

    public GcmRegistrationAsyncTask(Context context) {
        this.context = context;
    }


    /*
 * Get the most recent id from the links sqlite table.
 * Send a post request and use the JSON response.
 */
    void fetchdata() {
        SharedPreferences sharedPreference = context.getSharedPreferences("Kitabu_preferences",
                Context.MODE_PRIVATE);
        boolean msg = false;
        MySQLiteDbHelper mySQLiteDbHelper = new MySQLiteDbHelper(context);
        mySQLiteDbHelper.deleteallprivate();
        int lastid = mySQLiteDbHelper.getLastId();
        String phoneno = sharedPreference.getString("phoneno", null);
        String serv_res = "";
        Map<String, String> params = new HashMap<String, String>();
        try {
            serv_res = ServerUtil.get("http://kitabu.prashant.at/api/getlinks/" + lastid + "/" + phoneno, params, context);
            if (serv_res.equals("false")) {
                msg = false;
            } else {
                try {

                    JSONObject response = new JSONObject(serv_res);
                    Log.d("JSON Parsed", response.toString());
                    JSONArray publicArray = response.getJSONArray("public");
                    for (int i = 0; i < publicArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) publicArray.get(i);
                        KitabuEntry entry = new KitabuEntry(jsonObject);
                        mySQLiteDbHelper.insertEntry(entry);
                        Log.d("JSON", jsonObject.toString());
                    }
                    Log.d("JSON Parsed", publicArray.toString());
                    JSONArray privateArray = response.getJSONArray("private");
                    for (int i = 0; i < privateArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) privateArray.get(i);
                        KitabuEntry entry = new KitabuEntry(jsonObject);
                        mySQLiteDbHelper.insertEntry(entry);
                        Log.d("JSON", jsonObject.toString());
                    }
                    Log.d("JSON Parsed", privateArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("SERVER MESSAGE", serv_res);
                msg = true;
            }
        } catch (IOException e) {
            Log.d("LOGIN", "Sending to server did not work");
            e.printStackTrace();
            msg = false;
        }
    }

    /*
     * Returns true or false.
     * Sends a post request.
     */
    boolean register_gcm(String regId) {
        SharedPreferences sharedPreference = context.getSharedPreferences("Kitabu_preferences",
                Context.MODE_PRIVATE);
        boolean msg = false;
        String phoneno = sharedPreference.getString("phoneno", null);
        String serv_res = "";
        if (phoneno != null) {

            Map<String, String> params = new HashMap<String, String>();
            params.put("phoneno", phoneno);
            params.put("regId", regId);

            // Send to server
            try {
                serv_res = ServerUtil.get("http://kitabu.prashant.at/api/gcm", params, context);
                if (serv_res.contains("false")) {
                    msg = false;
                } else if (serv_res.contains("true")) {
                    msg = true;
                }
            } catch (IOException e) {
                Log.d("LOGIN", "Sending to server did not work");
                e.printStackTrace();
                msg = false;
            }
        }
        return msg;
    }

    /*
     * Inspired from sample code provided by XD.
     */
    @Override
    protected String doInBackground(Void... params) {
        fetchdata();
        if (regService == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("https://kitabu-dartmouth.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end of optional local run code
            regService = builder.build();
        }

        String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            String regId = gcm.register(SENDER_ID);
            SharedPreferences sharedPreferences = context.getSharedPreferences("Kitabu_preferences",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("regId", regId);
            editor.commit();
            // Receive the regid
            // Send the regid to our server API
            // Server stores our regid with the user table
            // Server can now send push messages
            if (regId != null) {
                if (register_gcm(regId)) {
                    msg = "Login Successful!";
                } else {
                    msg = "Login unsuccessful!";
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            msg = "Error: " + ex.getMessage();
        }
        return msg;
    }


    /*
     * Was registration successful?
     */
    @Override
    protected void onPostExecute(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
    }
}
