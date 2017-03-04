package com.example.jgraham.kitabureg1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by maheshdevalla on 2/24/17.
 */

public class Tab1 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        // Get contacts if permissions set
//        if (WelcomeActivity.checkPermissions(getActivity())) {
//            Log.d("CONTACTS", "Getting contacts...");
//            ContactsUtil cu = new ContactsUtil(getContext());
//            cu.sendContacts();
//        }
        return rootView;
    }
}
