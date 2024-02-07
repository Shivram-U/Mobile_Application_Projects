package com.Book_Exchange_Application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.Files_Mgmt.Data_Files;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

public class Portrait_Books_Data extends Base_Activity   implements NavigationView.OnNavigationItemSelectedListener
{
    public DrawerLayout drawerLayout;
    private boolean shouldRefreshUI = false;
    public JSONObject userb;
    public int in;
    private Data_Files df = new Data_Files();
    public static final String PREFS_NAME = "MyPrefsFile";
    private boolean log_flag;
    private SharedPreferences preferences;
    public void Show_Book(int i)
    {

        try {
            Intent intent = new Intent(Portrait_Books_Data.this, Portrait_Book_Display.class);
            intent.putExtra("jsonObject", this.userb.get(String.valueOf(i)).toString());
            // Start the activity
            startActivity(intent);
            shouldRefreshUI = true;
        }
        catch (Exception e)
        {
            Log.i("Data",e.getMessage());
        }
    }
    public void Setup_UI()
    {
        userb = df.Get_User_Books();
        int n = userb.length();
        Log.i("Data",userb.toString());
        Log.i("Data",String.valueOf(n));
        LinearLayout parentLayout = findViewById(R.id.bookcontent);
        parentLayout.removeAllViews();
        for(int i=0;i<n;i++)
        {
            in = i;
            // Get the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Inflate the book_info.xml layout
            View dynamicLinearLayout = inflater.inflate(R.layout.book_info, null);

            // Find TextViews and ImageButton in the dynamically inflated layout
            TextView textView1 = (TextView) dynamicLinearLayout.findViewById(R.id.t1);
            TextView textView2 = (TextView) dynamicLinearLayout.findViewById(R.id.t2);
            ImageButton button = (ImageButton) dynamicLinearLayout.findViewById(R.id.b1);

            // Set text to TextViews
            JSONObject tmp = userb;
            try {
                tmp = (JSONObject) userb.get(String.valueOf(i));
                textView1.setText(tmp.get("Name").toString());
                textView2.setText(tmp.get("BookID").toString());
            }
            catch(Exception e)
            {
                Log.i("Data",e.getMessage());
            }
            // Set OnClickListener for the button
            button.setOnClickListener(new View.OnClickListener() {
                public int ind = Portrait_Books_Data.this.in;
                @Override
                public void onClick(View v) {
                    Portrait_Books_Data.this.Show_Book(this.ind);
                }
            });

            // Add the dynamically created layout to the parent layout
            parentLayout.addView(dynamicLinearLayout);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portrait_books_data);
        setTitle("");
        // Flag Variables:
            // Initialize SharedPreferences
            preferences = getSharedPreferences(PREFS_NAME, getApplicationContext().MODE_PRIVATE);

            log_flag = preferences.getBoolean("LOG",false);

        // UI
            ImageButton bt = (ImageButton)findViewById(R.id.addavailablebook);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Portrait_Books_Data.this, Portrait_Book.class);
                    startActivity(intent);
                    shouldRefreshUI = true;
                }
            });
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
            this.Setup_UI();

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
                    SharedPreferences.Editor editor = Portrait_Books_Data.this.preferences.edit();
                    editor.putBoolean("LOG", false);
                    editor.apply();
                }
                /// Forward to the main Activity
                Intent intent = new Intent(Portrait_Books_Data.this, Main_Portrait_Activity.class);
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
                    Intent intent = new Intent(Portrait_Books_Data.this, to_act);
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
    @Override
    protected void onResume() {
        super.onResume();
        if (shouldRefreshUI) {
            this.Setup_UI(); // Refresh UI only if the flag is set
            shouldRefreshUI = false; // Reset the flag
        }
    }
}