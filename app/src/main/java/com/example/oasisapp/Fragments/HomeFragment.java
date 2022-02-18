package com.example.oasisapp.Fragments;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.constraintlayout.motion.widget.Debug.getLocation;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oasisapp.Activities.MainActivity;
import com.example.oasisapp.Adapters.LocationAdapter;
import com.example.oasisapp.Listeners.itemClickRV;
import com.example.oasisapp.Models.Location;
import com.example.oasisapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements itemClickRV {

    private RecyclerView home_fragment;
    private   LocationAdapter locationAdapter;
    private DatabaseReference databaseReference;
    private ArrayList<Location> locationArrayList;
    public MaterialToolbar toolbar_main;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // get array list
        locationArrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("locations");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Location location = dataSnapshot.getValue(Location.class);
                    locationArrayList.add(location);
                }
                    locationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        View view = inflater.inflate(R.layout.home_fragment,container,false);
        home_fragment = view.findViewById(R.id.home_RV_main);
        locationAdapter = new LocationAdapter(this.getActivity(), locationArrayList,this);

        home_fragment.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false));
        home_fragment.setHasFixedSize(true);
        home_fragment.setItemAnimator(new DefaultItemAnimator());
        home_fragment.setAdapter(locationAdapter);

        toolbar_main = view.findViewById(R.id.toolbar_main);
       return view;
    }


    @Override
    public void onItemClicked(Location location) {
        MainActivity.lats = location.getLats();
        MainActivity.longs = location.getLongs();
        Toast.makeText(this.getContext(), "Press on Map to see the location on the map", Toast.LENGTH_SHORT).show();
    }



}
