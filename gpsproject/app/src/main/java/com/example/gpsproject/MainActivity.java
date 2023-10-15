package com.example.gpsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener{
    double latitude;
    double longitude;
    double distance = 0.0;
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    TextView t5;
    Button reset;
    final int LOCATION_PERMISSION_REQUEST_CODE = 0;
    List<Address> address = new ArrayList<>();
    List<String> locations = new ArrayList<>();
    List<String> times = new ArrayList<>();
    Location location1 = null;
    Location location2 = null;
    long totalTime;
    long newTime;
    int longestTime;
    String favoriteLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = findViewById(R.id.id_favorite);
        t2 = findViewById(R.id.id_coordinate);
        t3 = findViewById(R.id.id_address);
        t4 = findViewById(R.id.id_distance);
        t5 = findViewById(R.id.id_name);
        reset = findViewById(R.id.id_reset);
        LocationManager locManager = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if(Build.VERSION.SDK_INT >= 23)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
            else
            {
                //start to find location
            }
        }
        else
        {

        }
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,5,this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // start to find location...

            } else { // if permission is not granted

                ActivityCompat.requestPermissions(this,permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onFlushComplete(int requestCode) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try
        {
            newTime = SystemClock.elapsedRealtime() - totalTime;
            times.add(String.valueOf(newTime));

            location1 = location;
            if(location2 == null)
                location2 = location1;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            location1.setLatitude(latitude);
            location1.setLongitude(longitude);
            distance += location1.distanceTo(location2);
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    distance = 0.0;
                    t4.setText("Distance: \n" + distance + " m");
                }
            });
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.US);
            address = geocoder.getFromLocation(latitude, longitude, 1);
            String addressLine = address.get(0).getAddressLine(0);
            locations.add(addressLine);
            t2.setText(("("+ longitude + " °W, " + latitude + " °N)"));
            t3.setText("Address: \n" + addressLine);
            t4.setText("Distance: \n" + distance + " m");
            t5.setText("Rivan Parikh");
            totalTime = SystemClock.elapsedRealtime();
            times.set(0, "0");
            for(int i = 1; i < times.size();i++)
            {
                if(Integer.parseInt(times.get(i)) > Integer.parseInt(times.get(longestTime)))
                {
                    longestTime = i;
                    if(times.size() > 1)
                    {
                        favoriteLocation = locations.get(longestTime-1);
                    }
                    else
                    {
                        favoriteLocation = locations.get(0);
                    }
                    t1.setText("Favorite Location: \n" + favoriteLocation + "\n" + Integer.parseInt(times.get(longestTime))/1000);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("error", "catch");
            address = null;
        }
    }
}