package com.example.thomas.erasmusproject;

import android.content.Context;
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

        Intent StartBeaconRegistration = new Intent(this, BeaconRegistration.class);
        startActivity(StartBeaconRegistration);

    }
    public void AddDevice(View view) {
        Intent StartAddDevice = new Intent(this, addDevice.class);
        startActivity(StartAddDevice);
    }
    public void ScanBeacons(View view) {
        String type = "getDevices";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type);
        /*Intent StartScanBeacons = new Intent(this, scanbeacons.class);
        startActivity(StartScanBeacons);*/
    }
    public void DeleteData(View view) {
        String type = "deleteData";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type);

    }
}
