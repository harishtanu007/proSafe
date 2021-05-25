package com.harish.prosafe.ui.myposts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.harish.prosafe.R;
import com.harish.prosafe.data.adapters.IncidentAdapter;
import com.harish.prosafe.data.adapters.IncidentValueChangeListener;
import com.harish.prosafe.ui.login.LoginActivity;
import com.harish.prosafe.util.IBackendProvider;

public class MyPostsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    IBackendProvider backendProvider;

    private static final int LOGIN_ACTIVITY_REQUEST_CODE = 845;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        backendProvider = IBackendProvider.getBackendProvider();
        recyclerView = findViewById(R.id.incidents_rv);
        recyclerView.setHasFixedSize(true);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        backendProvider.addMyIncidentValueEventListener(new IncidentValueChangeListener() {
            @Override
            public void onSuccess(IncidentAdapter adapter) {
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (backendProvider.getCurrentUser() == null) {
            openLoginPage();
        }
    }

    private void openLoginPage() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE);
    }
}