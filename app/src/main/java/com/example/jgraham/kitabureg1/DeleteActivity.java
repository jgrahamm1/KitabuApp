package com.example.jgraham.kitabureg1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jgraham.kitabureg1.database.KitabuEntry;
import com.example.jgraham.kitabureg1.database.MySQLiteDbHelper;

import java.util.ArrayList;


public class DeleteActivity extends AppCompatActivity {
    private EditText eid,title,link,tags;
    private static MySQLiteDbHelper mySQLiteDbHelper;
    private int id;
    private static final String EMPTY_STRING= " ";
    private Button delete_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        delete_button = (Button) findViewById(R.id.delete_button);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Long rowId = bundle.getLong("rowId");
        id = bundle.getInt("id");
        String str_title = bundle.getString("title");
        String str_link = bundle.getString("link");
        String str_tags = bundle.getString("tags");
//        eid = (EditText) findViewById(R.id.edittext_id);
//        eid.setText(id);

        title = (EditText) findViewById(R.id.edittext_title);
        title.setText(str_title==null?EMPTY_STRING:str_title);

        link = (EditText) findViewById(R.id.edittext_link);
        link.setText(str_link==null?EMPTY_STRING:str_link);

        tags = (EditText) findViewById(R.id.edittext_tags);
        tags.setText(str_tags==null?EMPTY_STRING:str_tags);

        mySQLiteDbHelper = new MySQLiteDbHelper(getApplicationContext());
    }
    public void delete_data(View view){
        mySQLiteDbHelper.removeEntry(id);
        finish();
    }

}
