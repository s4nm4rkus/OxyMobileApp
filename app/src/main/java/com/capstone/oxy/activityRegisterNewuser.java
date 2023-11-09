package com.capstone.oxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class activityRegisterNewuser extends AppCompatActivity {

    TextView registerNewUser, cancelRegistration;
    TextInputEditText newEmail, newUserPass, confirmUserPass, userName;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userAccountId;
    CircularProgressIndicator progressBar;
    CheckBox adminChkbx;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_newuser);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

        adminChkbx = findViewById(R.id.checkBoxAdminSelect);
        userName = findViewById(R.id.userName);
        newEmail = findViewById(R.id.new_email);
        newUserPass = findViewById(R.id.new_userPassword);
        confirmUserPass = findViewById(R.id.confirm_userPassword);

        registerNewUser = findViewById(R.id.register_newuser);
        cancelRegistration = findViewById(R.id.cancel_registration);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        registerNewUser.setVisibility(View.VISIBLE);
        cancelRegistration.setVisibility(View.VISIBLE);

        registerNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = newEmail.getText().toString().trim();
                String password = newUserPass.getText().toString().trim();
                String confirmPassword = confirmUserPass.getText().toString().trim();
                String username = userName.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    newEmail.setError("Email is Required.");
                    return;
                } else if (!isValidEmail(email)) {
                    newEmail.setError("Invalid Email Address.");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    newUserPass.setError("Password is Required.");
                    return;
                } else if (password.length() < 6) {
                    newUserPass.setError("Password must be at least 6 characters long.");
                    return;
                } else if (!password.equals(confirmPassword)) {
                    confirmUserPass.setError("Passwords do not match.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Create user with email and password
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activityRegisterNewuser.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    Toast.makeText(activityRegisterNewuser.this, "User has been created", Toast.LENGTH_SHORT).show();
                                    userAccountId = firebaseAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = firebaseFirestore.collection("users").document(userAccountId);
                                    Map<String, Object> users = new HashMap<>();
                                    users.put("Username", username);
                                    users.put("Email", email);
                                    if(adminChkbx.isChecked()){
                                        users.put("USER-LEVEL", "Administrator");
                                    }
                                    documentReference.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("TAG", "onSuccess: user Profile is Created for " + userAccountId);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("TAG", "onFailure: " + e);
                                        }
                                    });
                                    finish();
                                } else {
                                    progressBar.setVisibility(View.VISIBLE);
                                    Toast.makeText(activityRegisterNewuser.this, "An error occurred while processing your request. Please check your input and try again. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        cancelRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    public void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Confirmation")
                .setMessage("Do you want to cancel the Registration?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
