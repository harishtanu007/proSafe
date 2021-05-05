package com.harish.prosafe.util;

import com.google.firebase.auth.FirebaseUser;
import com.harish.prosafe.data.adapters.IncidentCategoryValueChangeListener;
import com.harish.prosafe.data.adapters.IncidentValueChangeListener;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.ui.login.LoginActivity;
import com.harish.prosafe.ui.registration.RegistrationActivity;

public interface IBackendProvider {
  static IBackendProvider getBackendProvider(){
    return FirebaseProvider.getFirebaseProvider();
  }
  String getUserName();
  FirebaseUser getCurrentUser();
  FirebaseProvider addIncident(Incident incident);
  FirebaseProvider loginUser(String email, String password, LoginActivity loginActivity);
  FirebaseProvider registerUser(final String firstName, final String lastName, final String email, String password, final String mobile, RegistrationActivity registrationActivity);
  FirebaseProvider addIncidentValueEventListener(IncidentValueChangeListener valueChangeListener);
  FirebaseProvider addCategoryValueEventListener(IncidentCategoryValueChangeListener valueChangeListener);
}
