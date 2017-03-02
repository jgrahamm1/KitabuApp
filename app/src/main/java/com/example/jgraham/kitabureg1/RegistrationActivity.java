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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    protected EditText name_etxt;
    protected EditText email_etxt;
    protected EditText phone_etxt;
    protected EditText pass_etxt;
    protected EditText repass_etxt;

    protected String m_name;
    protected String m_email;
    protected String m_phone;
    protected String m_password;
    protected String m_repassword;

    protected Button reg_comp_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Set all of the EditTexts
        name_etxt = (EditText) findViewById(R.id.input_name);
        email_etxt = (EditText) findViewById(R.id.input_email);
        phone_etxt = (EditText) findViewById(R.id.input_phone);
        pass_etxt = (EditText) findViewById(R.id.input_password);
        repass_etxt = (EditText) findViewById(R.id.input_repassword);

        reg_comp_button = (Button) findViewById(R.id.complete_reg_btn);

    }

    // Registration! Button pressed
    public void sendRegistration(View view) {
        Log.d("REGISTRATION", "Register! button pressed");

        if (!validate()) {
            sendRegistrationFailed();
            return;
        }

        reg_comp_button.setEnabled(false);

        m_name = name_etxt.getText().toString();
        m_email = email_etxt.getText().toString();
        m_phone = phone_etxt.getText().toString();
        m_password = pass_etxt.getText().toString();
        m_repassword = repass_etxt.getText().toString();

        SendRegistrationTask send_task = new SendRegistrationTask();
        send_task.execute();
    }

    // When registration is successfully sent
    public void sendRegistrationPass() {
        reg_comp_button.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Successfully registered " + m_name, Toast.LENGTH_SHORT).show();
        finish();
    }

    // When validation does not pass
    public void sendRegistrationFailed() {
        Toast.makeText(getApplicationContext(), "Registration failed :(", Toast.LENGTH_SHORT).show();
        reg_comp_button.setEnabled(true);
    }

    // Validate the EditText fields
    public boolean validate() {
        boolean valid = true;

        String name = name_etxt.getText().toString();
        String email = email_etxt.getText().toString();
        String mobile = phone_etxt.getText().toString();
        String password = pass_etxt.getText().toString();
        String reEnterPassword = repass_etxt.getText().toString();

        if (name.isEmpty() || name.length() < 1) {
            name_etxt.setError("Enter at least 1 character");
            valid = false;
        } else {
            name_etxt.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_etxt.setError("Valid email address required");
            valid = false;
        } else {
            email_etxt.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            phone_etxt.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            phone_etxt.setError(null);
        }

        if (password.isEmpty() || password.length() < 1 || password.length() > 20) {
            pass_etxt.setError("Enter 1 to 20 alphanumeric characters");
            valid = false;
        } else {
            pass_etxt.setError(null);
        }

        if (reEnterPassword.isEmpty() || !(reEnterPassword.equals(password))) {
            repass_etxt.setError("Passwords need to match");
            valid = false;
        } else {
            repass_etxt.setError(null);
        }

        Log.d("REGISTRATION", "Validation result is " + valid);
        return valid;
    }

    /*
     * SendRegistrationTask:
     * AsyncTask for performing registration activity on network
     */

    class SendRegistrationTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        private String serv_res;

        protected String doInBackground(Void... arg0) {
            try {

                Log.d("REGISTRATION", "Sending --> name: " + m_name + " email: " +
                        m_email + " phone: " + m_phone +
                        " pass: " + m_password + " repass: " + m_repassword);

                // Put parameters in map
                Map<String, String> params = new HashMap<String, String>();
                params.put("phoneno", m_phone);
                params.put("password", m_password);
                params.put("password-confirmation", m_repassword);
                params.put("email", m_email);
                params.put("name", m_name);

                // Send to server
                try {
                    serv_res = ServerUtil.get("http://kitabu.prashant.at/api/register", params, getApplicationContext());
                } catch (IOException e) {
                    Log.d("REGISTER", "Sending to server did not work");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                this.exception = e;
                Log.d("REGISTRATION", "Async doInBackground failed");
                return null;
            }
            return serv_res;
        }

        protected void onPostExecute(String result) {
            Log.d("REGISTER", "onPostExecute got result: " + result);
            String tr = "true";
            /*
             * Check if true was returned.
             */
            if (result.contains(tr)) {
                sendRegistrationPass();
            }
            else {
                sendRegistrationFailed();
            }
        }
    }
}
