package com.harish.prosafe.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.harish.prosafe.R;
import com.harish.prosafe.ui.registration.RegistrationActivity;
import com.harish.prosafe.util.Helper;
import com.harish.prosafe.util.IBackendProvider;


import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private ScrollView rootLayout;

    ProgressDialog progressDialog;
    Helper helper;

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    @BindView(R.id.forgot_password)
    TextView _forgotPassword;
    IBackendProvider backendProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        helper = Helper.getHelper();
        rootLayout = findViewById(R.id.rootlayout);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        backendProvider = IBackendProvider.getBackendProvider();

        if (currentUser == null) {

            ButterKnife.bind(this);

            _loginButton.setOnClickListener(v-> {
                    if (helper.isInternetConnected(getApplicationContext())) {
                        login();
                    } else {
                        Snackbar.make(rootLayout, "No Internet Connection!", Snackbar.LENGTH_LONG).show();
                    }
                });
            _signupLink.setOnClickListener(v ->   startActivityForResult(new Intent(getApplicationContext(), RegistrationActivity.class), REQUEST_SIGNUP));
        } else {
            sendToMain();
        }
    }

    private void sendToMain() {
        finish();
    }

    public void login() {
        Log.d(TAG, "Login");

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();

        // TODO: Implement your own authentication logic here.
        if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
            backendProvider.loginUser(email, password,this).setEventListener(new EventListener() {
                @Override
                public void onSuccess() {
                    progressDialog.hide();
                    sendToMain();
                }

                @Override
                public void onFailed() {
                    progressDialog.hide();
                    onLoginFailed();
                }
            });
        }

    }


    public void onLoginFailed() {
        Snackbar.make(rootLayout, "Login failed", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
