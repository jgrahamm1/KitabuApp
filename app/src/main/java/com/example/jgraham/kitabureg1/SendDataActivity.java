package com.example.jgraham.kitabureg1;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.jgraham.kitabureg1.database.KitabuEntry;
import com.example.jgraham.kitabureg1.database.MySQLiteDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SendDataActivity extends AppCompatActivity {
    Button senddata_button;
    private EditText senddata_link,senddata_tags;
    private int flag=-2;

    // Store the info in EditTexts
    protected String link_str, tag_str;
    protected int typep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senddata);
        senddata_link = (EditText) findViewById(R.id.link);
        senddata_tags = (EditText) findViewById(R.id.tags);
    }

    // Onclick: called by pressing SendDataActivity Button
    public void sendData(View view){
        senddata_button = (Button) findViewById(R.id.senddata);

        // Get EditText info
        if(senddata_link.getText().length()==0){
            senddata_link.setError("URL is required");
        }
        if (senddata_tags.getText().length()==0){
            senddata_tags.setError("Description is required");
        }
        if(flag==-2){
            Toast.makeText(this, "Please select public or private", Toast.LENGTH_SHORT).show();
        }
        else {
            link_str = senddata_link.getText().toString();
            tag_str = senddata_tags.getText().toString();
            typep = flag;
        }


        // Send link to Prashant DB
        SendTask stask = new SendTask();
        stask.execute();
    }

    public void onRadioButtonClicked(View view){
        boolean temp = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_private:
                if (temp)
                    flag = 1;
                break;
            case R.id.radio_public:
                if (temp)
                    flag = 0;
                break;
        }
    }

    // Format URL for sending to Prashant DB
    protected String formatURL(String url) {
        String url_formatted = String.valueOf(url);
        if (!url.contains("http://") || !url.contains("https://")) {
            url_formatted = "http://" + url_formatted;
        }
        return url_formatted;
    }

    /*
 * LoginTask
 * AsyncTask for performing activity on network
 */
    class SendTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        private String serv_res;

        /*
         * Let's get this done in the background.
         */
        protected String doInBackground(Void... arg0) {
            try {

                // Get user phoneno
                SharedPreferences sharedPreferences = getSharedPreferences("Kitabu_preferences",
                        Context.MODE_PRIVATE);
                String phoneno_str = sharedPreferences.getString("phoneno", null);

                // Get EditText info
                // URL


                // Put parameters in map
                Map<String, String> params = new HashMap<String, String>();
                params.put("phoneno", phoneno_str);
                params.put("url", formatURL(link_str));
                params.put("typep", String.valueOf(typep));
                params.put("tag_list", tag_str);

                // Send to server
                try {
                    serv_res = ServerUtil.get("http://kitabu.prashant.at/api/add_link",
                            params, getBaseContext());
                } catch (IOException e) {
                    Log.d("LOGIN", "Sending to server did not work");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                this.exception = e;
                Log.d("LOGIN", "Async doInBackground failed");
                return null;
            }
            return serv_res;
        }

        /*
         * The background thing ended... so what now?
         */
        protected void onPostExecute(String result) {
            Log.d("LOGIN", "onPostExecute got result: " + result);
            String tr = "false";

            if (result == null || result.equals(tr)) {  // Did I get a null or a false?
                Log.d("SENDATA", "result == null || result was false");
            }
            else if(result.equals("502")) // ServerUtil returns this when Server down.
            {
                Log.d("LOGIN", "Came to 502");
                Toast.makeText(getApplicationContext(),
                        "Oops, looks like our server is down! Sorry! :(", Toast.LENGTH_LONG).show();
                //onLoginFailed();
            }
            else {                    // If no errors anticipated, lets parse the json.
                try {
                    JSONObject response = new JSONObject(result);
                    Log.d("SENDDATA", "JSONObject received: " + result.toString());

                    // Parse JSONObject and put info in SQLite DB
                    // Parse user info from JSON object
                    JSONObject link_json = response.getJSONObject("link");
                    JSONObject user_json = link_json.getJSONObject("user");
                    String phoneno = user_json.getString("phoneno");
                    String id = link_json.getString("id");
                    String url = link_json.getString("url");
                    String tag_list = link_json.getString("tag_list");
                    String type = link_json.getString("typep");
                    KitabuEntry k_entry;
                    if(type.equals("true"))
                    {
                        k_entry = new KitabuEntry(id, url, phoneno, tag_list, 1);
                    }
                    else
                    {
                        k_entry = new KitabuEntry(id, url, phoneno, tag_list, 0);
                    }

                    // Put entry in SQLite DB
                    MySQLiteDbHelper db_helper = new MySQLiteDbHelper(getApplicationContext());
                    long local_id = db_helper.insertEntry(k_entry);
                    Log.d("SENDDATA", "Put link in SQLite DB with id: " + local_id);
                    finish();

                } catch (JSONException e) {
                    Log.d("LOGIN", "JSONException...");
                    e.printStackTrace();
                }
            } // Else close
        } // PostExecute close
    } // Asynctask close
}
