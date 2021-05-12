package com.harish.prosafe.ui.newincident;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.harish.prosafe.R;
import com.harish.prosafe.data.model.Coordinates;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.ui.incidentlocation.LocationActivity;
import com.harish.prosafe.ui.login.EventListener;
import com.harish.prosafe.util.IBackendProvider;

import static com.harish.prosafe.util.Constants.*;

public class NewIncidentActivity extends AppCompatActivity {
    EditText incidentTitle, incidentDescription;
    Button shareIncident;
    IBackendProvider backendProvider;
    ConstraintLayout rootLayout;
    LinearLayout locationView;
    TextView location;
    double latitude,longitude;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_incident);
        incidentTitle = findViewById(R.id.incident_title);
        incidentDescription = findViewById(R.id.incident_description);
        location = findViewById(R.id.location);
        shareIncident = findViewById(R.id.share_incident);
        rootLayout = findViewById(R.id.rootlayout);
        locationView = findViewById(R.id.location_view);
        backendProvider = IBackendProvider.getBackendProvider();
        String incidentCategory = getIntent().getStringExtra("INCIDENT_CATEGORY");
        shareIncident.setOnClickListener(v -> {
            String title = incidentTitle.getText().toString().trim();
            String description = incidentDescription.getText().toString().trim();
            if (!title.isEmpty() && !description.isEmpty()) {
                Log.e("title : ", title);
                Log.e("description : ", description);
                String user = backendProvider.getUserName();
                long unixTime = System.currentTimeMillis() / 1000L;
                Incident incident = new Incident(title, description, user,incidentCategory,unixTime,address,new Coordinates(latitude,longitude));
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
        locationView.setOnClickListener(v-> startActivityForResult(new Intent(getApplicationContext(), LocationActivity.class), LOCATION_ACTIVITY));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                address=data.getStringExtra(LOCATION_ADDRESS);
                latitude=data.getDoubleExtra(LOCATION_LATITUDE,0);
                longitude=data.getDoubleExtra(LOCATION_LONGITUDE,0);
                location.setText(address);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
    }
}