package com.example.thomas.test1;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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


public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    protected static final String TAG = "RangingActivity";
    private static final int WIDTH_PX = 1000;
    private static final int HEIGHT_PX = 1000;
    private BeaconManager beaconManager;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private boolean isConnected = false;
    private int counter1 = 0;
    private int counter2 = 0;
    private int counter3 = 0;
    private int counter4 = 0;
    int amtAvgIterations = 10;
    private int med1[] = new int[amtAvgIterations];
    private int med2[] = new int[amtAvgIterations];
    private int med3[] = new int[amtAvgIterations];
    private int med4[] = new int[amtAvgIterations];
    private boolean outlier1 = false;
    private boolean outlier2 = false;
    private boolean outlier3 = false;
    private boolean outlier4 = false;
    private List<Integer> somelist1 = new ArrayList();
    private List<Integer> somelist2 = new ArrayList();
    private List<Integer> somelist3 = new ArrayList();
    private List<Integer> somelist4 = new ArrayList();
    private double avg1;
    private double avg2;
    private double avg3;
    private double avg4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAndroid6();
    }

    public void OpenHeatmapActivity (View HeatmapButton) {
        Intent intent = new Intent(this, Heatmap.class);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    private double calculateAverage(List <Integer> marks) {
        Integer sum = 0;
        if(!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }
    boolean firstIterationFinished = false;
    @Override
    public void onBeaconServiceConnect() {
        Log.i(TAG,"Start");

        beaconManager.setBackgroundScanPeriod(1100l);
        beaconManager.setBackgroundBetweenScanPeriod(30000l);
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    int rssi =  beacons.iterator().next().getRssi();
                    if(counter4 <= amtAvgIterations && beacons.iterator().next().getBluetoothName().equals("WXG4")) {
                        somelist4.add(rssi);
                        avg4 = calculateAverage(somelist4);
                        if (counter4 == amtAvgIterations) {
                            counter4 = 0;
                            firstIterationFinished = true;
                        }
                        if (counter4 != 0) {
                            //check if the value isnt 10 higher of lower than the previous one to filter out random values
                            if (!(med4[counter4] < (med4[counter4 - 1] -= 10) || med4[counter4] < (med4[counter4 - 1] += 10)) || outlier4 == true) {
                                med4[counter4] = rssi;
                                counter4++;
                                int avg = 0;
                                for (int val : med4) {
                                    avg += val;
                                }
                                if (firstIterationFinished) {
                                    avg /= amtAvgIterations;
                                }
                                else {
                                    avg /= counter4;
                                }
                                outlier4 = false;
                                Log.i(TAG, calculateAccuracy(-58, avg)+"");
                            } else {
                                // if there are 2 continious outliers, that means the distance really changed
                                outlier4 = true;
                            }
                        }
                        else {
                            med4[counter4] = rssi;
                            counter4++;
                            int avg = 0;
                            for (int val : med4) {
                                avg += val;
                            }
                            avg /= amtAvgIterations;
                            outlier4 = false;
                            Log.i(TAG, calculateAccuracy(-58, avg)+"");
                        }
                    }
                    Log.i(TAG, "The first beacon name " + beacons.iterator().next().getBluetoothName()
                            + "; RSSI: " + beacons.iterator().next().getRssi()
                            + " I see is about "+beacons.iterator().next().getDistance()+" meters away. TXPower: " + beacons.iterator().next().getTxPower()
                            + " UUID: "+ beacons.iterator().next().getServiceUuid()
                            + " distance: "+ calculateAccuracy(-58, beacons.iterator().next().getRssi()));
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

    protected static double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi*1.0/txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            double accuracy =  (0.89976)*Math.pow(ratio,7.7095)+ 0.111;
            return accuracy;
        }
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

}
