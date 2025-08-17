package com.example.mealmate.geotag;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mealmate.R;
import com.example.mealmate.databinding.ActivityGeoTagBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class ActivityGeoTag extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityGeoTagBinding binding;
    private GoogleMap googleMap;
    private static final int FINE_PERMISSION_CODE = 1;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<String> ingredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGeoTagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the ingredients list from the intent
        ingredients = getIntent().getStringArrayListExtra("ingredients");
        if (ingredients != null && !ingredients.isEmpty()) {
            Toast.makeText(this, "Ingredients: " + ingredients, Toast.LENGTH_LONG).show();
        } else {
            ingredients = new ArrayList<>();
            Toast.makeText(this, "No ingredients found.", Toast.LENGTH_SHORT).show();
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Check and request location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
        } else {
            getLastLocation();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void getLastLocation() {
        // Ensure permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission is not granted.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the last known location
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                Log.d("GeoTag", "Location retrieved: " + location);
            } else {
                Log.w("GeoTag", "Current location is null.");
                Toast.makeText(ActivityGeoTag.this, "Unable to get current location.", Toast.LENGTH_SHORT).show();
            }

            // Initialize the map
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(ActivityGeoTag.this);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (currentLocation != null) {
            LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(myLocation).title("Your Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));
            displayIngredientsOnMap(ingredients, myLocation);
        } else {
            LatLng defaultLocation = new LatLng(16.871311, 96.199379);
            googleMap.addMarker(new MarkerOptions().position(defaultLocation).title("Your Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 13));
            displayIngredientsOnMap(ingredients, defaultLocation);
        }
    }

    private void displayIngredientsOnMap(List<String> ingredients, LatLng baseLocation) {
        if (ingredients == null || ingredients.isEmpty()) {
            Toast.makeText(this, "No ingredients to display on the map.", Toast.LENGTH_SHORT).show();
            return;
        }

        double radius = 0.01; // Radius of the circle in degrees (adjust as needed)
        int numMarkers = ingredients.size();
        double angleIncrement = (2 * Math.PI) / numMarkers; // Equal angle between markers

        for (int i = 0; i < numMarkers; i++) {
            double angle = i * angleIncrement; // Current angle for this marker

            // Convert polar coordinates (angle, radius) to Cartesian offsets
            double latOffset = radius * Math.cos(angle);
            double lngOffset = radius * Math.sin(angle);

            // Calculate the marker's position
            LatLng markerLocation = new LatLng(baseLocation.latitude + latOffset, baseLocation.longitude + lngOffset);

            // Add the marker to the map
            googleMap.addMarker(new MarkerOptions()
                    .position(markerLocation)
                    .title("Store with " + ingredients.get(i)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission is denied. Please grant permission to use this feature.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
