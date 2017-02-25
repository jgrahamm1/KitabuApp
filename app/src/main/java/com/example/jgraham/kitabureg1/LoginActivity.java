package com.example.jgraham.kitabureg1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jgraham on 2/24/17.
 */

public class LoginActivity extends AppCompatActivity {

    protected EditText login_etxt;
    protected EditText pwd_etxt;

    protected String m_login_phone;
    protected String m_login_pwd;

    protected Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_etxt = (EditText) findViewById(R.id.login_phone);
        pwd_etxt = (EditText) findViewById(R.id.login_password);

        login_btn = (Button) findViewById(R.id.btn_login);
    }

    // Login! pressed
    public void onLoginClick(View view) {

        login_btn.setEnabled(false);

        m_login_phone = login_etxt.getText().toString();
        m_login_pwd = pwd_etxt.getText().toString();

        LoginTask lgn_task = new LoginTask();
        lgn_task.execute();
    }

    public void onLoginPassed(JSONObject json_obj) {
        try {

            // Parse user info from JSON object
            JSONObject user = json_obj.getJSONObject("user");
            String id = user.getString("id");
            String phoneno = user.getString("phoneno");
            String email = user.getString("email");
            String name = user.getString("name");

            Log.d("LOGIN", "Got user info --> id " + id + " phn: " + phoneno + " email: " + email +
            " name: " + name);

            // Start MainActivity with user info in Extras
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("phoneno", phoneno);
            intent.putExtra("email", email);
            intent.putExtra("name", name);
            startActivity(intent);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        login_btn.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        login_btn.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
    }

    /*
     * LoginTask
     * AsyncTask for performing activity on network
     */
    class LoginTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        private String serv_res;

        protected String doInBackground(Void... arg0) {
            try {

                Log.d("LOGIN", "Sending --> login: " + m_login_phone + " pwd: " +
                        m_login_pwd);

                // Put parameters in map
                Map<String, String> params = new HashMap<String, String>();
                params.put("phoneno", m_login_phone);
                params.put("password", m_login_pwd);

                // Send to server
                try {
                    serv_res = ServerUtil.get("http://kitabu.prashant.at/api/sign_in", params);
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

        protected void onPostExecute(String result) {
            // TODO: check this.exception
            // TODO: do something with the result
            Log.d("LOGIN", "onPostExecute got result: " + result);
            String tr = "false";

            if (result.contains(tr)) {
                onLoginFailed();
            }
            else {
                try {
                    JSONObject response = new JSONObject(result);
                    onLoginPassed(response);
                } catch (JSONException e) {
                    Log.d("LOGIN", "JSONException...");
                    e.printStackTrace();
                }
            }
        }
    }
}
