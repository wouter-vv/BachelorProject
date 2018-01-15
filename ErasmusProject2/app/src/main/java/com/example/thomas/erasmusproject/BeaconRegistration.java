package com.example.thomas.erasmusproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class BeaconRegistration extends AppCompatActivity {

    EditText UUID1, UUID2, UUID3, UUID4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_registration);
        UUID1 = (EditText)findViewById(R.id.txtBeacon1);
        UUID2 = (EditText)findViewById(R.id.txtBeacon2);
        UUID3 = (EditText)findViewById(R.id.txtBeacon3);
        UUID4 = (EditText)findViewById(R.id.txtBeacon4);
    }

    public void OnSend(View view) {
        String str_UUID1 = UUID1.getText().toString();
        String str_UUID2 = UUID2.getText().toString();
        String str_UUID3 = UUID3.getText().toString();
        String str_UUID4 = UUID4.getText().toString();
        String type = "rooms";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,str_UUID1,str_UUID2,str_UUID3,str_UUID4);
    }

    public  void cancel (View view) {
        Intent StartBeaconRegistration = new Intent(this, Menu.class);
        startActivity(StartBeaconRegistration);
    }
}
