package com.example.thomas.erasmusproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

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
                String UUID1  = params[1];
                String UUID2  = params[2];
                String UUID3  = params[3];
                String UUID4  = params[4];

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
                String post_data = URLEncoder.encode("UUID1", "UTF-8") + "=" + URLEncoder.encode(UUID1, "UTF-8") + "&"
                        + URLEncoder.encode("UUID2", "UTF-8") + "=" + URLEncoder.encode(UUID2, "UTF-8") + "&"
                        + URLEncoder.encode("UUID3", "UTF-8") + "=" + URLEncoder.encode(UUID3, "UTF-8") + "&"
                        + URLEncoder.encode("UUID4", "UTF-8") + "=" + URLEncoder.encode(UUID4, "UTF-8");
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
                String UUID  = params[1];
                String Name  = params[2];


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
                String post_data = URLEncoder.encode("UUID", "UTF-8") + "=" + URLEncoder.encode(UUID, "UTF-8") + "&"
                        + URLEncoder.encode("Name", "UTF-8") + "=" + URLEncoder.encode(Name, "UTF-8");
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
        String[] parts = result.split("-");
        if(parts[0].contentEquals("loginsuccess")) {

            context.startActivity(new Intent(context, Menu.class));

        }else if(parts[0].contentEquals("Devices:")) {

            String[] yourArray = Arrays.copyOfRange(parts, 1, parts.length);

            if(yourArray.length > 0) {

                StringBuilder stringBuilder = new StringBuilder();
                for(String s: yourArray) {
                    stringBuilder.append(s);
                    stringBuilder.append(" ");
                }
                SharedPreferences devices = context.getSharedPreferences("Device", MODE_PRIVATE);
                SharedPreferences.Editor edit = devices.edit();
                edit.clear();
                edit.putString("Devices", devices.toString());
                edit.commit();

                context.startActivity(new Intent(context, scanbeacons.class));
            } else {
                alertDialog.setMessage("No devices found in database");
                alertDialog.show();
            }


        } else if(parts[0].contentEquals("loginnot")) {
            alertDialog.setMessage(result);
            alertDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
    }
}
