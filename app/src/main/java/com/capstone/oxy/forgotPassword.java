package com.capstone.oxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class forgotPassword extends AppCompatActivity {

    Button confirmEmail, cancelBtn;
    ProgressDialog dialog;
    FirebaseAuth auth;
    EditText inputYourEmail;
    ConstraintLayout forgotPage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));

        // Make the navigation bar translucent
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );


        forgotPage=findViewById(R.id.forgotPage);
        inputYourEmail = findViewById(R.id.forgotPasswordEmailInput);
        confirmEmail = findViewById(R.id.confirmEmail);
        cancelBtn = findViewById(R.id.cancelEmail);

        Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up_anim);

        forgotPage.startAnimation(slideUpAnimation);

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(forgotPassword.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");

        confirmEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputYourEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    // Email is empty
                    Toast.makeText(forgotPassword.this, "Oops! Your email is missing. Please enter your email to reset your password.", Toast.LENGTH_SHORT).show();
                    inputYourEmail.setBackgroundResource(R.drawable.error_login_background);
                    inputYourEmail.setHintTextColor(getResources().getColor(R.color.redoxy));
                } else if (!isValidEmail(email)) {
                    // Invalid email format
                    Toast.makeText(forgotPassword.this, "Oops! Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                    inputYourEmail.setBackgroundResource(R.drawable.error_login_background);
                    inputYourEmail.setHintTextColor(getResources().getColor(R.color.redoxy));
                } else {
                    // Valid email, proceed with sending the password reset email
                    dialog.show();

                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                startActivity(new Intent(forgotPassword.this, Login.class));
                                finish();
                                Toast.makeText(forgotPassword.this, "Please check your Email Address.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(forgotPassword.this, "Failed to send reset email. Please check your Email Address", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(forgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            private boolean isValidEmail(String email) {
                return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        finish();
    }
}