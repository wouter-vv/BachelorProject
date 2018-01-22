package com.example.thomas.erasmusproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;

public class addDevice extends AppCompatActivity {
    EditText Name;
    String[] roomArray;
    String selectedRoom;
    String[] devicesArrayF;

    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        alertDialog = new AlertDialog.Builder(this).create();

        Name = (EditText)findViewById(R.id.txtBeaconName);

        Spinner dropdown = (Spinner) findViewById(R.id.spinner1);

        //Get available rooms from the sharedPreferences
        SharedPreferences userDetails = getSharedPreferences("Room", MODE_PRIVATE);
        String devicesString = userDetails.getString("Room", "");
        roomArray = devicesString.split(" ");

        //Dropdown menu for selecting a room
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roomArray);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedRoom = (String) parent.getItemAtPosition(position);
                Log.d("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }




    public void OnCancel(View view) {
        Intent StartBeaconRegistration = new Intent(this, Menu.class);
        startActivity(StartBeaconRegistration);
    }

    public void OnRegister(View view) {
        if(!Name.getText().toString().contentEquals("")) {
            String str_name = Name.getText().toString();

            //Calls the backgroundworker, who gets the beacons and puts them in shared preferences
            String type = "getDevices";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type,selectedRoom,"0");

            //gets the beacons from shared preferences
            SharedPreferences userDetails = getSharedPreferences("Device", MODE_PRIVATE);
            String devicesString = userDetails.getString(selectedRoom, "");
            devicesArrayF = devicesString.split(" ");

            //Checks if the added beacon isnt already registered
            if(!Arrays.asList(devicesArrayF).contains(str_name)){
                //Adds the device to the the room
                type = "device";
                backgroundWorker = new BackgroundWorker(this);

                //Makes sure that the added device is also in the shared preferences
                backgroundWorker.execute(type, str_name, selectedRoom);
                type = "getDevices";
                backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(type,selectedRoom,"0");
            }else {
                alertDialog.setTitle("ERROR");
                alertDialog.setMessage("Beacon already exists in this room");
                alertDialog.show();
            }
        }else {
            alertDialog.setTitle("ERROR");
            alertDialog.setMessage("Please enter a name");
            alertDialog.show();
        }
    }
}
