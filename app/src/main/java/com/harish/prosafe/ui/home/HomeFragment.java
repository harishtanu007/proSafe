package com.harish.prosafe.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.Toast;

import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.harish.prosafe.R;
import com.harish.prosafe.data.adapters.IncidentAdapter;
import com.harish.prosafe.data.adapters.IncidentValueChangeListener;
import com.harish.prosafe.ui.customincidents.CustomIncidentsActivity;
import com.harish.prosafe.ui.login.LoginActivity;
import com.harish.prosafe.ui.registration.RegistrationActivity;
import com.harish.prosafe.util.IBackendProvider;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    IBackendProvider backendProvider;
    ImageView customizeIncidents;
    LinearLayout noIncidentsLayout;

    private static final int LOGIN_ACTIVITY_REQUEST_CODE = 844;
    private static final String TAG = HomeFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        backendProvider = IBackendProvider.getBackendProvider();
        recyclerView = root.findViewById(R.id.incidents_rv);
        recyclerView.setHasFixedSize(true);

        customizeIncidents = root.findViewById(R.id.customize_incidents);
        noIncidentsLayout = root.findViewById(R.id.no_incidents_layout);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));
        if(backendProvider!=null && backendProvider.getCurrentUser()!=null) {
            backendProvider.addIncidentValueEventListener(new IncidentValueChangeListener() {
                @Override
                public void onSuccess(IncidentAdapter adapter) {
                    Log.e(TAG,  "Item count" + adapter.getItemCount());
                    if(adapter.getItemCount()>0){

                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        noIncidentsLayout.setVisibility(View.GONE);
                    }
                    else {
                        noIncidentsLayout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailed() {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.incident_fetch_error_message), Toast.LENGTH_SHORT).show();
                }
            });
        }else
        {
            openLoginPage();
        }

        customizeIncidents.setOnClickListener(v -> startActivity(new Intent(getContext(), CustomIncidentsActivity.class)));
        return root;
    }

    @Override
    public void onStart() {
        // Check if user is signed in (non-null) and update UI accordingly.
        super.onStart();
        if (backendProvider.getCurrentUser() == null) {
            openLoginPage();
        }
    }

    private void openLoginPage() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE);
    }


}