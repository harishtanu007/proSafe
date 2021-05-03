package com.harish.prosafe.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.harish.prosafe.data.model.Gender;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.data.model.User;
import com.harish.prosafe.ui.login.LoginActivity;
import com.harish.prosafe.ui.login.EventListener;
import com.harish.prosafe.ui.registration.RegistrationActivity;

public class FirebaseProvider implements IBackendProvider {
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private EventListener listener;

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
        this.listener = null;
    }

    @Override
    public String getUser() {
        String userName = mAuth.getCurrentUser().getDisplayName();
        return userName;
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
    @Override
    public FirebaseProvider addIncident(Incident incident) {
        DatabaseReference incidentDatabaseReference;
        incidentDatabaseReference = getIncidentDatabase();
        String incidentId = incidentDatabaseReference.push().getKey();
        incidentDatabaseReference.child(incidentId).setValue(incident).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
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
            }
        });
        return getFirebaseProvider();
    }

    @Override
    public FirebaseProvider loginUser(String email, String password, LoginActivity loginActivity) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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
                    }
                });
        return getFirebaseProvider();
    }

    @Override
    public FirebaseProvider registerUser(final String firstName, final String lastName, final String email, String password, final String mobile, RegistrationActivity registrationActivity) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(registrationActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference userDatabaseReference;
                            userDatabaseReference = getUserDatabase();
                            String userId = userDatabaseReference.push().getKey();
                            long unixTime = System.currentTimeMillis() / 1000L;
                            User user = new User(firstName, lastName, mobile, email, mobile, Gender.MALE, unixTime);

                            userDatabaseReference.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
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
                                }
                            });
                        } else {
                            listener.onFailed();
                        }
                    }
                });
        return getFirebaseProvider();
    }

}
