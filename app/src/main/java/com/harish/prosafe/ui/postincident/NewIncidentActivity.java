package com.harish.prosafe.ui.postincident;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.harish.prosafe.R;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.util.FirebaseProvider;
import com.harish.prosafe.util.IBackendProvider;

public class NewIncidentActivity extends AppCompatActivity {
    EditText incidentTitle,incidentDescription;
    Button shareIncident;
    IBackendProvider backendProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_incident);
        incidentTitle = findViewById(R.id.incident_title);
        incidentDescription = findViewById(R.id.incident_description);
        shareIncident = findViewById(R.id.share_incident);

        backendProvider = FirebaseProvider.getFirebaseProvider();


        shareIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = incidentTitle.getText().toString().trim();
                String description = incidentDescription.getText().toString().trim();
                if(!title.isEmpty() && !description.isEmpty())
                {
                    Log.e("title : ",title);
                    Log.e("description : ",description);
                    Incident incident = new Incident(title,description,"testing");
                    backendProvider.addIncident(incident);
                }
            }
        });
    }
}