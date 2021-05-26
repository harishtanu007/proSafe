package com.harish.prosafe.ui.incidentcategory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.harish.prosafe.R;
import com.harish.prosafe.data.adapters.IncidentCategoryAdapter;
import com.harish.prosafe.data.adapters.IncidentCategoryValueChangeListener;
import com.harish.prosafe.util.IBackendProvider;

public class IncidentCategoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    IBackendProvider backendProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_category);
        getSupportActionBar().setTitle("Choose a Category");
        backendProvider = IBackendProvider.getBackendProvider();
        recyclerView = findViewById(R.id.categories_rv);
        recyclerView.setHasFixedSize(true);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getApplicationContext()));

        backendProvider.addCategoryValueEventListener(new IncidentCategoryValueChangeListener() {
            @Override
            public void onSuccess(IncidentCategoryAdapter adapter) {
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailed() {
                Toast.makeText(getApplicationContext(),getString(R.string.incident_category_fetch_error_message), Toast.LENGTH_SHORT).show();
            }
        });
    }
}