package com.example.thomas.erasmusproject;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    EditText UsernameEt, PasswordEt, IpaddressEt;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UsernameEt = (EditText) findViewById(R.id.txtBeacon2);
        PasswordEt = (EditText) findViewById(R.id.txtBeacon3);
        IpaddressEt = (EditText) findViewById(R.id.txtRoomName);
        alertDialog = new AlertDialog.Builder(this).create();
    }

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public void OnLogin(View view) {
        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String ipaddress = IpaddressEt.getText().toString();
        if(!username.contentEquals("")) {
            if(!password.contentEquals("")) {
                if(!ipaddress.contentEquals("")) {
                    if(PATTERN.matcher(ipaddress).matches()) {
                        //Deleting all the shared
                        SharedPreferences deleteDevice = this.getSharedPreferences("Device", this.MODE_PRIVATE);
                        deleteDevice.edit().remove("Devices").commit();
                        SharedPreferences deleteIp = this.getSharedPreferences("ipaddress", this.MODE_PRIVATE);
                        deleteIp.edit().remove("ipaddress").commit();
                        SharedPreferences deleteRoom = this.getSharedPreferences("Room", this.MODE_PRIVATE);
                        deleteRoom.edit().remove("Room").commit();
                        String type = "login";

                        this.getSharedPreferences("Device", 0).edit().clear().commit();
                        try{
                            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                            backgroundWorker.Ipaddress = ipaddress;
                            backgroundWorker.execute(type,username,password,ipaddress);
                        }catch (Exception e) {
                            alertDialog.setTitle("ERROR");
                            alertDialog.setMessage("Connection not succeeded");
                            alertDialog.show();
                        }
                    }else {
                        alertDialog.setTitle("ERROR");
                        alertDialog.setMessage("Please enter an valid ipaddress");
                        alertDialog.show();
                    }
                }
                else {
                    alertDialog.setTitle("ERROR");
                    alertDialog.setMessage("Please enter an ipaddress");
                    alertDialog.show();
                }
            }
            else {
                alertDialog.setTitle("ERROR");
                alertDialog.setMessage("Please enter a password");
                alertDialog.show();
            }
        }
        else {
            alertDialog.setTitle("ERROR");
            alertDialog.setMessage("Please enter a username");
            alertDialog.show();
        }
    }
}

