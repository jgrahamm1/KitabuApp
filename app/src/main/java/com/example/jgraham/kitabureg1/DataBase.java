package com.example.jgraham.kitabureg1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
    An Empty class just used to handle database exceptions removed code as of now
    For Database operaions goto package: database/MYSQLiteDbhelper.
 */
public class DataBase extends SQLiteOpenHelper {
    public final static String DB_NAME = "LINKS.DB";
    public final static String TABLE_NAME = "LINKS";

    public DataBase(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
