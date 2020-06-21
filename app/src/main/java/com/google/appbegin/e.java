package com.google.appbegin;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class e extends AppCompatActivity  implements OnMapReadyCallback{

    private GoogleMap mMap;
    TextView t;
    private FusedLocationProviderClient client;
    double x =10.8460838, y =106.8149293;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        t = findViewById(R.id.t);
        res();
        client = LocationServices.getFusedLocationProviderClient(this);
        t = findViewById(R.id.t);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.getLastLocation().addOnSuccessListener(e.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            t.setText(location.getLatitude()+"-"+location.getLongitude()+"");
                            x = location.getLatitude() ;
                            y = location.getLongitude() ;
                            Log.d("122",x +" "+y);
                        }
                    }
                });
            }
        });
    }

    private void res() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(  x,106.8149293);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
