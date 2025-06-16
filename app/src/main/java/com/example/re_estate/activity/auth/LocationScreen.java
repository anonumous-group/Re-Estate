package com.example.re_estate.activity.auth;

import static com.example.re_estate.misc.FirebaseUtil.userDoc;
import static com.example.re_estate.misc.FirebaseUtil.userId;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.re_estate.R;
import com.example.re_estate.activity.MainScreen;
import com.example.re_estate.databinding.ActivityLocationScreenBinding;
import com.example.re_estate.misc.Utilities;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.GeoPoint;

public class LocationScreen extends AppCompatActivity {
    ActivityLocationScreenBinding binding;
    GeoPoint geoPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.light(
                getColor(R.color.white), getColor(R.color.white)
        ));
        binding = ActivityLocationScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.main);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(32, systemBars.top, 32, 32);
            return insets;
        });

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        binding.btnLocation.setOnClickListener(v -> {
            if (Utilities.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, this)) {
                client.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        userDoc(userId()).update("geoPoint", geoPoint).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                startActivity(new Intent(this, MainScreen.class));
                                finish();
                            } else {
                                Utilities.sendMessage(this, "Something went wrong");
                                Log.e("LocationScreen", "Error updating geoPoint: " + task.getException());
                            }
                        });
                    } else {
                        Utilities.sendMessage(this, "Something went wrong");
                        Log.e("LocationScreen", "Error getting location");
                    }
                });
            }
            else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            binding.btnLocation.performClick();
        } else {
            Utilities.sendMessage(this, "Location permission denied");
        }
    }
}