package com.Server;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.Book_Exchange_Application.Base_Activity;
import com.Files_Mgmt.Data_Files;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class User_Data
{
    private Data_Files df = new Data_Files();
    final static Handler hnd = new Handler();
    public Base_Activity ui;
    public boolean set = true;
    private SharedPreferences preferences;
    String ServerURL = "http://";
    // Constructor:
    public User_Data(SharedPreferences prf)
    {
        preferences = prf;
        String str = preferences.getString("SERVER_IP","UNSET");
        if(str.equals("UNSET"))
        {
            set = false;
        }

        if(set)
        {
            ServerURL = ServerURL+preferences.getString("SERVER_IP","")+":"+preferences.getString("SERVER_PORT","")+"/";
        }
    }
    public void test() {
        if(set)
            new TestServerTask(this.ServerURL).execute();
    }

    private static class TestServerTask extends AsyncTask<Void, Void, String> {
        public String serverurl;
        public TestServerTask(String serverurl)
        {
            this.serverurl = serverurl;
        }
        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(this.serverurl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set the request method to GET
                connection.setRequestMethod("GET");

                // Get the response code
                int responseCode = connection.getResponseCode();

                // Read the response content
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Log.i("Data",response.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }
        }
    public void sendData(Base_Activity ui,String command,Map<String, String> formData) {
        this.ui = ui;
        // Execute AsyncTask to perform the HTTP request in the background
        Log.i("Data", formData.toString());
        if(command.equals("Login"))
            new SendDataToServerTask().execute(this.ServerURL+"Book_Exchange_Application/Login/", formData,ui);
        else if(command.equals("Signup"))
            new SendDataToServerTask().execute(this.ServerURL+"Book_Exchange_Application/Signup/", formData,ui);
        else if(command.equals("Book_Register"))
            new SendDataToServerTask().execute(this.ServerURL+"Book_Exchange_Application/Book_Register/", formData,ui);
        else if(command.equals("Book_Update"))
            new SendDataToServerTask().execute(this.ServerURL+"Book_Exchange_Application/Book_Update/", formData,ui);
    }
    public void ReceiveData(Base_Activity ui, String command, String un, HashMap<String,String>...dts) {
        this.ui = ui;
        try
        {
            if(command.equals("User_Data"))
            {
                new ReceiveDataFromServer().execute(1,this.ServerURL+"Book_Exchange_Application/User_Data/", un,ui);
            }
            else if(command.equals("User_Books"))
            {
                JSONObject jsn = this.df.Get_User();
                String uid = (String)jsn.get("userid");
                new ReceiveDataFromServer().execute(2,this.ServerURL+"Book_Exchange_Application/Get_User_Books/", uid,ui);
            }
            else if(command.equals("Book_Search"))
            {
                new ReceiveDataFromServer().execute(3,this.ServerURL+"Book_Exchange_Application/Search_Books/",ui,dts[0]);
            }
        }
        catch (Exception e)
        {
            Log.i("Data",e.getMessage());
        }
    }


    // AsyncTask to handle HTTP request in the background
    private static class ReceiveDataFromServer extends AsyncTask<Object, Void, Void> {
        public Base_Activity ui;
        public String result;

        @Override
        protected Void doInBackground(Object... params) {
            Integer rq = (Integer)params[0];
            String serverUrl = (String) params[1];

            Log.i("Data","userdata");

            Uri.Builder builder = Uri.parse(serverUrl).buildUpon();
            try {
                if(rq == 1) {
                    String un = (String) params[2];
                    ui = (Base_Activity) params[3];
                    builder.appendQueryParameter("username", un);

                    String finalUrl = builder.build().toString();
                    Log.i("Data", finalUrl);

                    URL url = new URL(finalUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");

                    // Get the response code (optional, for handling server response)
                    int responseCode = connection.getResponseCode();
                    StringBuilder response;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        response = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        Log.i("Data", response.toString());
                    }
                    this.result = response.toString();
                    JSONObject jsn = ui.df.Parse_JSON_String(result);
                    ui.df.Create_User(jsn);
                    // Handle the server response if needed
                    Log.i("Data", result);
                }
                else if(rq == 2) {
                    String un = (String) params[2];
                    ui = (Base_Activity) params[3];
                    builder.appendQueryParameter("userid", un);

                    String finalUrl = builder.build().toString();
                    Log.i("Data", finalUrl);

                    URL url = new URL(finalUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");

                    // Get the response code (optional, for handling server response)
                    int responseCode = connection.getResponseCode();
                    StringBuilder response;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        response = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        // Log.i("Data", response.toString());
                    }
                    this.result = response.toString();
                    JSONObject jsn = ui.df.Parse_JSON_String(result);
                    ui.df.Create_User_Books(jsn);
                    // Handle the server response if needed
                    Log.i("Data",result);
                }
                else if(rq == 3) {
                    ui = (Base_Activity) params[2];
                    HashMap<String,String> dt = (HashMap<String,String>) params[3];
                    for (Map.Entry<String, String> entry : dt.entrySet()) {
                        builder.appendQueryParameter(entry.getKey(), entry.getValue());
                    }

                    String finalUrl = builder.build().toString();
                    Log.i("Data", finalUrl);

                    URL url = new URL(finalUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");

                    // Get the response code (optional, for handling server response)
                    int responseCode = connection.getResponseCode();
                    StringBuilder response;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        response = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        // Log.i("Data", response.toString());
                    }
                    this.result = response.toString();
                    JSONObject jsn = ui.df.Parse_JSON_String(result);
                    ui.df.Create_Search_Result(jsn);
                    // Handle the server response if needed
                    Log.i("Data",result);
                }
            }
            catch(IOException ie) {
                ui.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Log.i("Data","SENT");
                        ui.Process_Result("Server offline, please try again later"); // Show toast or handle the result in your activity
                    }
                });
                return null;
            }
            catch(Exception e) {
                e.printStackTrace();
                Log.i("Data", e.toString());
                return null;
            }
            Log.i("Data", "success");
            return null;
        }
    }

    // AsyncTask to handle HTTP request in the background
    private static class SendDataToServerTask extends AsyncTask<Object, Void, Void> {
        public Base_Activity ui;
        public String result;

        @Override
        protected Void doInBackground(Object... params) {
            String serverUrl = (String) params[0];
            Map<String, String> formData = (Map<String, String>) params[1];
            ui = (Base_Activity) params[2];
            Log.i("Data", serverUrl);

            try {
                Uri.Builder builder = Uri.parse(serverUrl).buildUpon();

                for (Map.Entry<String, String> entry : formData.entrySet()) {
                    builder.appendQueryParameter(entry.getKey(), entry.getValue());
                }

                String finalUrl = builder.build().toString();
                Log.i("Data", finalUrl);

                URL url = new URL(finalUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set the request method to GET
                connection.setRequestMethod("GET");

                // Get the response code (optional, for handling server response)
                int responseCode = connection.getResponseCode();
                StringBuilder response;

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Log.i("Data", response.toString());
                }

                this.result = response.toString();

                // Handle the server response if needed
                Log.i("Data", String.valueOf(responseCode));

            } catch (IOException ie) {
                ui.runOnUiThread(() -> ui.Process_Result("Server offline, please try again later"));
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Data", e.toString());
                return null;
            }

            Log.i("Data", "success");
            ui.runOnUiThread(() -> ui.Process_Result(result,formData.get("username")));

            return null;
        }
    }
}
