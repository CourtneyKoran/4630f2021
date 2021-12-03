package com.example.litternavbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.MenuItem;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

public class MainActivity extends FragmentActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    //public class MainActivity extends FragmentActivity implements LocationListener {
    BottomNavigationView bottomNavigationView;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



    }



    ImpactFragment firstFragment = new ImpactFragment();
    LitterMapFragment secondFragment = new LitterMapFragment();
    HomeFragment thirdFragment = new HomeFragment();


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_impact:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, firstFragment).commit();

                return true;

            case R.id.nav_litterMap:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, secondFragment).commit();
                return true;

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, thirdFragment).commit();
                return true;

           /* case R.id.nav_challenges:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fourthFragment).commit();
                return true;

            case R.id.nav_connect:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fifthFragment).commit();
                return true;*/
        }
        return false;
    }



}