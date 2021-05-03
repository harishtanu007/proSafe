package com.harish.prosafe.ui.registration;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.harish.prosafe.MainActivity;
import com.harish.prosafe.R;
import com.harish.prosafe.data.model.Gender;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.data.model.User;
import com.harish.prosafe.ui.login.LoginActivity;
import com.harish.prosafe.ui.login.LoginListener;
import com.harish.prosafe.util.FirebaseProvider;
import com.harish.prosafe.util.Helper;
import com.harish.prosafe.util.IBackendProvider;


import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";

    @BindView(R.id.first_name)
    EditText _firstNameText;
    @BindView(R.id.last_name)
    EditText _lastNameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_mobile)
    EditText _mobileText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;

    private ProgressDialog mRegProgress;
    private Helper helper;
    private ScrollView rootLayout;
    IBackendProvider backendProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        helper = Helper.getHelper();
        rootLayout = findViewById(R.id.rootlayout);
        backendProvider = FirebaseProvider.getFirebaseProvider();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.isInternetConnected(getApplicationContext()))
                    signup();
                else
                    Snackbar.make(rootLayout, "No Internet Connection!", Snackbar.LENGTH_LONG).show();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }



//    private void sendVerificationEmail() {
//        try {
//            final FirebaseUser user = mAuth.getCurrentUser();
//            user.sendEmailVerification()
//                    .addOnCompleteListener(new OnCompleteListener() {
//                        @Override
//                        public void onComplete(@NonNull Task task) {
//                            // Re-enable button
//
//                            if (task.isSuccessful()) {
//                                Snackbar.make(rootLayout, "Verification email sent to " + user.getEmail(), Snackbar.LENGTH_LONG).show();
//                                FirebaseAuth.getInstance().signOut();
//                            } else {
//                                Log.e(TAG, "sendEmailVerification", task.getException());
//                                Snackbar.make(rootLayout, "Failed to send verification email.", Snackbar.LENGTH_LONG).show();
//                                FirebaseAuth.getInstance().signOut();
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//            login();
//
//        }
//    }


    public void signup() {
        Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed();
            return;
        }
        mRegProgress = new ProgressDialog(RegistrationActivity.this);
        mRegProgress.setIndeterminate(true);
        mRegProgress.setCanceledOnTouchOutside(false);
        mRegProgress.setMessage("Creating Account...");
        mRegProgress.show();

        String firstName = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.
        backendProvider.registerUser(firstName, lastName, email, password, mobile,this).setLoginListener(new LoginListener() {
            @Override
            public void onSuccess() {
                Snackbar.make(rootLayout, "Account Created successfully", Snackbar.LENGTH_LONG).show();
                mRegProgress.dismiss();
                onSignupSuccess();
            }

            @Override
            public void onFailed() {
                mRegProgress.dismiss();
                Snackbar.make(rootLayout, "Account creation failed", Snackbar.LENGTH_LONG).show();
            }
        });

    }


    public void onSignupSuccess() {
        Intent uploadIntent = new Intent(getApplicationContext(), LoginActivity.class);
        uploadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(uploadIntent);
        finish();
    }


    public void onSignupFailed() {
        Snackbar.make(rootLayout, "Login failed", Snackbar.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            _firstNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _firstNameText.setError(null);
        }
        if (lastName.isEmpty() || lastName.length() < 3) {
            _lastNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _lastNameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

//        if (mobile.length() != 10) {
//            _mobileText.setError("Enter Valid Mobile Number");
//            valid = false;
//        } else {
//            _mobileText.setError(null);
//        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
//        if (mobile.isEmpty() || mobile.length()!=10) {
//            _mobileText.setError("contains 10 digits");
//            valid = false;
//        } else {
//            _mobileText.setError(null);
//        }


        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    private void login() {
        // Finish the registration screen and return to the Login activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
