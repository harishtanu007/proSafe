package com.harish.prosafe.ui.incidentdetails;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.harish.prosafe.R;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.util.Constants;
import com.harish.prosafe.util.Helper;

public class IncidentDetailsActivity extends AppCompatActivity {

    private TextView title, description, postedBy, time, category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String incidentCategory = "", incidentPostedBy = "", incidentDescription = "", incidentTitle = "";
        long incidentTime = 0;
        setContentView(R.layout.activity_incident_details);
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

        title.setText(incidentTitle);
        description.setText(incidentDescription);
        postedBy.setText(incidentPostedBy);
        category.setText(incidentCategory);
        time.setText(Helper.getTimeAgo(incidentTime));

    }
}