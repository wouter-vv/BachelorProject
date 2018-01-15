package com.example.thomas.erasmusproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RoomRegistration extends AppCompatActivity {

    EditText Roomname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_registration);
        Roomname = (EditText)findViewById(R.id.txtRoomName);
    }

    public void OnSend(View view) {
        String str_Roomname = Roomname.getText().toString();

        String type = "rooms";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,str_Roomname);
    }

    public  void cancel (View view) {
        Intent StartBeaconRegistration = new Intent(this, Menu.class);
        startActivity(StartBeaconRegistration);
    }
}
