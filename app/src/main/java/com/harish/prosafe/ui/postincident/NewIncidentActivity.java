package com.harish.prosafe.ui.postincident;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.harish.prosafe.R;
import com.harish.prosafe.data.model.Incident;

public class NewIncidentActivity extends AppCompatActivity {
    EditText incidentTitle,incidentDescription;
    Button shareIncident;
    DatabaseReference mbase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_incident);
        incidentTitle = findViewById(R.id.incident_title);
        incidentDescription = findViewById(R.id.incident_description);
        shareIncident = findViewById(R.id.share_incident);

        mbase = FirebaseDatabase.getInstance().getReference("Incidents");

        shareIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = incidentTitle.getText().toString().trim();
                String description = incidentDescription.getText().toString().trim();
                if(!title.isEmpty() && !description.isEmpty())
                {
                    Log.e("title : ",title);
                    Log.e("description : ",description);
                    String incidentId = mbase.push().getKey();
                    Incident incident = new Incident(title,description,"testing");
                    mbase.child(incidentId).setValue(incident);
                }
            }
        });
    }
}