package com.example.thomas.erasmusproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText UsernameEt, PasswordEt, IpaddressEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UsernameEt = (EditText) findViewById(R.id.txtBeacon2);
        PasswordEt = (EditText) findViewById(R.id.txtBeacon3);
        IpaddressEt = (EditText) findViewById(R.id.txtRoomName);

    }


    public void OnLogin(View view) {
        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String ipaddress = IpaddressEt.getText().toString();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.Ipaddress = ipaddress;
        backgroundWorker.execute(type,username,password,ipaddress);

    }

    public void HeatmapActivity(View view) {
        Intent ShowHeatmap = new Intent(this, Location.class);
        startActivity(ShowHeatmap);

    }
}

