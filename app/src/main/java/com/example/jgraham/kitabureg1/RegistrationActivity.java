package com.example.jgraham.kitabureg1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jgraham on 2/24/17.
 */

public class RegistrationActivity extends AppCompatActivity {

    protected EditText name_etxt;
    protected EditText email_etxt;
    protected EditText phone_etxt;
    protected EditText pass_etxt;
    protected EditText repass_etxt;

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
    }

    // Registration! Button pressed
    public boolean sendRegistration(View view) {
        Log.d("REGISTRATION", "Register! button pressed");



        return true;
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

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
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
}
