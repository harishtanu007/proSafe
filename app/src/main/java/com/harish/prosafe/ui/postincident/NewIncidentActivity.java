package com.harish.prosafe.ui.postincident;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.harish.prosafe.MainActivity;
import com.harish.prosafe.R;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.ui.location.LocationActivity;
import com.harish.prosafe.ui.login.EventListener;
import com.harish.prosafe.util.Constants;
import com.harish.prosafe.util.IBackendProvider;

public class NewIncidentActivity extends AppCompatActivity {
    EditText incidentTitle, incidentDescription;
    Button shareIncident;
    IBackendProvider backendProvider;
    ConstraintLayout rootLayout;
    LinearLayout locationView;
    TextView location;
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
                Incident incident = new Incident(title, description, user,incidentCategory,unixTime);
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
        locationView.setOnClickListener(v-> {
                startActivityForResult(new Intent(getApplicationContext(), LocationActivity.class), Constants.LOCATION_ACTIVITY);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.LOCATION_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                String address=data.getStringExtra(Constants.LOCATION_ADDRESS);
                location.setText(address);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}