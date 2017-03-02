package com.example.jgraham.kitabureg1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.w3c.dom.Text;

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

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    // Buttons
    protected Button login_button;
    protected Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
         * TODO: Aditi: Add a tutorial screen here.
         * TODO: Mahesh: Check for corner cases.
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Register listener for buttons
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(this);

        register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(this);

        // Get READ_CONTACTS permissions
        /*
         * TODO: Add Location also over here.
         */
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 0);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("Kitabu_preferences",
                Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        String name = sharedPreferences.getString("name", null);
        String email = sharedPreferences.getString("email", null);
        String phoneno = sharedPreferences.getString("phoneno", null);
        Log.d("id", String.valueOf(id));
        Log.d("name", String.valueOf(name));
        Log.d("email", String.valueOf(email));
        Log.d("phoneno", String.valueOf(phoneno));
        if(id != null && name != null && email !=null && phoneno != null)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("sender", "welcome"); // I check this flag to perform actions in MainActivity.
            startActivity(intent);
        }
    }

    /*
     * TODO: This is a corner case, when permissions weren't granted!
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    /*
     * If either of the buttons was clicked.
     */
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
