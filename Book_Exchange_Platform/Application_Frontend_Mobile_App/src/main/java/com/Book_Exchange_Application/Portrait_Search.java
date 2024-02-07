package com.Book_Exchange_Application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.Files_Mgmt.Data_Files;
import com.Server.User_Data;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Portrait_Search extends Base_Activity   implements NavigationView.OnNavigationItemSelectedListener
{
    private TextInputEditText editTextDate;
    private DrawerLayout drawerLayout;
    private User_Data ud;
    public JSONObject userb;
    private Data_Files df = new Data_Files();
    private Handler hnd = new Handler();
    public static final String PREFS_NAME = "MyPrefsFile";
    private boolean log_flag;
    public int in;
    private SharedPreferences preferences;
    public void Show_Book(int i)
    {

        try {
            Intent intent = new Intent(Portrait_Search.this, Portrait_Book_Display.class);
            intent.putExtra("jsonObject", this.userb.get(String.valueOf(i)).toString());
            // Start the activity
            startActivity(intent);
        }
        catch (Exception e)
        {
            Log.i("Data",e.getMessage());
        }
    }
    public void Setup_UI()
    {
        userb = df.Get_Search_Results();
        Log.i("Data",userb.toString());
        int n = userb.length();
        Log.i("Data",userb.toString());
        Log.i("Data",String.valueOf(n));
        LinearLayout parentLayout = findViewById(R.id.searchresult);
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
                public int ind = Portrait_Search.this.in;
                @Override
                public void onClick(View v) {
                    Portrait_Search.this.Show_Book(this.ind);
                }
            });

            // Add the dynamically created layout to the parent layout
            parentLayout.addView(dynamicLinearLayout);
        }
    }
    public void Search_Book()
    {
        HashMap<String,String> dt = new HashMap<String,String>();
        String tmp;
        tmp = ((EditText) findViewById(R.id.name)).getText().toString().strip();
        if(!tmp.equals(""))
            dt.put("name",tmp);
        tmp = ((EditText) findViewById(R.id.language)).getText().toString().strip();
        if(!tmp.equals(""))
            dt.put("language",tmp);
        tmp = ((Spinner) findViewById(R.id.genre)).getSelectedItem().toString().strip();
        if(!tmp.equals("None"))
            dt.put("genre",((Spinner) findViewById(R.id.genre)).getSelectedItem().toString().strip());
        tmp = ((EditText) findViewById(R.id.author)).getText().toString().strip();
        if(!tmp.equals(""))
            dt.put("author",tmp);
        Log.i("Data",dt.toString());
        // Log.i("Data",dt.toString());
        if(dt.size()>0) {
            this.ud.ReceiveData(Portrait_Search.this, "Book_Search", null, dt);
            hnd.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Portrait_Search.this.Setup_UI();
                }
            },1000);

        }
        else
            Toast.makeText(getApplicationContext(),"Please enter atleast one search data",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portrait_search);
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
            Spinner spinner = findViewById(R.id.genre);
            // Define the array of options
            String[] options = {"None",
                    "Education",
                    "Science",
                    "History",
                    "Biography",
                    "Narrative",
                    "Autobiograpy",
                    "Philosophy",
                    "Spirituality",
                    "Inspirational",
                    "Religion",
                    "Memoir",
                    "Novel",
                    "Self-Help",
                    "Travel",
                    "Cook-Books",
                    "Children's and Young Adult",
                    "Humor",
                    "Drama",
                    "Comics",
                    "Manga",
                    "Poetry",
                    "Fiction",
                    "Science Fiction",
                    "Historical Fiction",
                    "Adventure Fiction",
                    "Non-Fiction",
                    "Mystery",
                    "Fantasy",
                    "Thriller",
                    "Horror",
                    "Romance"
            };

            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_small, options);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_small);

            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            Button bt = (Button)findViewById(R.id.booksearch);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Portrait_Search.this.Search_Book();
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
                    SharedPreferences.Editor editor = Portrait_Search.this.preferences.edit();
                    editor.putBoolean("LOG", false);
                    editor.apply();
                }
                // Forward to the main Activity
                Intent intent = new Intent(Portrait_Search.this, Main_Portrait_Activity.class);
                startActivity(intent);
                finish(); // Finish the current activity
            }
            else {
                if (id == R.id.nav_home)
                    to_act = Main_Portrait_Activity.class;
                else if (id == R.id.nav_search){}
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
                    Intent intent = new Intent(Portrait_Search.this, to_act);
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
    protected void onDestroy() {
        super.onDestroy();
        this.df.Flush_Search_Result();
        // Perform cleanup tasks here
    }
}