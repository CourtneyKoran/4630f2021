package com.example.litternavbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import com.firebase.geofire.core.GeoHash;
//import com.firebase.geofire.util.GeoUtils;
import com.firebase.geofire.GeoFireUtils;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LitterMapFragment extends Fragment {

    private GoogleMap mMap;


    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    double currentLatitude = 41.318480;
    double currentLongitude = -19.794428;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DocumentReference userData = db.collection(user.getUid()).document("Po1xF78Hb5jrYUjSMiE8");

    DocumentReference litterData = db.collection("litterLocations").document("Po1xF78Hb5jrYUjSMiE8");
    DocumentReference litterBugUsers = db.collection("litterBugUsers").document("Po1xF78Hb5jrYUjSMiE8");

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("path/to/geofire");


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_litter, container, false);

        //db = FirebaseDatabase.getInstance();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.reportMsg)
                                .setTitle(R.string.reportTitle);

                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked Yes button
                                Log.w("Click", "User Clicked Yes");

                                String geoHash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(latLng.latitude, latLng.longitude));

                                Map<String, Object> locationRec = new HashMap<>();
                                locationRec.put("geohash", geoHash);
                                locationRec.put("lat", latLng.latitude);
                                locationRec.put("lon", latLng.longitude);
                                db.collection("litterLocations").document(geoHash).set(locationRec);

                                // add a marker to the map for the new litter
                                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));

                                // below line is use to move camera.
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                            }
                        });
                        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked No button
                                Log.w("Click", "User Clicked No");
                            }
                        });

                        AlertDialog dialog = builder.show();

                    }
                });

                Marker marker_1;

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        LatLng position = marker.getPosition();
                        Log.w("Click", "latitude: "+position.latitude+ " longitude: "+ position.longitude);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.pickupMsg)
                                .setTitle(R.string.pickupTitle);

                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked Yes button
                                Log.w("Click", "User Clicked Yes");
                                updateDatabaseInfo(position.latitude, position.longitude);
                                marker.remove();
                            }
                        });
                        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked No button
                                Log.w("Click", "User Clicked No");
                            }
                        });

                        AlertDialog dialog = builder.show();




                            return true;

                    }
                });
                displayLitter();
                getLastLocation();

                ExtendedFloatingActionButton fab = view.findViewById(R.id.litterButton);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recordLastLocation();
                        Snackbar.make(view, "Litter Recorded", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

            }
        });



        return view;

    }

    private void displayLitter() {
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        Query q = db.collection("litterLocations")
                .orderBy("geohash");
        tasks.add(q.get());
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            for (DocumentSnapshot doc : snap.getDocuments()) {
                                double tempLat = doc.getDouble("lat");
                                double tempLon= doc.getDouble("lon");
                                LatLng tempLocation = new LatLng(tempLat, tempLon);
                                // adding marker to each location on google maps
                                mMap.addMarker(new MarkerOptions().position(tempLocation).title("Marker"));
                            }
                        }
                    }
                });
    }

    private void updateDatabaseInfo(Double latitude, Double longitude) {
        String geoHash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(latitude, longitude));

        Map<String, Object> locationRec = new HashMap<>();
        locationRec.put("geohash", geoHash);
        locationRec.put("lat", latitude);
        locationRec.put("lon", longitude);
        db.collection(user.getUid()).document(geoHash).set(locationRec);
        db.collection("litterLocations").document(geoHash).delete();

        DocumentReference docRef = db.collection("litterBugUsers").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("LitterMap DB-Get", "DocumentSnapshot data: " + document.getData());
                        Long litterCount = document.getLong("litterCount");

                        Map<String, Object> litterCountRec = new HashMap<>();
                        litterCountRec.put("litterCount", litterCount+1);
                        db.collection("litterBugUsers").document(user.getUid()).set(litterCountRec);
                    } else {
                        Log.d("LitterMap DB-Get", "No such document");
                    }
                } else {
                    Log.d("LitterMap DB-Get", "get failed with ", task.getException());
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {


//This code does not work as expected. Was trying to get location updates.
/*                SettingsClient mSettingsClient;
                LocationRequest mLocationRequest;
                LocationSettingsRequest mLocationSettingsRequest;
                LocationCallback mLocationCallback;

                mSettingsClient = LocationServices.getSettingsClient(this.getActivity());

                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        // location is received
                        Location mCurrentLocation = locationResult.getLastLocation();
                        Log.d("LitterMap", "Location Callback");

                    }
                };

                //mRequestingLocationUpdates = false;

                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(180000);
                mLocationRequest.setFastestInterval(60000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback,
                        Looper.getMainLooper());*/

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            currentLatitude= location.getLatitude();
                            currentLongitude= location.getLongitude();

                            LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);

                            // Move Camera to current location, but don't add marker
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));


                        }
                    }
                });
            } else {
                Toast.makeText(this.getActivity(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void recordLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            currentLatitude= location.getLatitude();
                            currentLongitude= location.getLongitude();

                            String geoHash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(currentLatitude, currentLongitude));

                            Map<String, Object> locationRec = new HashMap<>();
                            locationRec.put("geohash", geoHash);
                            locationRec.put("lat", currentLatitude);
                            locationRec.put("lon", currentLongitude);
                            db.collection("litterLocations").document(geoHash).set(locationRec);

                            LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);

                            // add a marker to the map for the new litter
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker"));

                            // below line is use to move camera.
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

                        }
                    }
                });
            } else {
                Toast.makeText(this.getActivity(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Snackbar.make(getView(), "Sign Out Complete", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            //      latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            //    longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("LitterMap OnResume", "Made it");
        if (checkPermissions()) {
            getLastLocation();
        }
    }


}
