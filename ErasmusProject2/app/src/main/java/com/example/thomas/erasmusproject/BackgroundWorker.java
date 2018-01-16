package com.example.thomas.erasmusproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Thomas on 22/11/2017.
 */

public class BackgroundWorker extends AsyncTask<String,Void, String> {
    Context context;
    AlertDialog alertDialog;
    String Ipaddress;
    int keuzeRoom;

    BackgroundWorker(Context ctx) {
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        if(type.equals("login")) {
            try {
                String user_name = params[1];
                String password  = params[2];
                String login_url = "http://" + params[3] + "/login.php" ;

                SharedPreferences userDetails = context.getSharedPreferences("ipaddress", MODE_PRIVATE);
                SharedPreferences.Editor edit = userDetails.edit();
                edit.clear();
                edit.putString("ipaddress", params[3].toString().trim());
                edit.commit();

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine()) != null)   {
                    result += line;
                }
                Log.d("test", result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(type.equals("rooms")) {
            try {
                String Roomname  = params[1];

                SharedPreferences userDetails = context.getSharedPreferences("ipaddress", MODE_PRIVATE);
                String ipaddress = userDetails.getString("ipaddress", "");

                String room_url = "http://" + ipaddress + "/Rooms.php" ;

                URL url = new URL(room_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("nameRoom", "UTF-8") + "=" + URLEncoder.encode(Roomname, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine()) != null)   {
                    result += line;
                }
                Log.d("test", result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("device")) {
            try {

                String Name  = params[1];
                String Room  = params[2];

                SharedPreferences userDetails = context.getSharedPreferences("ipaddress", MODE_PRIVATE);
                String ipaddress = userDetails.getString("ipaddress", "");

                String device_url = "http://" + ipaddress + "/device.php" ;

                URL url = new URL(device_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));


                String post_data = URLEncoder.encode("Name", "UTF-8") + "=" + URLEncoder.encode(Name, "UTF-8")+ "&"
                        + URLEncoder.encode("Room", "UTF-8") + "=" + URLEncoder.encode(Room, "UTF-8");;

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine()) != null)   {
                    result += line;
                }
                Log.d("test", result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("getDevices")) {
            try {
                SharedPreferences userDetails = context.getSharedPreferences("ipaddress", MODE_PRIVATE);
                String ipaddress = userDetails.getString("ipaddress", "");

                String device_url = "http://" + ipaddress + "/getDevice.php" ;

                String Room = params[1];

                URL url = new URL(device_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Room", "UTF-8") + "=" + URLEncoder.encode(Room, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine()) != null)   {
                    result += line;
                }
                Log.d("test", result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("deleteData")) {
            try {
                SharedPreferences userDetails = context.getSharedPreferences("ipaddress", MODE_PRIVATE);
                String ipaddress = userDetails.getString("ipaddress", "");

                String device_url = "http://" + ipaddress + "/clearData.php" ;

                URL url = new URL(device_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine()) != null)   {
                    result += line;
                }
                Log.d("test", result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("getRooms")) {
            try {
                keuzeRoom = Integer.parseInt(params[1]);

                SharedPreferences userDetails = context.getSharedPreferences("ipaddress", MODE_PRIVATE);
                String ipaddress = userDetails.getString("ipaddress", "");

                String device_url = "http://" + ipaddress + "/getRooms.php" ;

                URL url = new URL(device_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine()) != null)   {
                    result += line;
                }
                Log.d("test", result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("deleteDevices")) {
            try {
                SharedPreferences userDetails = context.getSharedPreferences("ipaddress", MODE_PRIVATE);
                String ipaddress = userDetails.getString("ipaddress", "");

                String device_url = "http://" + ipaddress + "/deleteDevices.php" ;

                URL url = new URL(device_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine()) != null)   {
                    result += line;
                }
                Log.d("test", result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        String[] parts = result.split(" ");
        if (parts[0].contentEquals("loginsuccess")) {
            context.startActivity(new Intent(context, Menu.class));

        } else if (parts[0].contentEquals("Devices:")) {
            String[] yourArray = Arrays.copyOfRange(parts, 1, parts.length);
            if (yourArray.length > 0) {
                String deviceString = "";
                for (int i = 0; i < yourArray.length; i++) {
                    deviceString += yourArray[i] + " ";
                }
                SharedPreferences devices = context.getSharedPreferences("Device", MODE_PRIVATE);
                SharedPreferences.Editor edit = devices.edit();
                edit.clear();
                edit.putString("Devices", deviceString);
                edit.commit();
                context.startActivity(new Intent(context, scanbeacons.class));
            } else {
                alertDialog.setMessage("No devices found in database");
                alertDialog.show();
            }

        } else if (parts[0].contentEquals("loginnot")) {
            alertDialog.setMessage(result);
            alertDialog.show();

        } else if (parts[0].contentEquals("Rooms:")) {
            String[] yourArray = Arrays.copyOfRange(parts, 1, parts.length);
            if (yourArray.length > 0) {
                String roomString = "";
                for (int i = 0; i < yourArray.length; i++) {
                    roomString += yourArray[i] + " ";
                }
                SharedPreferences devices = context.getSharedPreferences("Room", MODE_PRIVATE);
                SharedPreferences.Editor edit = devices.edit();
                edit.clear();
                edit.putString("Room", roomString);
                edit.commit();
                if(keuzeRoom == 0) {
                    context.startActivity(new Intent(context, ChooseRoomScanBeacons.class));
                } else if (keuzeRoom == 1) {
                    context.startActivity(new Intent(context, addDevice.class));
                }

            }

        }else if(parts[0].contentEquals("Roomsnot")) {
            alertDialog.setMessage("No rooms found, register first a room");
            alertDialog.show();

        } else if(parts[0].contentEquals("InsertDeviceSuccesfull")) {
            context.startActivity(new Intent(context, Menu.class));

        } else if(parts[0].contentEquals("InsertRoomSuccessfull")) {
            context.startActivity(new Intent(context, Menu.class));
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
    }
}
