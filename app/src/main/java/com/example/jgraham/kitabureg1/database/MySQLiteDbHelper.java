package com.example.jgraham.kitabureg1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MySQLiteDbHelper extends SQLiteOpenHelper {
    // Database name string
    public static final String DATABASE_NAME = "Database_Kitabu";
    // Table name string. (Only one table)
    private static final String TABLE_NAME_ENTRIES = "Kitabu_Database_Table_Links";

    // Version code
    private static final int DATABASE_VERSION = 1;

    // Table schema, column names
    public static final String KEY_ROWID = "_id";
    public static final String KEY_ID = "id";
    public static final String KEY_LINK = "link";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TYPE = "type"; //public, private
    public static final String KEY_TAGS = "tags";
    public static final String KEY_PHNO = "phoneno";


    // SQL query to create the table for the first time
    // Data types are defined below
    private static final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME_ENTRIES
            + " ("
            + KEY_ROWID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ID
            + " INTEGER, "
            + KEY_LINK
            + " TEXT, "
            + KEY_TITLE
            + " TEXT, "
            + KEY_TYPE
            + " INTEGER, "
            + KEY_TAGS
            + " TEXT, "
            + KEY_PHNO
            + " INTEGER, "
            + ");";

    private static final String[] mColumnList = new String[] { KEY_ROWID,
            KEY_ID, KEY_LINK, KEY_TITLE, KEY_TYPE,
            KEY_TAGS, KEY_PHNO};

    public MySQLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DB created","Database_Kitabu");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the table schema.
        db.execSQL(CREATE_TABLE_ENTRIES);
        Log.d("Kitabu Database", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteDbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ENTRIES);
        onCreate(db);

    }

    // Insert a item given each column value
    public long insertEntry(KitabuEntry entry) {

        ContentValues value = new ContentValues();
        // put values in a ContentValues object
        value.put(KEY_ID, entry.getmId());
        value.put(KEY_LINK, entry.getmLink());
        value.put(KEY_TITLE, entry.getmTitle());
        value.put(KEY_TYPE, entry.getmType());
        value.put(KEY_TAGS, entry.getmTags());
        value.put(KEY_PHNO, entry.getmPhoneNo());
        // get a database object
        SQLiteDatabase dbObj = getWritableDatabase();
        // insert the record
        long id = dbObj.insert(TABLE_NAME_ENTRIES, null, value);
        // close the database
        dbObj.close();

        // return the primary key for the new record
        return id;
    }

    // Remove a entry by giving its index
    public void removeEntry(long rowIndex) {
        SQLiteDatabase dbObj = getWritableDatabase();
        dbObj.delete(TABLE_NAME_ENTRIES, KEY_ROWID + "=" + rowIndex, null);
        dbObj.close();
    }

    // Query a specific entry by its index. Return a cursor having each column
    // value
    public KitabuEntry fetchEntryByIndex(long rowId) throws SQLException {
        SQLiteDatabase dbObj = getReadableDatabase();
        KitabuEntry entry = null;
        // do the query with the condition KEY_ROWID = rowId
        Cursor cursor = dbObj.query(true, TABLE_NAME_ENTRIES, mColumnList,
                KEY_ROWID + "=" + rowId, null, null, null, null, null);

        // move the cursor to the first record
        if (cursor.moveToFirst()) {
            // convert the cursor to an KitabuEntry object
            entry = cursorToEntry(cursor, true);
        }

        // close the cursor
        cursor.close();
        dbObj.close();

        return entry;
    }

    // Query the entire table, return all rows
    public ArrayList<KitabuEntry> fetchEntries() {
        SQLiteDatabase dbObj = getReadableDatabase();
        // store all the entries to an ArrayList
        ArrayList<KitabuEntry> entryList = new ArrayList<KitabuEntry>();
        // do the query without any condition. it retrieves every record from
        // the database
        Cursor cursor = dbObj.query(TABLE_NAME_ENTRIES, mColumnList, null,
                null, null, null, null);

        // the cursor initially points the record PRIOR to the first record
        // use the while loop to read every record from the cursor
        while (cursor.moveToNext()) {
            KitabuEntry entry = cursorToEntry(cursor, false);
            entryList.add(entry);
            Log.d("TAGG", "Got data");
        }

        cursor.close();
        dbObj.close();

        return entryList;
    }

    // convert the a row in the cursor to an KitabuEntry object
    private KitabuEntry cursorToEntry(Cursor cursor, boolean needGps) {
        KitabuEntry entry = new KitabuEntry();
        entry.setmRowID(cursor.getLong(cursor.getColumnIndex(KEY_ROWID)));
        entry.setmId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        entry.setmLink(cursor.getString(cursor.getColumnIndex(KEY_LINK)));
        entry.setmTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
        entry.setmType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
        entry.setmTags(cursor.getString(cursor.getColumnIndex(KEY_TAGS)));
        entry.setmPhoneNo(cursor.getLong(cursor.getColumnIndex(KEY_PHNO)));
        return entry;
    }
}

