package com.harish.prosafe.util;

import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.ui.login.LoginActivity;
import com.harish.prosafe.ui.registration.RegistrationActivity;

public interface IBackendProvider {
  void addIncident(Incident incident);
  FirebaseProvider loginUser(String email, String password, LoginActivity loginActivity);
  FirebaseProvider registerUser(final String firstName, final String lastName, final String email, String password, final String mobile, RegistrationActivity registrationActivity);
}
