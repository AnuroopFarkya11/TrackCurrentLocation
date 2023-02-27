package com.example.location;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    Button get_location_btn;
    TextView lati_view, longi_view;

    String lati,longi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // binding button
        lati_view = findViewById(R.id.latitude_textview);
        longi_view = findViewById(R.id.longitude_textview);
        get_location_btn = findViewById(R.id.get_location);




        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }



        get_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


                // if gps is not active

                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    Toast.makeText(getApplicationContext(),"Location is on!",Toast.LENGTH_LONG).show();
                    // gps is active

                    getCurrentLocation();
                }
                else
                {
                    // gps is not on hence take permission



                    turnOnGPS();




                }

            }
        });


    }

    private void getCurrentLocation() {

        // checking the permission

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
//            ActivityCompat.requestPermissions(this,new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION
//            },REQUEST_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);

        }
        else
        {
            Location LocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


            if(LocationGPS!=null)
            {
                double lat = LocationGPS.getLatitude();
                double lon = LocationGPS.getLongitude();


                lati = String.valueOf(lat);
                longi = String.valueOf(lon);

                lati_view.setText(lati);
                longi_view.setText(longi);
//                Log.d("Location Update","Latitude: "+lat+"\nLongitude: "+ lon);

            }
            else if(LocationNetwork!=null)
            {
                double lat = LocationNetwork.getLatitude();
                double lon = LocationNetwork.getLongitude();


                lati = String.valueOf(lat);
                longi = String.valueOf(lon);

                lati_view.setText(lati);
                longi_view.setText(longi);

                Log.d("Location Update","Latitude: "+lat+"\nLongitude: "+ lon);

            }
            else{
                Toast.makeText(getApplicationContext(),"Sorry",Toast.LENGTH_LONG).show();
            }

        }


    }

    private void turnOnGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("ENABLE GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));


            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });


        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}