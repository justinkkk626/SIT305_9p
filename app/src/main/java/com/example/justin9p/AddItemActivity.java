package com.example.justin9p;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;

public class AddItemActivity extends AppCompatActivity {

    EditText etTitle, etDesc, etDate, etLocation, etContact;
    Button btnSave, btnCurrentLocation;

    DatabaseHelper db;
    FusedLocationProviderClient fusedLocationClient;

    private static final int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        etContact = findViewById(R.id.etContact);
        btnSave = findViewById(R.id.btnSave);
        btnCurrentLocation = findViewById(R.id.btnCurrentLocation);

        db = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "YOUR_GOOGLE_API_KEY_HERE");
        }

        etLocation.setOnClickListener(view -> {
            Intent intent = new Intent(AddItemActivity.this, PlacePickerActivity.class);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        });

        btnCurrentLocation.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
                return;
            }

            Task<Location> locationTask = fusedLocationClient.getLastLocation();
            locationTask.addOnSuccessListener(location -> {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    etLocation.setText(lat + ", " + lng);
                } else {
                    Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnSave.setOnClickListener(v -> {
            db.insertItem(
                    etTitle.getText().toString(),
                    etDesc.getText().toString(),
                    etDate.getText().toString(),
                    etLocation.getText().toString(),
                    etContact.getText().toString()
            );
            Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddItemActivity.this, MainActivity.class));
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK && data != null) {
            String selectedAddress = data.getStringExtra("address");
            etLocation.setText(selectedAddress);
        }
    }
}
