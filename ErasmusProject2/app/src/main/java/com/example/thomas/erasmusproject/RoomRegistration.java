package com.example.thomas.erasmusproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RoomRegistration extends AppCompatActivity {

    EditText Roomname;
    EditText RoomWith;
    EditText RoomLength;
    EditText RoomDescription;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_registration);
        Roomname = (EditText)findViewById(R.id.txtRoomName);
        RoomWith = (EditText)findViewById(R.id.txtRoomWith);
        RoomLength = (EditText)findViewById(R.id.txtRoomLength);
        RoomDescription = (EditText)findViewById(R.id.txtRoomDescription);
        alertDialog = new AlertDialog.Builder(this).create();
    }

    public void OnSend(View view) {
        String str_Roomname = Roomname.getText().toString();
        String str_RoomWith = RoomWith.getText().toString();
        String str_RoomLength = RoomLength.getText().toString();
        String str_RoomDescription = RoomDescription.getText().toString();

        int int_RoomWith = 0;
        int int_RoomLength = 0;
        try {
            int_RoomWith =Integer.parseInt(str_RoomWith);
        }catch (Exception e ) {
            alertDialog.setTitle("ERROR");
            alertDialog.setMessage("Please enter a width number");
            alertDialog.show();
        }
        try {
            int_RoomLength = Integer.parseInt(str_RoomLength);
        }catch (Exception e) {
            alertDialog.setTitle("ERROR");
            alertDialog.setMessage("Please enter a length number");
            alertDialog.show();
        }

        if(!str_Roomname.contentEquals("")) {
            if(int_RoomWith != 0) {
                if(int_RoomLength != 0) {
                    if(!str_RoomDescription.contentEquals("")) {
                        String type = "rooms";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                        backgroundWorker.execute(type,str_Roomname,str_RoomWith,str_RoomLength,str_RoomDescription);
                    }else {
                        alertDialog.setTitle("ERROR");
                        alertDialog.setMessage("Please enter a description");
                        alertDialog.show();
                    }
                }else {
                    alertDialog.setTitle("ERROR");
                    alertDialog.setMessage("Please enter a length");
                    alertDialog.show();
                }
            }else {
                alertDialog.setTitle("ERROR");
                alertDialog.setMessage("Please enter a with");
                alertDialog.show();
            }
        }else {
            alertDialog.setTitle("ERROR");
            alertDialog.setMessage("Please enter a name");
            alertDialog.show();
        }


    }

    public  void cancel (View view) {
        Intent StartBeaconRegistration = new Intent(this, Menu.class);
        startActivity(StartBeaconRegistration);
    }
}
