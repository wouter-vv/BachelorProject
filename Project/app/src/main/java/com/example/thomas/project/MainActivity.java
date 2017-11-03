package com.example.thomas.project;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    Button btn;
    EditText txtIpaddress;
    EditText txtPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nv = (NavigationView)findViewById(R.id.nv1);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent in;
                switch (menuItem.getItemId()) {
                    case(R.id.nav_home):
                        in = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(in);
                        Log.d("Home", "To home");
                        break;
                    case(R.id.nav_Setting):
                        in = new Intent(getApplicationContext(),ActivitySettings.class);
                        startActivity(in);
                        Log.d("Settings", "To settings");
                        break;
                }
                return true;
            }
        });


        btn = (Button) findViewById(R.id.btnSend);
        txtIpaddress  = (EditText)findViewById(R.id.TxtIpAddress);
        txtPort = (EditText) findViewById(R.id.TxtServerPort);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt = "Test boodschap";
                int server_port = Integer.parseInt(txtPort.getText().toString());
                String server_ip = txtIpaddress.getText().toString();
                DatagramSocket s;
                try {
                    InetAddress local = InetAddress.getByName(server_ip);
                    int msg_length = messageTxt.length();
                    byte[] message = messageTxt.getBytes();

                    s = new DatagramSocket();

                    DatagramPacket p = new DatagramPacket(message, msg_length, local, server_port);
                    s.send(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
