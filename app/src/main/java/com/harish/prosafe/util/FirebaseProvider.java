package com.harish.prosafe.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.harish.prosafe.data.adapters.AddressValueChangeListener;
import com.harish.prosafe.data.adapters.IncidentAdapter;
import com.harish.prosafe.data.adapters.IncidentCategoryAdapter;
import com.harish.prosafe.data.adapters.IncidentCategoryValueChangeListener;
import com.harish.prosafe.data.adapters.IncidentValueChangeListener;
import com.harish.prosafe.data.model.Coordinates;
import com.harish.prosafe.data.model.Gender;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.data.model.IncidentCategory;
import com.harish.prosafe.data.model.User;
import com.harish.prosafe.ui.login.LoginActivity;
import com.harish.prosafe.ui.login.EventListener;
import com.harish.prosafe.ui.registration.RegistrationActivity;


import java.util.ArrayList;
import java.util.List;

public class FirebaseProvider implements IBackendProvider {
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private EventListener listener;
    private List<Incident> incidentListData;
    private List<IncidentCategory> CategoryListData;
    IncidentCategoryAdapter incidentCategoryAdapter;
    IncidentAdapter incidentAdapter;

    static FirebaseProvider firebaseProvider;

    private static final String TAG = "FirebaseProvider";

    public static FirebaseProvider getFirebaseProvider() {
        if (firebaseProvider == null)
            firebaseProvider = new FirebaseProvider();
        return firebaseProvider;
    }

    private FirebaseProvider() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        incidentListData = new ArrayList<>();
        CategoryListData = new ArrayList<>();
        this.listener = null;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    @Override
    public String getUserName() {
        return mAuth.getCurrentUser().getDisplayName();
    }

    // Assign the listener implementing events interface that will receive the events
    public void setEventListener(EventListener listener) {
        this.listener = listener;
    }


    public DatabaseReference getUserDatabase() {
        return databaseReference.child("Users");
    }

    public DatabaseReference getIncidentDatabase() {
        return databaseReference.child("Incidents");
    }

    public DatabaseReference getCategoriesDatabase() {
        return databaseReference.child("IncidentCategories");
    }


    @Override
    public FirebaseProvider addIncident(Incident incident) {
        DatabaseReference incidentDatabaseReference;
        incidentDatabaseReference = getIncidentDatabase();
        String incidentId = incidentDatabaseReference.push().getKey();
        incidentDatabaseReference.child(incidentId).setValue(incident).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (listener != null)
                    listener.onSuccess();
                else {
                    Log.e(TAG, "Listener error");
                    listener.onFailed();
                }
            } else {
                listener.onFailed();
            }
        });
        return getFirebaseProvider();
    }

    @Override
    public FirebaseProvider loginUser(String email, String password, LoginActivity loginActivity) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(loginActivity, task -> {
                    if (task.isSuccessful()) {
                        if (listener != null)
                            listener.onSuccess();
                        else {
                            Log.e(TAG, "Listener error");
                            listener.onFailed();
                        }
                    } else {
                        listener.onFailed();
                    }
                });
        return getFirebaseProvider();
    }

    @Override
    public FirebaseProvider registerUser(final String firstName, final String lastName, final String email, String password, final String mobile, RegistrationActivity registrationActivity) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(registrationActivity, task -> {
                    if (task.isSuccessful()) {
                        DatabaseReference userDatabaseReference;
                        userDatabaseReference = getUserDatabase();
                        String userId = getCurrentUser().getUid();
                        long unixTime = System.currentTimeMillis() / 1000L;
                        User user = new User(firstName, lastName, mobile, email, mobile, Gender.MALE, unixTime);
                        FirebaseUser currentUser = getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(firstName + " " + lastName).build();

                        currentUser.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                        userDatabaseReference.child(userId).setValue(user).addOnCompleteListener(task11 -> {
                                            if (task11.isSuccessful()) {
                                                if (listener != null)
                                                    listener.onSuccess();
                                                else {
                                                    Log.e(TAG, "Listener error");
                                                    listener.onFailed();
                                                }
                                            } else {
                                                listener.onFailed();
                                            }
                                        });
                                    } else {
                                        listener.onFailed();
                                    }
                                });
                    } else {
                        listener.onFailed();
                    }
                });
        return getFirebaseProvider();
    }

    @Override
    public FirebaseProvider addIncidentValueEventListener(IncidentValueChangeListener valueChangeListener) {
        getIncidentDatabase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                incidentListData.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot npsnapshot : snapshot.getChildren()) {
                        Incident l = npsnapshot.getValue(Incident.class);
                        incidentListData.add(l);
                    }
                    incidentAdapter = new IncidentAdapter(incidentListData);
                    valueChangeListener.onSuccess(incidentAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                valueChangeListener.onFailed();
            }
        });

        return getFirebaseProvider();
    }

    @Override
    public FirebaseProvider addCategoryValueEventListener(IncidentCategoryValueChangeListener valueChangeListener) {
        getCategoriesDatabase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CategoryListData.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot npsnapshot : snapshot.getChildren()) {
                        IncidentCategory category = npsnapshot.getValue(IncidentCategory.class);
                        CategoryListData.add(category);
                    }
                    incidentCategoryAdapter = new IncidentCategoryAdapter(CategoryListData);
                    valueChangeListener.onSuccess(incidentCategoryAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                valueChangeListener.onFailed();
            }
        });

        return getFirebaseProvider();
    }

    @Override
    public FirebaseProvider getAddressValueEventListener(AddressValueChangeListener valueChangeListener) {
        getUserDatabase().child(getCurrentUser().getUid()).child("coordinates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    valueChangeListener.onSuccess(dataSnapshot.getValue(Coordinates.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                valueChangeListener.onFailed();
            }
        });
        return getFirebaseProvider();
    }

    @Override
    public FirebaseProvider addAddressValueEventListener(Coordinates coordinates) {
        getUserDatabase().child(getCurrentUser().getUid()).child("coordinates").setValue(coordinates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (listener != null)
                    listener.onSuccess();
                else {
                    Log.e(TAG, "Listener error");
                    listener.onFailed();
                }
            } else {
                listener.onFailed();
            }
        });
        return getFirebaseProvider();
    }

    @Override
    public Coordinates getUserCoordinates() {
        return null;
    }

}
