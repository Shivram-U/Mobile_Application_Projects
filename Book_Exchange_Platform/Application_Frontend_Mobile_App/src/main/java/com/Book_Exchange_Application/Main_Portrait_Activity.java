package com.Book_Exchange_Application;

import com.Files_Mgmt.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;

import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;

public class Main_Portrait_Activity extends Base_Activity   implements NavigationView.OnNavigationItemSelectedListener
{
    private static final int REQUEST_MANAGE_EXTERNAL_STORAGE = 2;
    private Handler hnd = new Handler();
    private DrawerLayout drawerLayout;
    private NavigationView nv;
    public static final String PREFS_NAME = "MyPrefsFile";
    private boolean log_flag;
    private SharedPreferences preferences;
    private void checkWriteExternalStoragePermission() {
        // Check if the permission has been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            Log.i("Data","Not Granted");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    nv.setVisibility(View.GONE);
                    Toast.makeText(this, "Please provide file managing permission.", Toast.LENGTH_LONG).show();
                    hnd.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivityForResult(intent,REQUEST_MANAGE_EXTERNAL_STORAGE);
                        }
                    }, 3000);

                    // The MANAGE_EXTERNAL_STORAGE permission is not granted
                    // Redirect the user to the system settings to grant the permission
                }
            }
        }
        Log.i("Data","Granted");
    }

    // Handle the result of the permission-granting activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_MANAGE_EXTERNAL_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager())
                {
                    // Permission denied, inform the user
                    Toast.makeText(this, "File Permissions are required for the Application.\nShutting down in 5 seconds", Toast.LENGTH_LONG).show();
                    hnd.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 5000);
                }
                else
                {
                    nv.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_portrait_activity);
        setTitle("");
        // UI Elements:
            nv = (NavigationView)findViewById(R.id.nav_view);
            Data_Files df = new Data_Files();
        // Flag Variables:
            // Initialize SharedPreferences
            preferences = getSharedPreferences(PREFS_NAME, getApplicationContext().MODE_PRIVATE);

            // Check if the flag is already set, if not, set it
            boolean isFlagSet;
            isFlagSet = preferences.getBoolean("LOG", false);

            SharedPreferences.Editor editor = preferences.edit();
            if(!isFlagSet)
            {
                editor.putBoolean("LOG", false);
                editor.apply();
            }


            log_flag = preferences.getBoolean("LOG",false);

            // log_flag = true;
        // Permissions:
            // Check if the app has the WRITE_EXTERNAL_STORAGE permission
            checkWriteExternalStoragePermission();
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
            if(log_flag)
            {
                navigationView.inflateMenu(R.menu.navigation_menu_log);
            }
            else
            {
                navigationView.inflateMenu(R.menu.navigation_menu);
            }

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
                    SharedPreferences.Editor editor = Main_Portrait_Activity.this.preferences.edit();
                    editor.putBoolean("LOG", false);
                    editor.apply();
                }
                // Restart the Current Activity
                Intent intent = getIntent();
                finish(); // Finish the current activity
                startActivity(intent);
            }
            else {
                if (id == R.id.nav_home){}
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
                        else
                            to_act = Portrait_Signup.class;
                    }
                }
                // Inside the current activity (e.g., in response to a button click or some other event)
                if(to_act!=null)
                {
                    Intent intent = new Intent(Main_Portrait_Activity.this, to_act);
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