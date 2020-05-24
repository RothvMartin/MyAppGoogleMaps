package com.example.myappgooglemaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button buttonLoadCoordinates;
    Button buttonMapView;

    TextView textViewLatView;
    TextView textViewLonView;

    Double currentLat;
    Double currentLon;

    FusedLocationProviderClient client;

    boolean clientPermissions = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLoadCoordinates = findViewById(R.id.buttonLoadCoordinates);
        buttonMapView = findViewById(R.id.buttonShowMap);
        textViewLatView = findViewById(R.id.textViewLatView);
        textViewLonView = findViewById(R.id.textViewLonView);

        client = LocationServices.getFusedLocationProviderClient(this);

        buttonMapView.setEnabled(false);

        buttonLoadCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCoordinates();
            }
        });


        buttonMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("Latitude", currentLat);
                intent.putExtra("Longitude", currentLon);
                startActivity(intent);
            }
        });








    }



    public void loadCoordinates(){

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            clientPermissions = true;
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 901);
        }


        if(clientPermissions){
            final Task<Location> location = client.getLastLocation();

            location.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if(task.isSuccessful()){
                        textViewLatView.setText(String.valueOf(task.getResult().getLatitude()));
                        currentLat = task.getResult().getLatitude();
                        textViewLonView.setText(String.valueOf(task.getResult().getLongitude()));
                        currentLon = task.getResult().getLongitude();

                        buttonMapView.setEnabled(true);

                    }
                }
            });
        }

        else{
            Toast.makeText(this, "Error: App requires permission to use location", Toast.LENGTH_SHORT).show();
        }

    }

}
