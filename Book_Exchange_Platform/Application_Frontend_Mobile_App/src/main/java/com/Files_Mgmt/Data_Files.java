package com.Files_Mgmt;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class Data_Files
{
    public String STDIR,DIR,UDIR,UCDIR;
    public File fl,dir;
    public void DIR_Setup()
    {
        try
        {
            dir = new File(this.DIR);
            dir.mkdirs();
            dir = new File(this.UDIR);
            dir.mkdirs();
            dir = new File(this.UCDIR);
            dir.mkdirs();
        }
        catch(Exception e)
        {
            Log.i("Data",e.getMessage());
        }
    }
    public Data_Files()
    {
        this.STDIR = String.valueOf(Environment.getExternalStorageDirectory());
        Log.i("Data",this.STDIR);
        this.DIR = this.STDIR+"/Book_Exchange_Platform/";
        this.UDIR = this.DIR+"/User/";
        this.UCDIR = this.DIR+"/User/User_Chat/";
        try
        {
            dir = new File(this.DIR);
            if(!dir.exists())
                this.DIR_Setup();
        }
        catch(Exception e)
        {
            Log.i("Data",e.getMessage());
        }
    }
    public void Create_File(String filename)
    {
        try
        {
            File fil = new File(filename);
            if(!fil.exists())
                fil.createNewFile();
        }
        catch(Exception e)
        {
            Log.i("Data",e.getMessage());
        }
    }
    public void Delete_File(String filename)
    {
        try
        {
            File fil = new File(filename);
            if(fil.exists())
                fil.delete();
        }
        catch(Exception e)
        {
            Log.i("Message",e.getMessage());
        }
    }
    public JSONObject Parse_JSON_String(String jsonString) {
        try {
            return new JSONObject(jsonString);
        } catch (Exception e) {
            Log.i("Data", "Error parsing JSON string: " + e.getMessage());
            return null;
        }
    }
    public JSONObject Read_JSON(String filename)
    {
        try {
            Log.i("Data",filename);
            File fil = new File(filename + ".json");
            FileReader fr = new FileReader(fil);
            BufferedReader fbr = new BufferedReader(fr);
            String line = fbr.readLine();
            Log.i("Data", String.valueOf(line));
            JSONObject rd = new JSONObject(line);
            // Log.i("Message", rd.get("Developer").toString());
            return rd;
        }
        catch(Exception e)
        {
            Log.i("Data",e.getMessage());
        }
        return null;
    }

    public void Create_User(JSONObject dat)
    {
        try
        {
            this.Create_File(this.UDIR + "User_Data.json");
            this.Create_File(this.UDIR + "User_Books.json");
            JSONObject fil = new JSONObject();
            fil.put("userid", dat.get("userid"));
            fil.put("username", dat.get("username"));
            fil.put("password", dat.get("password"));
            fil.put("address", dat.get("address"));
            fil.put("joineddate", dat.get("joineddate"));
            fil.put("reputationscore", dat.get("reputationscore"));
            fil.put("phonenumber", dat.get("contactno"));
            fil.put("email", dat.get("email"));
            FileWriter fw = new FileWriter(this.UDIR +"User_Data.json");
            if(fil!=null){
                fw.write(fil.toString());
                fw.flush();
            }
            fw.close();
        }
        catch (Exception e)
        {
            Log.i("Data",e.getMessage());
        }
    }
    public void Delete_User()
    {
        this.Delete_File(this.UDIR + "User_Data");
        this.Delete_File(this.UDIR + "User_Books");
        File directory = new File(this.UCDIR);
        File[] files = directory.listFiles();
        // Check if there are any files
        if (files != null) {
            // Iterate through each file and delete it
            for (File file : files) {
                file.delete();
            }
        }
    }

    public JSONObject Get_User()
    {
        return Read_JSON(this.UDIR+"User_Data");
    }

    public JSONObject Get_User_Books()
    {
        return Read_JSON(this.UDIR+"User_Books");
    }
    public void Create_User_Books(JSONObject jsn)
    {
        try
        {
            FileWriter fw = new FileWriter(this.UDIR +"User_Books.json");
            if(jsn!=null){
                fw.write(jsn.toString());
                fw.flush();
            }
        }
        catch(Exception e)
        {
            Log.i("Data",e.getMessage());
        }
    }
    public JSONObject Get_Search_Results()
    {
        return Read_JSON(this.UDIR+"Search_Result");
    }
    public void Create_Search_Result(JSONObject jsn)
    {
        try
        {
            FileWriter fw = new FileWriter(this.UDIR +"Search_Result.json");
            if(jsn!=null){
                fw.write(jsn.toString());
                fw.flush();
            }
        }
        catch(Exception e)
        {
            Log.i("Data",e.getMessage());
        }
    }
    public void Flush_Search_Result()
    {
        try
        {
            FileWriter fw = new FileWriter(this.UDIR +"Search_Result.json");
            fw.flush();
        }
        catch(Exception e)
        {
            Log.i("Data",e.getMessage());
        }
    }
    public void Update_JSON(String fn,String[] args)
    {
        try {
            JSONObject fil = null;
            if(fn.equals("User_Data")) {
                this.Create_File(this.UDIR+fn+".json");
                fil = this.Read_JSON(this.UDIR+"User_Data");
                fil.put("userid",args[0]);
                fil.put("username",args[1]);
                fil.put("password",args[2]);
                fil.put("address",args[3]);
                fil.put("phonenumber",args[4]);
                fil.put("email",args[5]);
            }
            fn = fn+".json";
            FileWriter fw = new FileWriter(this.DIR + "/"+fn);
            if(fil!=null){
                fw.write(fil.toString());
                fw.flush();
            }
            fw.close();
        }
        catch(Exception e){
            Log.i("Exception",e.getMessage());
        }
        Log.i("Message","Update done");
    }
    public void Write_JSON(String fn)
    {
        try {
            JSONObject fil = new JSONObject();
            JSONArray jsa = new JSONArray();
            if(fn.equals("Data")) {
                this.Create_File(this.UDIR+fn+".json");
                jsa.put("Application Name");
                jsa.put("Developer");
                jsa.put("Year");
                jsa.put("Status");
                fil.put("FLDS",jsa);
                fil.put("Application Name", "General Chat");
                fil.put("Developer", "Shivram_U");
                fil.put("Year", "2023");
                fil.put("Status", "Under Development");
            }
            else if(fn.equals("Connection"))
            {
                jsa.put("Server_IP");
                jsa.put("Server_Port");
                fil.put("FLDS",jsa);
                fil.put("Server_IP", "$");
                fil.put("Server_Port", "0000");
            }
            else if(fn.equals("Clients"))
            {
                jsa.put("Clients");
                fil.put("FLDS",jsa);
                fil.put("Clients",new JSONArray());
            }
            else if(fn.equals("User"))
            {
                jsa.put("Name");
                jsa.put("Language");
                jsa.put("Address");
                jsa.put("Nationality");
                fil.put("FLDS",jsa);
                fil.put("Name","User name");
                fil.put("Language","User Language");
                fil.put("Address","User Address");
                fil.put("Nationality","User Nationality");
            }
            else if(fn.equals("Server"))
            {
                jsa.put("Server_ID");
                jsa.put("Server_Status");
                jsa.put("Server_Clients_Count");
                fil.put("FLDS",jsa);
                fil.put("Server_ID","$");
                fil.put("Server_Status","OFFLINE");
                fil.put("Server_Clients_Count",0);
            }
            fn = fn+".json";
            FileWriter fw = new FileWriter(this.DIR + "/"+fn);
            fw.write(fil.toString());
            fw.flush();
            fw.close();
        }
        catch(Exception e){
            Log.i("Exception",e.getMessage());
        }
        Log.i("Message","Write done");
    }

    @Override
    protected void finalize() {
        try {
            super.finalize();
            Log.i("Fragment","Home Page Destroyed");
        }
        catch(Throwable e)
        {
            Log.i("Exception",e.getMessage());
        }
    }
}