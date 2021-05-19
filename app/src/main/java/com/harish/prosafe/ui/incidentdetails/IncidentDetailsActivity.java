package com.harish.prosafe.ui.incidentdetails;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.harish.prosafe.R;
import com.harish.prosafe.data.adapters.AddressValueChangeListener;
import com.harish.prosafe.data.model.Coordinates;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.util.Constants;
import com.harish.prosafe.util.Helper;
import com.harish.prosafe.util.IBackendProvider;

import java.text.DecimalFormat;

public class IncidentDetailsActivity extends AppCompatActivity{

    private TextView title, description, postedBy, time, category, distance;
    private GoogleMap mMap;
    IBackendProvider backendProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String incidentCategory = "", incidentPostedBy = "", incidentDescription = "", incidentTitle = "";
        long incidentTime = 0;
        setContentView(R.layout.activity_incident_details);
        backendProvider =IBackendProvider.getBackendProvider();
        Incident incident = (Incident) getIntent().getSerializableExtra(Constants.INCIDENT_DATA_EXTRA);
        incidentCategory = incident.getIncidentCategory();
        incidentPostedBy = incident.getPostedBy();
        incidentDescription = incident.getDescription();
        incidentTitle = incident.getTitle();
        incidentTime = incident.getPostTime();

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        postedBy = findViewById(R.id.posted_by);
        category = findViewById(R.id.incident_category);
        time = findViewById(R.id.time);
        distance = findViewById(R.id.incident_distance);

        title.setText(incidentTitle);
        description.setText(incidentDescription);
        postedBy.setText(incidentPostedBy);
        category.setText(incidentCategory);
        time.setText(Helper.getTimeAgo(incidentTime));
        backendProvider.getAddressValueEventListener(new AddressValueChangeListener() {
            @Override
            public void onSuccess(Coordinates userCurrentCoordinates) {
                DecimalFormat df = new DecimalFormat("###.#");
                distance.setText(df.format(Helper.distance(incident.getCoordinates(),userCurrentCoordinates))+Helper.getDistanceUnit());
            }
            @Override
            public void onFailed() {
                Toast.makeText(getApplicationContext(), "Error while adding the location", Toast.LENGTH_SHORT).show();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            LatLng incidentLocation = new LatLng(incident.getCoordinates().getLatitude(), incident.getCoordinates().getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(incidentLocation)
                    .title("Marker in Sydney"));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(incidentLocation)      // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(incidentLocation));
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        });

    }

}