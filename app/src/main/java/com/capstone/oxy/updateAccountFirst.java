package com.capstone.oxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.oxy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class updateAccountFirst extends AppCompatActivity {

    // Only when the app used by the user for the first time

    FirebaseUser firebaseChangePass;
    FirebaseFirestore firebaseFirestore;
    EditText editTextUpdatename, editTextPassword, editTextConfirm;
    TextView btn_save;
    CircularProgressIndicator progressBar;
    String userDocumentId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account_first);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        firebaseChangePass = mAuth.getCurrentUser();
        editTextUpdatename = findViewById(R.id.username_update);
        editTextPassword = findViewById(R.id.signupPassword);
        editTextConfirm = findViewById(R.id.signupConfirmPassword);
        btn_save = findViewById(R.id.btn_save);
        progressBar = findViewById(R.id.progressBar);
        btn_save.setVisibility(View.VISIBLE);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_save.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                String username, newpass, conpass;
                username = String.valueOf(editTextUpdatename.getText());
                newpass = String.valueOf(editTextPassword.getText());
                conpass = String.valueOf(editTextConfirm.getText());

                if (TextUtils.isEmpty(conpass) && TextUtils.isEmpty(newpass) && TextUtils.isEmpty(username)) {
                    Toast.makeText(updateAccountFirst.this, "Hold on! All fields are required. Don't forget to fill them in.", Toast.LENGTH_SHORT).show();
                    editTextUpdatename.setBackgroundResource(R.drawable.error_login_background);
                    editTextUpdatename.setHintTextColor(getResources().getColor(R.color.redoxy));
                    editTextPassword.setBackgroundResource(R.drawable.error_login_background);
                    editTextPassword.setHintTextColor(getResources().getColor(R.color.redoxy));
                    editTextConfirm.setBackgroundResource(R.drawable.error_login_background);
                    editTextConfirm.setHintTextColor(getResources().getColor(R.color.redoxy));
                    progressBar.setVisibility(View.GONE);
                    btn_save.setVisibility(View.VISIBLE);
                    return;
                } else if (TextUtils.isEmpty(username)) {
                    Toast.makeText(updateAccountFirst.this, "Oops! It looks like you forgot to provide Username.", Toast.LENGTH_SHORT).show();
                    editTextUpdatename.setBackgroundResource(R.drawable.error_login_background);
                    editTextUpdatename.setHintTextColor(getResources().getColor(R.color.redoxy));
                    progressBar.setVisibility(View.GONE);
                    btn_save.setVisibility(View.VISIBLE);
                    return;
                } else if (TextUtils.isEmpty(newpass) && TextUtils.isEmpty(conpass)) {
                    Toast.makeText(updateAccountFirst.this, "Please set-up your password to proceed.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    editTextPassword.setBackgroundResource(R.drawable.error_login_background);
                    editTextPassword.setHintTextColor(getResources().getColor(R.color.redoxy));
                    editTextConfirm.setBackgroundResource(R.drawable.error_login_background);
                    editTextConfirm.setHintTextColor(getResources().getColor(R.color.redoxy));
                    editTextPassword.requestFocus();
                    editTextConfirm.requestFocus();
                    btn_save.setVisibility(View.VISIBLE);
                    return;
                } else if (TextUtils.isEmpty(newpass)) {
                    Toast.makeText(updateAccountFirst.this, "Please set-up your password to proceed.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    editTextPassword.setBackgroundResource(R.drawable.error_login_background);
                    editTextPassword.setHintTextColor(getResources().getColor(R.color.redoxy));
                    btn_save.setVisibility(View.VISIBLE);
                    return;
                } else if (TextUtils.isEmpty(conpass)) {
                    Toast.makeText(updateAccountFirst.this, "Oops! Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    editTextConfirm.setBackgroundResource(R.drawable.error_login_background);
                    editTextConfirm.setHintTextColor(getResources().getColor(R.color.redoxy));
                    btn_save.setVisibility(View.VISIBLE);
                    return;
                } else if (!newpass.matches(conpass)) {
                    Toast.makeText(updateAccountFirst.this, "Oops! Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    editTextConfirm.setBackgroundResource(R.drawable.error_login_background);
                    editTextConfirm.setHintTextColor(getResources().getColor(R.color.redoxy));
                    btn_save.setVisibility(View.VISIBLE);
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    // Update the username in Firestore
                    updateUsernameInFirestore(username);
                }
            }
        });
    }

    private void updateUsernameInFirestore(String newUsername) {
        // Check if the user is authenticated
        if (firebaseChangePass != null) {
            // Get the current user's document reference
            DocumentReference userRef = firebaseFirestore.collection("users").document(firebaseChangePass.getUid());

            // Create a map with the new username
            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("Username", newUsername);

            // Update the username in Firestore
            userRef.update(updatedData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(updateAccountFirst.this, "Account has been successfully updated.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(updateAccountFirst.this, activityRoomSelection.class);
                                startActivity(intent);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(updateAccountFirst.this, "Failed to update username", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                btn_save.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
