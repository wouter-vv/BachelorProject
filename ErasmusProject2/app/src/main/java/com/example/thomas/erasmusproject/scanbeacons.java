package com.example.thomas.erasmusproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class scanbeacons extends AppCompatActivity implements BeaconConsumer{

    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    //Find devices of this room
    String room;
    List<String> AvailableBeacons = new ArrayList<>();


    //Used for showing data on mobile screen
    ArrayAdapter<String> adapterBeacons;
    ListView lvName;
    //***************************************

    //Stores the beacons found in the database
    String[] devicesArrayF;
    String[] devicesArray;

    //stores values for each device
    HashMap<String, ArrayList<Integer>> deviceData = new HashMap<>();

    //keeps data to send to database
    Integer[] dataArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanbeacons);


        //Gets the devices who where put in the database from SharedPreferences
        SharedPreferences userDetails = getSharedPreferences("Device", MODE_PRIVATE);
        String devicesString = userDetails.getString("Devices", "");
        devicesArrayF = devicesString.split(" ");


        //Shows beacons found in database for specified room
        TextView beaconsDatabase = (TextView)findViewById(R.id.txtAvailable);
        beaconsDatabase.setText("Beacons found in database for room: " + devicesArrayF[0] +": \n");
        room = devicesArrayF[0];
        devicesArray = Arrays.copyOfRange(devicesArrayF, 1, devicesArrayF.length);
        for(int i=0; i < devicesArray.length; i++) {
            beaconsDatabase.append(devicesArray[i] + "\n");
        }

        //Initialisation of the list, show name and RSSI of found beacon
        lvName = (ListView) findViewById(R.id.list);
        adapterBeacons=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, AvailableBeacons);
        lvName.setAdapter(adapterBeacons);

        //initialisation of the dataArray, keeps data for sending to database
        dataArray = new Integer[4];
        dataArray[0] = 0;
        dataArray[1] = 0;
        dataArray[2] = 0;
        dataArray[3] = 0;

        //Calls function for higher android versions (5.0+), for authorisation
        initAndroid6();
    }



    /**
     * Sends data to the background worker and the background worker sends it to the database
     * @param value1    rssi value of first beacon
     * @param value2    rssi value of second beacon
     * @param value3    rssi value of third beacon
     * @param value4    rss1 value of fourth beacon
     */
    public void sendsDataDatabase(int value1, int value2, int value3, int value4) {
        String type = "setValues";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,room,value1+"",value2+"",value3+"",value4 +"");
    }


    public void initAndroid6() {

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        beaconManager.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }
    }
    /**
     * ask for the result of the request of the new permission
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("a", "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }



    @Override
    public void onBeaconServiceConnect() {
        Log.i(TAG,"Start");
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    //Checks if the list of a found beacon already exists
                    if(deviceData.containsKey(beacons.iterator().next().getBluetoothName())) {
                        //Adds the RSSI data to the list of the specified beacon
                        ArrayList<Integer> valuesList=deviceData.get(beacons.iterator().next().getBluetoothName());
                        valuesList.add(beacons.iterator().next().getRssi());
                        deviceData.put(beacons.iterator().next().getBluetoothName(), valuesList);
                        /******************************************************/

                        int index=0;
                        //updates list with new RSSI
                        for(int i = 0; i < AvailableBeacons.size(); i++) {
                            String s = AvailableBeacons.get(i);
                            String split = s.split("\n")[0];
                            if(split.contentEquals(beacons.iterator().next().getBluetoothName())) {
                                index = i;
                            }
                        }
                        //Sends data to writer to database
                        dataArray[index] = beacons.iterator().next().getRssi();
                        if(dataArray[0] != 0 && dataArray[1] != 0) {
                            sendsDataDatabase(dataArray[0],dataArray[1], 0, 0);
                        }

                        //int index = AvailableBeacons.indexOf(beacons.iterator().next().getBluetoothName());
                        AvailableBeacons.set(index, beacons.iterator().next().getBluetoothName() + "\nRSSI: " + beacons.iterator().next().getRssi());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapterBeacons.notifyDataSetChanged();
                            }
                        });
                        //********************************

                    }
                    else {
                        //checks if detected beacon device is in the database
                        if (Arrays.asList(devicesArray).contains(beacons.iterator().next().getBluetoothName())) {

                            //makes new list in the hashmap deviceData for the specified beacon
                            ArrayList<Integer> values = new ArrayList<Integer>();
                            deviceData.put(beacons.iterator().next().getBluetoothName(), values);

                            ArrayList<Integer> valuesList=deviceData.get(beacons.iterator().next().getBluetoothName());
                            valuesList.add(beacons.iterator().next().getRssi());
                            deviceData.put(beacons.iterator().next().getBluetoothName(), valuesList);

                            //Adds found device on the screen
                            AvailableBeacons.add(beacons.iterator().next().getBluetoothName()+ "\nRSSI: " + beacons.iterator().next().getRssi());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapterBeacons.notifyDataSetChanged();
                                }
                            });
                        }
                        else {
                            Log.d("NEJE", "Geen beacons gevonden");
                        }
                    }


                    Log.i(TAG,"The first beacon name " + beacons.iterator().next().getBluetoothName() + " " + beacons.iterator().next().getBluetoothAddress());
                    /*Log.i(TAG, "The first beacon name " + beacons.iterator().next().getBluetoothName()
                            + "; RSSI: " + beacons.iterator().next().getRssi()
                            + " I see is about "+beacons.iterator().next().getDistance()+" meters away. TXPower: " + beacons.iterator().next().getTxPower()
                            + " UUID: "+ beacons.iterator().next().getServiceUuid());*/
                    //double distance = calculateDistance(beacons.iterator().next().getRssi(), beacons.iterator().next().getTxPower());
                    //Log.i(TAG, "Name " + beacons.iterator().next().getBluetoothName() + " distance:" + distance);
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("0000feaa-0000-1000-8000-00805f9b34fb", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
