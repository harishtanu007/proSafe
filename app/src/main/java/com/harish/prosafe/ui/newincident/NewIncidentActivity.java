package com.harish.prosafe.ui.newincident;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.harish.prosafe.R;
import com.harish.prosafe.data.adapters.AddressValueChangeListener;
import com.harish.prosafe.data.model.Coordinates;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.ui.incidentlocation.LocationActivity;
import com.harish.prosafe.ui.login.EventListener;
import com.harish.prosafe.util.IBackendProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.harish.prosafe.util.Constants.*;

public class NewIncidentActivity extends AppCompatActivity {
    EditText incidentTitle, incidentDescription;
    Button shareIncident;
    IBackendProvider backendProvider;
    ScrollView rootLayout;
    LinearLayout locationView;
    TextView locationText;
    double latitude, longitude;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_incident);
        incidentTitle = findViewById(R.id.incident_title);
        incidentDescription = findViewById(R.id.incident_description);
        locationText = findViewById(R.id.location);
        shareIncident = findViewById(R.id.share_incident);
        rootLayout = findViewById(R.id.rootlayout);
        locationView = findViewById(R.id.location_view);

        backendProvider = IBackendProvider.getBackendProvider();
        String incidentCategory = getIntent().getStringExtra(getString(R.string.incident_category));
        getSupportActionBar().setTitle(incidentCategory);

        backendProvider.getAddressValueEventListener(new AddressValueChangeListener() {
            @Override
            public void onSuccess(Coordinates coordinates) {
                Location location = new Location("providerNA");
                location.setLatitude(coordinates.getLatitude());
                location.setLongitude(coordinates.getLongitude());
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                }catch (Exception e){
                    String errorMessage = e.getMessage();
                    Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                if(addresses==null || addresses.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Unable to get current location",Toast.LENGTH_SHORT).show();
                }else {
                    Address address = addresses.get(0);
                    ArrayList<String> addressFragments = new ArrayList<>();
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        addressFragments.add(address.getAddressLine(i));
                    }
                    locationText.setText(TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")),addressFragments));
                }

            }

            @Override
            public void onFailed() {

            }
        });

        shareIncident.setOnClickListener(v -> {
            String title = incidentTitle.getText().toString().trim();
            String description = incidentDescription.getText().toString().trim();
            if (!title.isEmpty() && !description.isEmpty()) {
                Log.e("title : ", title);
                Log.e("description : ", description);
                String userName = backendProvider.getUserName();
                long unixTime = System.currentTimeMillis() / 1000L;
                Incident incident = new Incident(title, description, userName, incidentCategory, unixTime, address, new Coordinates(latitude, longitude));
                backendProvider.addIncident(incident).setEventListener(new EventListener() {
                    @Override
                    public void onSuccess() {
                        Snackbar.make(rootLayout, "Incident Shared Successfully", Snackbar.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailed() {
                        Snackbar.make(rootLayout, "Something went wrong!", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
        locationView.setOnClickListener(v -> startActivityForResult(new Intent(getApplicationContext(), LocationActivity.class), LOCATION_ACTIVITY));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                address = data.getStringExtra(LOCATION_ADDRESS);
                latitude = data.getDoubleExtra(LOCATION_LATITUDE, 0);
                longitude = data.getDoubleExtra(LOCATION_LONGITUDE, 0);
                locationText.setText(address);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
    }
}