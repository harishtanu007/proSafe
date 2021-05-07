package com.harish.prosafe.ui.incidentdetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.TextView;

import com.harish.prosafe.R;
import com.harish.prosafe.util.Helper;

public class IncidentDetailsActivity extends AppCompatActivity {

    private TextView title, description, postedBy, time, category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_details);
        String incidentCategory = getIntent().getStringExtra("INCIDENT_CATEGORY");
        String incidentPostedBy = getIntent().getStringExtra("INCIDENT_POSTED_BY");
        String incidentDescription = getIntent().getStringExtra("INCIDENT_DESCRIPTION");
        String incidentTitle = getIntent().getStringExtra("INCIDENT_TITLE");
        long incidentTime = getIntent().getLongExtra("INCIDENT_TIME",0);

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