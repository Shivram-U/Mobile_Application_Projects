package com.Book_Exchange_Application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.Files_Mgmt.Data_Files;
import com.Server.User_Data;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.util.HashMap;

public class Portrait_Signup extends Base_Activity   implements NavigationView.OnNavigationItemSelectedListener
{
    private User_Data ud;
    private Data_Files df = new Data_Files();
    private DrawerLayout drawerLayout;
    private Handler hnd = new Handler();
    public static final String PREFS_NAME = "MyPrefsFile";
    private boolean log_flag;
    private int[] flds = {R.id.username,R.id.password,R.id.address,R.id.phonenumber,R.id.email};
    private SharedPreferences preferences;

    public void Signup()
    {
        EditText tmp;
        HashMap<String,String> dt = new HashMap<String,String>();
        String t;
        tmp = (EditText) findViewById(R.id.username);
        t = tmp.getText().toString().strip();
        if(t.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter username",Toast.LENGTH_SHORT).show();
            return;
        }
        dt.put("username",t);
        tmp = (EditText) findViewById(R.id.password);
        t = tmp.getText().toString().strip();
        if(t.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        dt.put("password",t);
        tmp = (EditText) findViewById(R.id.address);
        t = tmp.getText().toString().strip();
        if(t.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter address",Toast.LENGTH_SHORT).show();
            return;
        }
        dt.put("address",t);
        tmp = (EditText) findViewById(R.id.phonenumber);
        dt.put("phonenumber",tmp.getText().toString().strip());
        tmp = (EditText) findViewById(R.id.email);
        t = tmp.getText().toString().strip();
        if(t.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter mail-id",Toast.LENGTH_SHORT).show();
            return;
        }
        dt.put("email",t);
        this.ud.sendData(Portrait_Signup.this,"Signup",dt);
    }
    @Override
    public void Process_Result(String...params)
    {
        String res = params[0];
        this.Show_Message(res);
        if(res.equals("Registration Authorized"))
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("LOG", true);
            editor.apply();
            this.ud.ReceiveData(this,"User_Data",params[1]);
            hnd.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Portrait_Signup.this.ud.ReceiveData(Portrait_Signup.this,"User_Books",null);
                }
            },1000);
            hnd.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Portrait_Signup.this, Portrait_Profile.class);
                    startActivity(intent);
                    // Finish (destroy) the current activity
                    finish();
                }
            },3000);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portrait_signup);
        setTitle("");
        // Flag Variables:
            // Initialize SharedPreferences
            preferences = getSharedPreferences(PREFS_NAME, getApplicationContext().MODE_PRIVATE);

            this.ud = new User_Data(preferences);

            log_flag = preferences.getBoolean("LOG",false);

            // log_flag = true;
        // Layouts
            drawerLayout = findViewById(R.id.drawer_layout);
        // Tool Bar :
            Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
            setSupportActionBar(toolbar);

            // Reference Toolbar items
            ImageButton toolbarNavigationButton = findViewById(R.id.toolbarNavigationButton);

            // Set up navigation button click listener, for example
            toolbarNavigationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    // Handle navigation button click
                    toggleDrawer();
                }
            });
        // Navigation Bar
            // Reference : https://androidknowledge.com/navigation-drawer-android-studio/
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            // navigationView.getMenu().clear(); // Clear existing menu
            navigationView.inflateMenu(R.menu.navigation_menu);

        // Events:
            Button signup = (Button)findViewById(R.id.signup);
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Portrait_Signup.this.Signup();
                }
            });
    }
    // UI Functions:
        private void toggleDrawer() {
            if (drawerLayout.isDrawerOpen(findViewById(R.id.nav_view))) {
                drawerLayout.closeDrawer(findViewById(R.id.nav_view));
            } else {
                drawerLayout.openDrawer(findViewById(R.id.nav_view));
            }
        }
    // Events
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            Class to_act = null;
            /*
            switch (item.getItemId()) {
                case (R.id.nav_home):   -> error: constant expression required
            }
            */
            /*
                // Inside the current activity (e.g., in response to a button click or some other event)
                Intent intent = new Intent(CurrentActivity.this, NewActivity.class);
                startActivity(intent);

                // Finish (destroy) the current activity
                finish();
            */
            if(id == R.id.nav_logout)
            {
                if(preferences.getBoolean("LOG",false))
                {
                    SharedPreferences.Editor editor = Portrait_Signup.this.preferences.edit();
                    editor.putBoolean("LOG", false);
                    editor.apply();
                }
                // Forward to the main Activity
                Intent intent = new Intent(Portrait_Signup.this, Main_Portrait_Activity.class);
                startActivity(intent);
                finish(); // Finish the current activity
            }
            else {
                if (id == R.id.nav_home)
                    to_act = Main_Portrait_Activity.class;
                else if (id == R.id.nav_search)
                    to_act = Portrait_Search.class;
                else if (id == R.id.nav_about)
                    to_act = Portrait_About.class;
                else if (id == R.id.nav_settings)
                    to_act = Portrait_Settings.class;
                else if (id == R.id.nav_server)
                    to_act = Server_Customize.class;
                else
                {
                    if(preferences.getBoolean("LOG",false))
                    {
                        if(id == R.id.nav_profile)
                            to_act = Portrait_Profile.class;
                        else
                            to_act = Portrait_Books_Data.class;
                    }
                    else
                    {
                        if(id == R.id.nav_login)
                            to_act = Portrait_Login.class;
                    }
                }
                // Inside the current activity (e.g., in response to a button click or some other event)
                if(to_act!=null)
                {
                    Intent intent = new Intent(Portrait_Signup.this, to_act);
                    startActivity(intent);
                    // Finish (destroy) the current activity
                    finish();
                }
                // Handle Home click
            }
            // Close the navigation drawer
            drawerLayout.closeDrawers();
            return true;
        }
        @Override
        public void onBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
}