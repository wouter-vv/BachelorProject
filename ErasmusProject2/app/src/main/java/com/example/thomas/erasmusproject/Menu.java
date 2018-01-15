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
        /*
        Intent StartAddDevice = new Intent(this, addDevice.class);
        startActivity(StartAddDevice);*/
    }
    public void ScanBeacons(View view) {
        String type = "getRooms";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,"0");

        /*String type = "getDevices";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type);*/
        /*Intent StartScanBeacons = new Intent(this, scanbeacons.class);
        startActivity(StartScanBeacons);*/
    }
    public void DeleteData(View view) {
        String type = "deleteDevices";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type);

    }
    public void DeleteBeacons(View view) {
        String type = "deleteData";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type);
    }
}
