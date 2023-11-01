package com.capstone.oxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    TextView login_btn;
    FirebaseAuth mAuth;
    CircularProgressIndicator progressBar;
    Drawable drawableEnd;
    TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the status bar color to teal
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));

        // Make the navigation bar translucent
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

        // Set the layout for this activity
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        editTextUsername = findViewById(R.id.Username);
        editTextPassword = findViewById(R.id.Password);
        errorTextView = findViewById(R.id.errorTextView);
        login_btn = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        drawableEnd = getResources().getDrawable(R.drawable.err_info2);
        login_btn.setVisibility(View.VISIBLE);

        // Set an OnClickListener for the login button
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_btn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                String email, password;
                email = String.valueOf(editTextUsername.getText());
                password = String.valueOf(editTextPassword.getText());

                // Check if both email and password are empty
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Oops! Please enter your username and password to log in.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    editTextUsername.setBackgroundResource(R.drawable.error_login_background);
                    editTextPassword.setBackgroundResource(R.drawable.error_login_background);
                    editTextUsername.setHintTextColor(getResources().getColor(R.color.redoxy));
                    editTextPassword.setHintTextColor(getResources().getColor(R.color.redoxy));
                    login_btn.setVisibility(View.VISIBLE);
                    return;
                }
                // Check if email is empty
                else if (TextUtils.isEmpty(email)) {
                    editTextUsername.setError("Oops! Your email is missing. Please enter your username to log in.");
                    progressBar.setVisibility(View.GONE);
                    editTextUsername.setBackgroundResource(R.drawable.error_login_background);
                    editTextUsername.setHintTextColor(getResources().getColor(R.color.redoxy));
                    login_btn.setVisibility(View.VISIBLE);
                    return;
                }
                // Check if password is empty
                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Oops! Your password is missing. Please enter your password to log in.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    editTextPassword.setBackgroundResource(R.drawable.error_login_background);
                    editTextPassword.setHintTextColor(getResources().getColor(R.color.redoxy));
                    login_btn.setVisibility(View.VISIBLE);
                    return;
                }

                // Attempt to sign in with the provided email and password
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // If login is successful, navigate to another activity
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), updateAccountFirst.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If login fails, show an error message
                                    Toast.makeText(Login.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    editTextUsername.setBackgroundResource(R.drawable.error_login_background);
                                    editTextPassword.setBackgroundResource(R.drawable.error_login_background);
                                    login_btn.setVisibility(View.VISIBLE);
                                    return;
                                }
                            }
                        });
            }
        });
    }
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToast("Press back again to exit");

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
