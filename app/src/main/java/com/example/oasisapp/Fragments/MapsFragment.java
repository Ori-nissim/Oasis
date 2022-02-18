package com.example.oasisapp.Fragments;

import static com.example.oasisapp.Activities.MainActivity.lats;
import static com.example.oasisapp.Activities.MainActivity.longs;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oasisapp.Activities.MainActivity;
import com.example.oasisapp.Models.Location;
import com.example.oasisapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    private DatabaseReference databaseReference;
    private ArrayList<Location> locationArrayList;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng home;
            if (MainActivity.lats != 0){
                home = new LatLng(MainActivity.lats, MainActivity.longs);
            } else {
                 home = new LatLng(32.069, 34.829);
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(home));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(9));



            // get array list
            locationArrayList = new ArrayList<>();
            databaseReference = FirebaseDatabase.getInstance().getReference("locations");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Location location = dataSnapshot.getValue(Location.class);
                        locationArrayList.add(location);

                        MarkerOptions markerOptions= new MarkerOptions();
                        LatLng latlng = new LatLng(location.getLats(), location.getLongs());
                        googleMap.addMarker(markerOptions.position(latlng)
                                .title(location.getName())
                        ).showInfoWindow();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}