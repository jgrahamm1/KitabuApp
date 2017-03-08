package com.example.jgraham.kitabureg1.database;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class KitabuEntry {

    private Long mRowID; // Unique Row ID for database operations.
    private int mId;    // Unique id generated from web server.
    private String mLink;     // URL that is shared.
    private String mTitle;    //Title of the URL.
    private int mType;        // column if is is
    private String mTags;
    private Long mPhoneNo;

    public KitabuEntry() {
        this.mId=0;
        this.mLink="";
        this.mTitle="";
        this.mType=-2;
        this.mTags="";
        this.mPhoneNo=0L;
    }

    public KitabuEntry(String id_str, String url, String phone, String tags, int type, String title) {
        this.mId = Integer.parseInt(id_str);
        this.mLink = url;
        this.mPhoneNo = Long.parseLong(phone);
        this.mTitle = title;
        this.mTags = tags;
        this.mType = type;
    }

    public Long getmRowID() {
        return mRowID;
    }

    public void setmRowID(Long mRowID) {
        this.mRowID = mRowID;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getmTags() {
        return mTags;
    }

    public void setmTags(String mTags) {
        this.mTags = mTags;
    }

    public Long getmPhoneNo() {
        return mPhoneNo;
    }

    public void setmPhoneNo(Long mPhoneNo) {
        this.mPhoneNo = mPhoneNo;
    }
    public String toString() {

        return this.getmTitle() +" \n " + getmLink()+" \n "+getmPhoneNo();
    }

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
           // this.mTags = jsonObject.getString("tag_list");
        }
        catch (JSONException e)
        {
            Log.d("JSON", "EXCEPTION");
        }
    }
}

