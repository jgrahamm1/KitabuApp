package com.example.jgraham.kitabureg1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by jgraham on 2/24/17.
 */

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    // Buttons
    protected Button login_button;
    protected Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Register listener for buttons
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(this);

        register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(this);

        // Get READ_CONTACTS permissions
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 0);
        }

    }

    public static boolean checkPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onClick(View v) {
        // Determine if user pressed 'Login' or 'Register'
        switch(v.getId()) {

            // Login Button
            case R.id.login_button:
                Intent login_intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(login_intent);
                break;

            // Register Button
            case R.id.register_button:
                Intent reg_intent = new Intent(v.getContext(), RegistrationActivity.class);
                startActivity(reg_intent);
                break;

            default:
                Log.d("ERROR", "WelcomeActivity OnClickListener does not recognize this View");
        }
    }
}
