package com.Book_Exchange_Application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.Files_Mgmt.Data_Files;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

public class Portrait_Profile extends Base_Activity   implements NavigationView.OnNavigationItemSelectedListener
{
    private Data_Files df = new Data_Files();
    private DrawerLayout drawerLayout;
    public static final String PREFS_NAME = "MyPrefsFile";
    private boolean log_flag;
    private SharedPreferences preferences;
    public void Load_Data()
    {
        try {
            JSONObject js = df.Read_JSON(df.UDIR+"User_Data");
            EditText tmp;
            tmp = (EditText) findViewById(R.id.userid);
            tmp.setText(js.get("userid").toString());
            tmp = (EditText) findViewById(R.id.username);
            tmp.setText(js.get("username").toString());
            tmp = (EditText) findViewById(R.id.password);
            tmp.setText(js.get("password").toString());
            tmp = (EditText) findViewById(R.id.joineddate);
            tmp.setText(js.get("joineddate").toString());
            tmp = (EditText) findViewById(R.id.reputationscore);
            tmp.setText(js.get("reputationscore").toString());
            tmp = (EditText) findViewById(R.id.address);
            tmp.setText(js.get("address").toString());
            tmp = (EditText) findViewById(R.id.phonenumber);
            tmp.setText(js.get("phonenumber").toString());
            tmp = (EditText) findViewById(R.id.mailid);
            tmp.setText(js.get("email").toString());
        }
        catch(Exception e)
        {
            Log.i("Data",e.getMessage());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portrait_profile);
        setTitle("");
        this.Load_Data();
        // Flag Variables:
            // Initialize SharedPreferences
            preferences = getSharedPreferences(PREFS_NAME, getApplicationContext().MODE_PRIVATE);

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
                    SharedPreferences.Editor editor = Portrait_Profile.this.preferences.edit();
                    editor.putBoolean("LOG", false);
                    editor.apply();
                }
                // Forward to the main Activity
                Intent intent = new Intent(Portrait_Profile.this, Main_Portrait_Activity.class);
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
                        if(id == R.id.nav_profile){}
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
                    Intent intent = new Intent(Portrait_Profile.this, to_act);
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