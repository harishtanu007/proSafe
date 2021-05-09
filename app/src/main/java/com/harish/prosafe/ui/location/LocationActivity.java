package com.harish.prosafe.ui.location;

import androidx.annotation.NonNull;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.harish.prosafe.R;
import com.harish.prosafe.util.Constants;

public class LocationActivity extends AppCompatActivity {
    LinearLayout useCurrentLocation;
    private static final int REQUEST_LOCATION = 1;
    Button btnGetLocation;
    TextView showLocation;
    LocationManager locationManager;
    String latitude, longitude;
    private ResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        useCurrentLocation = findViewById(R.id.use_current_location);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        resultReceiver = new AddressResultReceiver(new Handler());
        useCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCurrentLocation();
            }
        });

    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            LocationServices.getFusedLocationProviderClient(LocationActivity.this)
                    .requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(getApplicationContext()).removeLocationUpdates(this);
                            if (locationResult != null && locationResult.getLocations().size() > 0) {
                                int latestLocationIndex = locationResult.getLocations().size() - 1;
                                double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                                Toast.makeText(getApplicationContext(), "Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude, Toast.LENGTH_SHORT).show();
                                Location location = new Location("providerNA");
                                location.setLatitude(latitude);
                                location.setLongitude(longitude);
                                fetchAddressfromLatLong(location);
                            }else {

                            }

                        }
                    }, Looper.getMainLooper());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchAddressfromLatLong(Location location){
        Intent intent = new Intent(this,FetchAddressService.class);
        intent.putExtra(Constants.RECEIVER,resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA,location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {

        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.RESULT_SUCCESS) {
                Toast.makeText(getApplicationContext(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }
}