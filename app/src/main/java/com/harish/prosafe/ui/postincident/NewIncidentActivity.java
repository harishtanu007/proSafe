package com.harish.prosafe.ui.postincident;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.harish.prosafe.R;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.ui.login.EventListener;
import com.harish.prosafe.util.FirebaseProvider;
import com.harish.prosafe.util.IBackendProvider;

public class NewIncidentActivity extends AppCompatActivity {
    EditText incidentTitle, incidentDescription;
    Button shareIncident;
    IBackendProvider backendProvider;
    ConstraintLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_incident);
        incidentTitle = findViewById(R.id.incident_title);
        incidentDescription = findViewById(R.id.incident_description);
        shareIncident = findViewById(R.id.share_incident);
        rootLayout = findViewById(R.id.rootlayout);
        backendProvider = IBackendProvider.getBackendProvider();
        String incidentCategory = getIntent().getStringExtra("INCIDENT_CATEGORY");
        shareIncident.setOnClickListener(v -> {
            String title = incidentTitle.getText().toString().trim();
            String description = incidentDescription.getText().toString().trim();
            if (!title.isEmpty() && !description.isEmpty()) {
                Log.e("title : ", title);
                Log.e("description : ", description);
                String user = backendProvider.getUserName();
                Incident incident = new Incident(title, description, user,incidentCategory);
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
    }
}