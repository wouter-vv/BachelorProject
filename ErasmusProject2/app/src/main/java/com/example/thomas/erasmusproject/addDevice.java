package com.example.thomas.erasmusproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class addDevice extends AppCompatActivity {
    EditText UUID, Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        UUID = (EditText)findViewById(R.id.txtUUID);
        Name = (EditText)findViewById(R.id.txtName);
    }

    public void OnCancel(View view) {
        Intent StartBeaconRegistration = new Intent(this, Menu.class);
        startActivity(StartBeaconRegistration);
    }

    public void OnRegister(View view) {
        String str_UUID = UUID.getText().toString();
        String str_name = Name.getText().toString();
        String type = "device";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,str_UUID, str_name);

    }

}
