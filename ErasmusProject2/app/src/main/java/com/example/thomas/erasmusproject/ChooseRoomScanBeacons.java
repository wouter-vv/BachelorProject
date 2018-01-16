package com.example.thomas.erasmusproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ChooseRoomScanBeacons extends AppCompatActivity {

    String[] roomArray;
    String selectedRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_room_scan_beacons);


        SharedPreferences userDetails = getSharedPreferences("Room", MODE_PRIVATE);
        String devicesString = userDetails.getString("Room", "");
        roomArray = devicesString.split(" ");

        Spinner dropdown = (Spinner) findViewById(R.id.spinner1);
        //Dropdown menu for selecting a room
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roomArray);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedRoom = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void scanBeacons(View view) {
        String type = "getDevices";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,selectedRoom);
    }

    public void back(View view) {
        Intent StartBeaconRegistration = new Intent(this, Menu.class);
        startActivity(StartBeaconRegistration);
    }
}
