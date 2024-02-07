package com.Book_Exchange_Application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.Files_Mgmt.Data_Files;
import com.Server.User_Data;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Portrait_Book extends Base_Activity   implements NavigationView.OnNavigationItemSelectedListener
{
    private TextInputEditText editTextDate;
    private DrawerLayout drawerLayout;
    private User_Data ud;
    public String un;
    private Data_Files df = new Data_Files();
    private Handler hnd = new Handler();
    public static final String PREFS_NAME = "MyPrefsFile";
    private boolean log_flag;
    private SharedPreferences preferences;
    @Override
    public void Process_Result(String...params)
    {
        String res = params[0];
        this.Show_Message(res);
    }
    public void Register_Book()
    {

        HashMap<String,String> dt = new HashMap<String,String>();
        String tmp;
        tmp = ((EditText) findViewById(R.id.name)).getText().toString().strip();
        if(tmp.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter book name",Toast.LENGTH_LONG).show();
            return;
        }
        dt.put("name",tmp);
        tmp = ((EditText) findViewById(R.id.language)).getText().toString().strip();
        if(tmp.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter book language",Toast.LENGTH_LONG).show();
            return;
        }
        dt.put("language",tmp);

        DatePicker datePicker = (DatePicker) findViewById(R.id.purchasedate);

        // Get the selected year, month, and day
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

        // Create a Calendar instance and set the selected date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

        // Format the date into the "yyyy-MM-dd" format
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String formattedDate = dateFormat.format(calendar.getTime());
        dt.put("purchasedate",formattedDate);
        tmp = ((EditText) findViewById(R.id.copycount)).getText().toString().strip();
        if(tmp.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter book copy count",Toast.LENGTH_LONG).show();
            return;
        }
        dt.put("copycount",tmp);
        dt.put("genre",((Spinner) findViewById(R.id.genre)).getSelectedItem().toString().strip());
        dt.put("isbn",((EditText) findViewById(R.id.isbn)).getText().toString().strip());
        dt.put("author",((EditText) findViewById(R.id.author)).getText().toString().strip());
        dt.put("edition",((EditText) findViewById(R.id.edition)).getText().toString().strip());
        dt.put("condition",((Spinner) findViewById(R.id.condition)).getSelectedItem().toString().strip());
        Log.i("Data","mroe"+dt.toString());
        try {
            JSONObject jsn = this.df.Get_User();
            dt.put("userid",(String)(jsn.get("userid")));
        }
        catch (Exception e)
        {
            Log.i("Data",e.getMessage());
        }
        // Log.i("Data",dt.toString());
        this.ud.sendData(Portrait_Book.this,"Book_Register",dt);
        hnd.postDelayed(new Runnable() {
            @Override
            public void run() {
                Portrait_Book.this.ud.ReceiveData(Portrait_Book.this,"User_Books",null);
            }
        },  500);
        hnd.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },3000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portrait_book);
        setTitle("");
        // Flag Variables:
            // Initialize SharedPreferences
            preferences = getSharedPreferences(PREFS_NAME, getApplicationContext().MODE_PRIVATE);

            this.ud = new User_Data(preferences);

            log_flag = preferences.getBoolean("LOG",false);

            // log_flag = true;
        // UI
            // editTextDate = (TextInputEditText) findViewById(R.id.purchasedate);
            // Set onTouchListener to the DatePicker
            DatePicker datePicker = (DatePicker)findViewById(R.id.purchasedate);

            Spinner spinner = findViewById(R.id.genre);
            // Define the array of options
            String[] options = {"Education",
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
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, options);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            spinner = findViewById(R.id.condition);
            // Define the array of options
            String[] options1 = {"Like New",
                    "New",
                    "Poor",
                    "Very Good",
                    "Fair",
                    "Good",
                    "Ex-Library",
                    "Book Club Edition",
                    "Signed",
                    "Uncorrected Proof",
            };

            // Create an ArrayAdapter using the string array and a default spinner layout
            adapter = new ArrayAdapter<>(this, R.layout.spinner_item, options1);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            Button bt = (Button)findViewById(R.id.bookregister);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Portrait_Book.this.Register_Book();
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
                    SharedPreferences.Editor editor = Portrait_Book.this.preferences.edit();
                    editor.putBoolean("LOG", false);
                    editor.apply();
                }
                /// Forward to the main Activity
                Intent intent = new Intent(Portrait_Book.this, Main_Portrait_Activity.class);
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
                    Intent intent = new Intent(Portrait_Book.this, to_act);
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