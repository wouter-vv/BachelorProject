package com.example.thomas.erasmusproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    public void RegisterBeacon(View view) {
        Intent StartBeaconRegistration = new Intent(this, RoomRegistration.class);
        startActivity(StartBeaconRegistration);
    }
    public void AddBeacon(View view) {
        String type = "getRooms";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,"1");
    }
    public void ScanBeacons(View view) {
        String type = "getRooms";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,"0");
    }
}
