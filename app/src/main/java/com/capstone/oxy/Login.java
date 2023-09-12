package com.capstone.oxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealsecondary));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        editTextUsername = findViewById(R.id.Username);
        editTextPassword = findViewById(R.id.Password);
        errorTextView = findViewById(R.id.errorTextView);
        login_btn = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        drawableEnd = getResources().getDrawable(R.drawable.err_info2);
        login_btn.setVisibility(View.VISIBLE);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_btn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);


                String email, password;
                email = String.valueOf(editTextUsername.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Oops ! Please enter your username and password to log in.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    editTextUsername.setBackgroundResource(R.drawable.error_login_background);
                    editTextPassword.setBackgroundResource(R.drawable.error_login_background);
                    editTextUsername.setHintTextColor(getResources().getColor(R.color.redoxy));
                    editTextPassword.setHintTextColor(getResources().getColor(R.color.redoxy));
                    login_btn.setVisibility(View.VISIBLE);
                    return;
                }

                else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Oops! Your password is missing. Please enter your username to log in.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    editTextUsername.setBackgroundResource(R.drawable.error_login_background);
                    editTextUsername.setHintTextColor(getResources().getColor(R.color.redoxy));
                    login_btn.setVisibility(View.VISIBLE);
                    return;
                }
                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Oops! Your password is missing. Please enter your password to log in.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    editTextPassword.setBackgroundResource(R.drawable.error_login_background);
                    editTextPassword.setHintTextColor(getResources().getColor(R.color.redoxy));
                    login_btn.setVisibility(View.VISIBLE);
                    return;
                }



              mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), update_pwdfirst.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
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
}