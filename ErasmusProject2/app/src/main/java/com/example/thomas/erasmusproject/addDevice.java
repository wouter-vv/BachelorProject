package com.example.thomas.erasmusproject;

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

public class addDevice extends AppCompatActivity {
    EditText Name;
    String[] roomArray;
    String selectedRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        Name = (EditText)findViewById(R.id.txtBeaconName);
        Spinner dropdown = (Spinner) findViewById(R.id.spinner1);

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
        if(Name.getText().toString().length() > 0) {
            String str_name = Name.getText().toString();
            String type = "device";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, str_name, selectedRoom);
        }
    }
}