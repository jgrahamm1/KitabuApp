package com.example.jgraham.kitabureg1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by maheshdevalla on 2/24/17.
 */

public class Tab2 extends Fragment implements View.OnClickListener {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab2, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        // Temporary button, register to listener
        Button create_btn = (Button) rootView.findViewById(R.id.create_link);
        create_btn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Log.d("TAB2", "OnClick called");
        if (v.getId() == R.id.create_link) {
            Intent intent = new Intent(v.getContext(), SendDataActivity.class);
            startActivity(intent);
        }
    }
}
