package com.Book_Exchange_Application;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.Files_Mgmt.Data_Files;

import java.util.HashMap;
import java.util.Map;

public class Base_Activity extends AppCompatActivity
{
    public Data_Files df = new Data_Files();
    public void Show_Message(String message)
    {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
    public void Process_Result(String...params)
    {
    }

}
