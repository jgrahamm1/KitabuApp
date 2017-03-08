package com.example.jgraham.kitabureg1.database;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class KitabuEntry {

    private Long mRowID;       // Unique Row ID for database operations.
    private int mId;          // Unique id generated from web server.
    private String mLink;     // URL that is shared.
    private String mTitle;    //Title of the URL.
    private int mType;        //Column if is is public/private link.
    private String mTags;     // Tags of the link just for description.
    private Long mPhoneNo;     // Phone number of the user.

//    Constructor to add the default values if user provided empty records.
    public KitabuEntry() {
        this.mId=0;
        this.mLink="";
        this.mTitle="";
        this.mType=-2;
        this.mTags="";
        this.mPhoneNo=0L;
    }

    //    Constructor to add the user provided values
    public KitabuEntry(String id_str, String url, String phone, String tags, int type, String title) {
        this.mId = Integer.parseInt(id_str);
        this.mLink = url;
        this.mPhoneNo = Long.parseLong(phone);
        this.mTitle = title;
        this.mTags = tags;
        this.mType = type;
    }

    // Get the RowID of the table ** if needed**
    public Long getmRowID() {
        return mRowID;
    }

    // Set the RowID of the table ** if needed**
    public void setmRowID(Long mRowID) {
        this.mRowID = mRowID;
    }

   // Get the id generated from the server
    public int getmId() {
        return mId;
    }

    // set the id generated from the server
    public void setmId(int mId) {
        this.mId = mId;
    }

    // Get the link or URL
    public String getmLink() {
        return mLink;
    }

    // Set the link or URL
    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public String getmTitle() {
        return mTitle;
    }

    // Set the title for the URL
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    // Get the title for the URL
    public int getmType() {
        return mType;
    }
    // Set the type if public or private
    public void setmType(int mType) {
        this.mType = mType;
    }
    // Get the Tags based upon the URL
    public String getmTags() {
        return mTags;
    }
    // Set the Tags based upon the URL
    public void setmTags(String mTags) {
        this.mTags = mTags;
    }
    // Get the phone number who have sent.
    public Long getmPhoneNo() {
        return mPhoneNo;
    }
    // Set the phone number who have sent.
    public void setmPhoneNo(Long mPhoneNo) {
        this.mPhoneNo = mPhoneNo;
    }
    // Method to return the values accordinglt in the list view.
    public String toString() {

        return this.getmTitle() +" \n " + getmLink()+" \n "+getmPhoneNo();
    }

    // Convert the object to JSON object.
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("RowId",mRowID);
            obj.put("id", getmId());
            obj.put("Link", getmLink());
            obj.put("Title", getmTitle());
            obj.put("Type",getmType());
            obj.put("Tags", getmTags());
            obj.put("PhoneNo", getmPhoneNo());
        } catch (JSONException e) {
            return null;
        }
        return obj;
    }
    // Based on the JSON object received from server add the data to the local MYSQL DB.
    public KitabuEntry(JSONObject jsonObject)
    {
        try {
            this.mId = jsonObject.getInt("id");
            Log.d("id", String.valueOf(this.mId));
            if(jsonObject.getString("phoneno").equals("null")) {
                this.mPhoneNo = -1L;
            }
            else
            {
                this.mPhoneNo = Long.parseLong(jsonObject.getString("phoneno"));
            }
            Log.d("phoneno", String.valueOf(this.mPhoneNo));
            this.mLink = jsonObject.getString("url");
            Log.d("link", String.valueOf(this.mLink));
            this.mTitle = jsonObject.getString("title");
            Log.d("title", String.valueOf(this.mTitle));
            if(jsonObject.getString("typep").equals("true"))
                this.mType = 1;
            else
                this.mType = 0;
            Log.d("type", String.valueOf(this.mType));
        }
        catch (JSONException e)
        {
            Log.d("JSON", "EXCEPTION");
        }
    }
}

