package com.example.jgraham.kitabureg1;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class SendData extends AppCompatActivity {
    Button senddata_button;
    private EditText senddata_id, senddata_link,senddata_tags;
    private int flag=-2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senddata);
        senddata_id = (EditText) findViewById(R.id.id);
        senddata_link = (EditText) findViewById(R.id.link);
        senddata_tags = (EditText) findViewById(R.id.tags);
    }
    public void sendData(View view){
        senddata_button = (Button) findViewById(R.id.senddata);
        if(senddata_id.getText().length()==0){
            senddata_id.setError("ID is required");
        }
        if(senddata_link.getText().length()==0){
            senddata_link.setError("URL is required");
        }
        if (senddata_tags.getText().length()==0){
            senddata_tags.setError("Description is required");
        }
        if(flag==-2){
            Toast.makeText(this, "Please select public or private", Toast.LENGTH_SHORT).show();
        }
        Log.d(Integer.toString(flag),"view");
    }

    public void onRadioButtonClicked(View view){
        boolean temp = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_private:
                if (temp)
                    flag = 1;
                break;
            case R.id.radio_public:
                if (temp)
                    flag = 0;
                break;
        }
    }
}
