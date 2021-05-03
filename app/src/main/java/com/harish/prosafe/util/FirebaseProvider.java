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
import com.harish.prosafe.ui.login.LoginListener;
import com.harish.prosafe.ui.registration.RegistrationActivity;

public class FirebaseProvider implements IBackendProvider {
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private LoginListener listener;

    static FirebaseProvider firebaseProvider;

    private static final String TAG ="FirebaseProvider" ;
    public static FirebaseProvider getFirebaseProvider() {
        if(firebaseProvider == null)
            firebaseProvider= new FirebaseProvider();
        return firebaseProvider;
    }

    private FirebaseProvider() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.listener = null;
    }

    // Assign the listener implementing events interface that will receive the events
    public void setLoginListener(LoginListener listener) {
        this.listener = listener;
    }

    public DatabaseReference getUserDatabase(){
        return databaseReference.child("Users");
    }

    @Override
    public void addIncident(Incident incident) {
        DatabaseReference userDatabaseReference;
        userDatabaseReference = getUserDatabase();
        String incidentId = userDatabaseReference.push().getKey();
        userDatabaseReference.child(incidentId).setValue(incident);
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
                            else
                                Log.e(TAG,"Listener error");
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
                                        listener.onSuccess();
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
