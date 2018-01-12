package com.example.thomas.test1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;


    List<Integer> WXG1RSSI = new ArrayList<Integer>();
    List<Integer> WXG2RSSI = new ArrayList<Integer>();
    List<Integer> WXG3RSSI = new ArrayList<Integer>();
    List<Integer> WXG4RSSI = new ArrayList<Integer>();

    List<String> AvailableBeacons = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.list);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AvailableBeacons);
        lv.setAdapter(adapter);

        initAndroid6();
    }

    /**
     * code to make the bluetoothconnection work for android version 6+
     */
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




    public boolean listContainsString(List<String> Beacons, String beaconName) {
        for(String str: Beacons) {
            if(str.trim().contains(beaconName))
                return true;
        }
        return false;
    }
    public void addTextField(){


    }

    @Override
    public void onBeaconServiceConnect() {
        Log.i(TAG,"Start");
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    if(listContainsString(AvailableBeacons,beacons.iterator().next().getBluetoothName())) {
                        if (beacons.iterator().next().getBluetoothName().equals("WXG1")) {
                            WXG1RSSI.add(beacons.iterator().next().getRssi());
                        }
                        else if(beacons.iterator().next().getBluetoothName().equals("WXG2")) {
                            WXG2RSSI.add(beacons.iterator().next().getRssi());
                        }
                        else if(beacons.iterator().next().getBluetoothName().equals("WXG3")) {
                            WXG3RSSI.add(beacons.iterator().next().getRssi());
                        }
                        else if(beacons.iterator().next().getBluetoothName().equals("WXG4")) {
                            WXG4RSSI.add(beacons.iterator().next().getRssi());
                        }
                    }
                    else {
                        AvailableBeacons.add(beacons.iterator().next().getBluetoothName());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
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
