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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    protected EditText login_etxt;
    protected EditText pwd_etxt;
    protected String m_login_phone;
    protected String m_login_pwd;
    protected Button login_btn;

    /*
     * onCreate of the activity.
     * Not sure if we will do anything onSuspend...
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_etxt = (EditText) findViewById(R.id.login_phone);
        pwd_etxt = (EditText) findViewById(R.id.login_password);
        login_btn = (Button) findViewById(R.id.btn_login);
    }

    /*
     * Method to validate phone number and password.
     * setError when field is invalid.
     */
    boolean validate(String phone, String pwd) {
        boolean valid = true;
        if (phone.isEmpty() || phone.length() != 10) {
            login_etxt.setError("Enter Valid 10 digit Mobile Number!");
            valid = false;
        } else {
            login_etxt.setError(null);
        }
        if (pwd.isEmpty() || pwd.length() < 6) {
            pwd_etxt.setError("Password must be greater than 6 characters!");
            valid = false;
        } else {
            login_etxt.setError(null);
        }
        return valid;
    }
    // Login! pressed
    public void onLoginClick(View view) {

        login_btn.setEnabled(false);

        m_login_phone = login_etxt.getText().toString();
        m_login_pwd = pwd_etxt.getText().toString();
        if (validate(m_login_phone, m_login_pwd)) {
            LoginTask lgn_task = new LoginTask();
            lgn_task.execute();
        } else {
            login_btn.setEnabled(true);
        }
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
            intent.putExtra("sender", "login");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        login_btn.setEnabled(true);
        finish();
    }

    /*
     * If username/password was incorrect, this method is called.
     */
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

        /*
         * Let's get this done in the background.
         */
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
                    serv_res = ServerUtil.get("http://kitabu.prashant.at/api/sign_in",
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

            if (result == null || result.contains(tr)) {  // Did I get a null or a false?
                onLoginFailed();
            } else if (result.equals("502")) // ServerUtil returns this when Server down.
            {
                Log.d("LOGIN", "Came to 502");
                Toast.makeText(getApplicationContext(),
                        "Oops, looks like our server is down! Sorry! :(", Toast.LENGTH_LONG).show();
                onLoginFailed();
            } else {                    // If no errors anticipated, lets parse the json.
                try {
                    JSONObject response = new JSONObject(result);
                    onLoginPassed(response);
                } catch (JSONException e) {
                    Log.d("LOGIN", "JSONException...");
                    e.printStackTrace();
                }
            } // Else close
        } // PostExecute close
    } // Login Asynctask close
} // Login Activity close
