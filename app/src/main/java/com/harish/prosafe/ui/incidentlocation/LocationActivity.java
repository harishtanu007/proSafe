package com.harish.prosafe.ui.incidentlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.harish.prosafe.R;
import com.harish.prosafe.util.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class LocationActivity extends AppCompatActivity {
    CardView useCurrentLocation;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    private ResultReceiver resultReceiver;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        useCurrentLocation = findViewById(R.id.use_current_location);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        resultReceiver = new AddressResultReceiver(new Handler());
        useCurrentLocation.setOnClickListener(v -> getCurrentLocation());

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_places_api_key));
        }

// Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                Toast.makeText(getApplicationContext(),place.getName(),Toast.LENGTH_SHORT).show();
                String address = place.getAddress();
                LatLng location = place.getLatLng();
                double latitude = location.latitude;
                double longitude = location.longitude;
                Toast.makeText(getApplicationContext(),location.latitude+""+location.longitude,Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constants.LOCATION_ADDRESS,address);
                returnIntent.putExtra(Constants.LOCATION_LATITUDE,latitude);
                returnIntent.putExtra(Constants.LOCATION_LONGITUDE,longitude);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }

            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Toast.makeText(getApplicationContext(),"An error occurred: " + status,Toast.LENGTH_SHORT).show();

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
                                latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                                Toast.makeText(getApplicationContext(), "Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude, Toast.LENGTH_SHORT).show();
                                Location location = new Location("providerNA");
                                location.setLatitude(latitude);
                                location.setLongitude(longitude);
                                fetchAddressfromLatLong(location);
                            } else {
                                Toast.makeText(getApplicationContext(), "Cannot fetch the location", Toast.LENGTH_SHORT).show();
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

    private void fetchAddressfromLatLong(Location location) {
        Intent intent = new Intent(this, FetchAddressService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
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
                String address = resultData.getString(Constants.RESULT_DATA_KEY);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constants.LOCATION_ADDRESS,address);
                returnIntent.putExtra(Constants.LOCATION_LATITUDE,latitude);
                returnIntent.putExtra(Constants.LOCATION_LONGITUDE,longitude);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }
}