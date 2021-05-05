package com.harish.prosafe.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harish.prosafe.R;
import com.harish.prosafe.data.adapters.IncidentAdapter;
import com.harish.prosafe.data.adapters.IncidentValueChangeListener;
import com.harish.prosafe.ui.login.LoginActivity;
import com.harish.prosafe.util.IBackendProvider;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    IBackendProvider backendProvider;


    private static final int LOGIN_ACTIVITY_REQUEST_CODE = 844;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        backendProvider = IBackendProvider.getBackendProvider();
        recyclerView = root.findViewById(R.id.incidents_rv);
        recyclerView.setHasFixedSize(true);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        backendProvider.addIncidentValueEventListener(new IncidentValueChangeListener() {
            @Override
            public void onSuccess(IncidentAdapter adapter) {
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {

            }
        });
        return root;
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
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check that it is the SecondActivity with an OK result
        if (requestCode == LOGIN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

            }
        }
    }
}