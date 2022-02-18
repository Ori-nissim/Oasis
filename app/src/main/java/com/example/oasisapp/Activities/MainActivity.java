package com.example.oasisapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.oasisapp.Fragments.AddLocationFragment;
import com.example.oasisapp.Fragments.HomeFragment;
import com.example.oasisapp.Fragments.MapsFragment;
import com.example.oasisapp.Models.Location;
import com.example.oasisapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // RV
    private RecyclerView main_RV_Locations;

    // firebase features
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //components
    //buttons
    private MaterialButton map_BTN_main;
    private MaterialButton home_BTN_main;
    private MaterialButton addLocation_BTN_main;
    // tool bar
    public MaterialToolbar toolbar_main;
    private MenuItem logout_BTN_toolbar;

    public static double lats  ;
    public static double longs ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        findViews();
        homeButtonClicked();



        map_BTN_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapButtonClicked();

            }
        });

        home_BTN_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               homeButtonClicked();

            }
        });

        addLocation_BTN_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLocationButtonClicked();
            }
        });
    }

    private void addLocationButtonClicked() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String name = currentUser.getDisplayName();
        if(name != null)
        {
            toolbar_main.setTitle("Add a new location");
            toolbar_main.setSubtitle("Sharing is caring :)");

            Toast.makeText(this, "Hello " + name, Toast.LENGTH_SHORT).show();
            initAddLocationFragment();

        } else {
            Toast.makeText(this, "Only authenticated users can use this feature!", Toast.LENGTH_SHORT).show();
        }

    }

    private void initAddLocationFragment() {

        Fragment addLocationFragment = new AddLocationFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment,addLocationFragment)
                .commit();

    }

    private void homeButtonClicked() {
        lats = 0;
        toolbar_main.setTitle("Home Feed");
        toolbar_main.setSubtitle("All locations");
        initHomeFragment();
    }

    private void initHomeFragment() {
        Fragment homeFragment = new HomeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment,homeFragment)
                .commit();
    }

    public void mapButtonClicked() {
        toolbar_main.setTitle("Map view");
        toolbar_main.setSubtitle("Near your location");
        initMapFragment();
    }


    public void initMapFragment() {
        Fragment fragment = new MapsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment,fragment)
                .commit();
    }

    private void findViews() {
        map_BTN_main = findViewById(R.id.Map_BTN_main);
        toolbar_main = findViewById(R.id.toolbar_main);
        addLocation_BTN_main = findViewById(R.id.addLocation_BTN_main);
        home_BTN_main = findViewById(R.id.home_BTN_main);
        logout_BTN_toolbar = findViewById(R.id.logOut);
    }
    public void signOut(MenuItem item) {
        String id = mAuth.getInstance().getUid();
        mAuth.getInstance().signOut();
        Toast.makeText(this, "Signed Out: "+id , Toast.LENGTH_SHORT).show();
        Log.d("signedout",id);

        Intent intent = new Intent( MainActivity.this , LoginActivity.class);
        startActivity(intent);
    }


}