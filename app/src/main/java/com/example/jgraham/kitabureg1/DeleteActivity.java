package com.example.jgraham.kitabureg1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jgraham.kitabureg1.database.MySQLiteDbHelper;

/*
    * Defined a class for debugging if the data is not deleted just
    *  to start an activity and see the records of an individual
    *  record.
    *
    *  Implemented in such a way just to avoid the annoyance of logging from everywhere.
 */

public class DeleteActivity extends AppCompatActivity {
    private static final String EMPTY_STRING = " "; // An instance variable to store the empty to avoid NullPointerException.
    private static MySQLiteDbHelper mySQLiteDbHelper; // An instance variable for the database.
    private EditText eid, title, link, tags; // edittexts to display contents of the individual record.
    private int id; // ID that is generated from web server.
    private Button delete_button;   // Delete button to delete the individual record from the list.


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        delete_button = (Button) findViewById(R.id.delete_button);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Long rowId = bundle.getLong("rowId");
        id = bundle.getInt("id");


        // Settings the title, link and tags on the textviews of the activity.
        String str_title = bundle.getString("title");
        String str_link = bundle.getString("link");
        String str_tags = bundle.getString("tags");

        // Setting the URL title
        title = (EditText) findViewById(R.id.edittext_title);
        title.setText(str_title == null ? EMPTY_STRING : str_title);

        // Setting the URL title
        link = (EditText) findViewById(R.id.edittext_link);
        link.setText(str_link == null ? EMPTY_STRING : str_link);

        // Setting the Tags for the URL.
        tags = (EditText) findViewById(R.id.edittext_tags);
        tags.setText(str_tags == null ? EMPTY_STRING : str_tags);

        // Getting the instance of the Database object.
        mySQLiteDbHelper = MySQLiteDbHelper.getInstance(getApplicationContext());
    }

    /*
       * Method to delete the individual record from the database
       * that is selected from the listview.
       *
     */
    public void delete_data(View view) {
        mySQLiteDbHelper.removeEntry(id);
        finish();
    }

}
