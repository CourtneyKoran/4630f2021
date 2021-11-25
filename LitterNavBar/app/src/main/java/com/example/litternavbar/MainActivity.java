package com.example.litternavbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends FragmentActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);



    }



    ImpactFragment firstFragment = new ImpactFragment();
    LitterMapFragment secondFragment = new LitterMapFragment();
    HomeFragment thirdFragment = new HomeFragment();
    ChallengesFragment fourthFragment = new ChallengesFragment();
    ConnectFragment fifthFragment = new ConnectFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_impact:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, firstFragment).commit();
                //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                //mapFragment.getMapAsync();
               // mapFragment.getMapAsync(this);

                return true;

            case R.id.nav_litterMap:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, secondFragment).commit();
                return true;

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, thirdFragment).commit();
                return true;

            case R.id.nav_challenges:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fourthFragment).commit();
                return true;

            case R.id.nav_connect:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fifthFragment).commit();
                return true;
        }
        return false;
    }
}