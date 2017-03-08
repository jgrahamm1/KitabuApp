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

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactsUtil {

    public Context m_context;
    public ArrayList<String> m_contact_list;
    public int mid;
    public String mphoneno;
    protected String m_contacts_string;

    // Constructor
    public ContactsUtil(Context context) {
        m_context = context;
    }

    // This function returns ArrayList of phone numbers
    public ArrayList<String> getContacts() {
        ContentResolver cr = m_context.getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        // Contacts ArrayList
        ArrayList<String> contacts_list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            contacts_list = new ArrayList<String>();
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contacts_list.add(contactNumber);
                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext());
        }
        Log.d("CONTACTS", "Contacts: " + contacts_list.toString());
        return contacts_list;
    }

    public void sendContacts(int id, String phoneno) {
        mid = id;
        mphoneno = phoneno;
        ContactsTask c_task = new ContactsTask();
        c_task.execute();
    }

    /*
    * LoginTask
    * AsyncTask for performing activity on network
    */
    class ContactsTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        private String serv_res;

        protected String doInBackground(Void... arg0) {
            try {
                m_contact_list = getContacts();
                m_contacts_string = m_contact_list.toString();

                // Put parameters in map
                Map<String, String> params = new HashMap<String, String>();
                params.put("contacts_str", m_contacts_string);
                params.put("id", String.valueOf(mid));
                params.put("phoneno", String.valueOf(mphoneno));

                // Send to server
                try {
                    serv_res = ServerUtil.get("http://kitabu.prashant.at/api/message", params, m_context);
                } catch (IOException e) {
                    Log.d("CONTACTS", "Sending contacts to server did not work");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                this.exception = e;
                Log.d("CONTACTS", "Async doInBackground failed");
                return null;
            }
            return serv_res;
        }

        protected void onPostExecute(String result) {
            Log.d("CONTACTS", "onPostExecute got result: " + result);
        }
    }

}
