package com.example.thomas.erasmusproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import android.widget.TextView;

import static java.lang.Math.pow;

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
    Heatmap hm;

    String width;
    String length;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanbeacons);

        //Gets the devices who where put in the database from SharedPreferences
        SharedPreferences userDetails = getSharedPreferences("Device", MODE_PRIVATE);
        room = userDetails.getString("SelectedRoom", "");
        String devicesString = userDetails.getString(room, "");
        devicesArray = devicesString.split(" ");


        //Shows beacons found in database for specified room
        TextView beaconsDatabase = (TextView)findViewById(R.id.txtAvailable);
        beaconsDatabase.setText("Beacons found in database for room: " + room +": \n");
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





        SharedPreferences getShared = getSharedPreferences("RoomMeasures", MODE_PRIVATE);
        String roomValueString = getShared.getString("RoomMeasures", "");
        String[] arr_RoomValues = roomValueString.split(" ");
        width = arr_RoomValues[0];
        length = arr_RoomValues[1];

        /*hm.roomLength = Integer.parseInt(width);
        hm.roomWidth = Integer.parseInt(length);*/




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
                    //Checks if the list of a found beacon already exists in the database
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
                        if(dataArray[0] != 0 && dataArray[1] != 0 && dataArray[2] != 0 && dataArray[3] != 0) {
                            sendsDataDatabase(dataArray[0],dataArray[1], dataArray[2], dataArray[3]);
                            if(c != null) {
                                hm.values = calculateDistance(dataArray);
                                dataArray = new Integer[] {0,0,0,0};
                                hm.redrawHeatmap(hm,Integer.parseInt(width),Integer.parseInt(length));
                            }
                            dataArray = new Integer[] {0,0,0,0};
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
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("0000feaa-0000-1000-8000-00805f9b34fb", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        findViewById(R.id.Heatmap).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hm = new Heatmap(scanbeacons.this);
                setContentView(hm);
            }
        });




    }
    public static Context c;
    private int[] calculateDistance(Integer[] dataArray) {
        int txPower = -59;
        int[] distances = new int[dataArray.length];
        for (int i =0; i<dataArray.length; i++) {
            distances[i]= (int)((Math.pow(10d, ((double) txPower - dataArray[i]) / (10 * 2)))*100);
        }

        return distances;
    }
    private static class Heatmap extends View implements BeaconConsumer{
        public static int[] values = {0,0,250,250};

        public int roomWidth;
        public int roomLength;
        int[][] room = new int[roomWidth][roomLength];
        Point beacon1 = new Point(30,30);
        Point beacon2 = new Point(30+roomWidth*200,30);
        Point beacon3 = new Point(30,30+roomLength*200);
        Point beacon4 = new Point(30+roomWidth*200,30+roomLength*200);
        int counterOld = 0;
        int counterNew = 0;

        public void redrawHeatmap(Heatmap hm, final int width, final int length) {
            post(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                    roomWidth = width;
                    roomLength = length;
                }
            });
        }
        // CONSTRUCTOR
        public Heatmap(Context context) {
            super(context);
            scanbeacons.c = context;
            setFocusable(true);
            for (int i = 0; i<roomWidth; i++ ) {
                for (int j = 0; j<roomLength; j++ ) {
                    room[i][j] =220;
                }
            }
            setWillNotDraw(false);



        }
        @Override
        protected void onDraw(Canvas canvas) {
            Point location = trilateration();
            Paint paint = new Paint();
            for (int i = 0; i<roomWidth; i++ ) {
                for (int j = 0; j<roomLength; j++ ) {
                    Point currentRectTopLeft = new Point(30 + i*200, 30+j*200);
                    Point currentRectBottomRight = new Point(230+i*200, 230+j*200);
                    if (location.x > currentRectTopLeft.x && location.y > currentRectTopLeft.y) {
                        if (location.x <= currentRectBottomRight.x && location.y <= currentRectBottomRight.y) {
                            room[i][j] -= 10;
                        }
                    }
                    paint.setColor(Color.rgb(room[i][j], room[i][j], room[i][j]));
                    canvas.drawRect(30 + i * 200, 30 + j * 200, 230 + i * 200, 230 + j * 200, paint);
                }
            }
            paint.setColor(Color.RED);
            canvas.drawCircle(beacon1.x, beacon1.y ,50,paint);
            canvas.drawCircle(beacon2.x ,beacon2.y ,50,paint);
            canvas.drawCircle(beacon3.x ,beacon3.y ,50,paint);
            canvas.drawCircle(beacon4.x ,beacon4.y ,50,paint);
        }
        
        public Point trilateration() {
            float beacon1X = beacon1.x;
            float beacon1Y = beacon1.y;
            float beacon2X = beacon2.x;
            float beacon2Y = beacon2.y;
            float beacon3X = beacon3.x;
            float beacon3Y = beacon3.y;
            float DistanceB1 = (float)values[0];
            float DistanceB2 = (float)values[1];
            float DistanceB3 = (float)values[2];

            double temp1 = (pow(beacon3X, 2.) - pow(beacon2X, 2.) + pow(beacon3Y, 2.) -
                    pow(beacon2Y, 2.) + pow(DistanceB2, 2.) - pow(DistanceB3, 2.)) / 2.0;
            double temp2 = (pow(beacon1X, 2.) - pow(beacon2X, 2.) + pow(beacon1Y, 2.) -
                    pow(beacon2Y, 2.) + pow(DistanceB2, 2.) - pow(DistanceB1, 2.)) / 2.0;
            double y = ((temp2 * (beacon2X - beacon3X)) - (temp1 * (beacon2X - beacon1X))) /
                    (((beacon1Y - beacon2Y) * (beacon2X - beacon3X)) - ((beacon3Y - beacon2Y) * (beacon2X - beacon1X)));
            double x = ((y * (beacon1Y - beacon2Y)) - temp2) / (beacon2X - beacon1X);
            return new Point((int)x,(int)y);
        }

        @Override
        public void onBeaconServiceConnect() {

        }

        @Override
        public Context getApplicationContext() {
            return null;
        }

        @Override
        public void unbindService(ServiceConnection serviceConnection) {

        }

        @Override
        public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
            return false;
        }
    }

}
