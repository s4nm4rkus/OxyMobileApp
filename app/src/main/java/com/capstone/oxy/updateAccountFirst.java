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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class updateAccountFirst extends AppCompatActivity {

    //Only when the app used by the user for the first time

    FirebaseUser firebaseChangePass;
    EditText editTextFname, editTextLname, editTextPassword, editTextConfirm;
    TextView btn_save;
    CircularProgressIndicator progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account_first);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        // Declare Firebase user and UI elements
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        firebaseChangePass = mAuth.getCurrentUser();
        editTextFname = findViewById(R.id.firstname);
        editTextLname = findViewById(R.id.lastname);
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

                String fname, lname, newpass, conpass;
                fname = String.valueOf(editTextFname.getText());
                lname = String.valueOf(editTextLname.getText());
                newpass = String.valueOf(editTextPassword.getText());
                conpass = String.valueOf(editTextConfirm.getText());

                if (TextUtils.isEmpty(conpass) && TextUtils.isEmpty(newpass) && TextUtils.isEmpty(fname) && TextUtils.isEmpty(lname)) {
                    Toast.makeText(updateAccountFirst.this, "Hold on! All fields are required. Don't forget to fill them in.", Toast.LENGTH_SHORT).show();
                    editTextFname.setBackgroundResource(R.drawable.error_login_background);
                    editTextFname.setHintTextColor(getResources().getColor(R.color.redoxy));
                    editTextLname.setBackgroundResource(R.drawable.error_login_background);
                    editTextLname.setHintTextColor(getResources().getColor(R.color.redoxy));
                    editTextPassword.setBackgroundResource(R.drawable.error_login_background);
                    editTextPassword.setHintTextColor(getResources().getColor(R.color.redoxy));
                    editTextConfirm.setBackgroundResource(R.drawable.error_login_background);
                    editTextConfirm.setHintTextColor(getResources().getColor(R.color.redoxy));
                    progressBar.setVisibility(View.GONE);
                    btn_save.setVisibility(View.VISIBLE);
                    return;

                } else if (TextUtils.isEmpty(fname) && TextUtils.isEmpty(lname)) {
                    Toast.makeText(updateAccountFirst.this, "Hold on! Your name is required to proceed", Toast.LENGTH_SHORT).show();
                    editTextFname.setBackgroundResource(R.drawable.error_login_background);
                    editTextFname.setHintTextColor(getResources().getColor(R.color.redoxy));
                    editTextLname.setBackgroundResource(R.drawable.error_login_background);
                    editTextLname.setHintTextColor(getResources().getColor(R.color.redoxy));
                    progressBar.setVisibility(View.GONE);
                    btn_save.setVisibility(View.VISIBLE);
                    return;

                } else if (TextUtils.isEmpty(fname)) {
                    Toast.makeText(updateAccountFirst.this, "Oops! It looks like you forgot to provide your first name.", Toast.LENGTH_SHORT).show();
                    editTextFname.setBackgroundResource(R.drawable.error_login_background);
                    editTextFname.setHintTextColor(getResources().getColor(R.color.redoxy));
                    progressBar.setVisibility(View.GONE);
                    btn_save.setVisibility(View.VISIBLE);
                    return;

                } else if (TextUtils.isEmpty(lname)) {
                    Toast.makeText(updateAccountFirst.this, "Oops! It looks like you forgot to provide your last name.", Toast.LENGTH_SHORT).show();
                    editTextLname.setBackgroundResource(R.drawable.error_login_background);
                    editTextLname.setHintTextColor(getResources().getColor(R.color.redoxy));
                    progressBar.setVisibility(View.GONE);
                    btn_save.setVisibility(View.VISIBLE);
                    return;

                }

                    else if (TextUtils.isEmpty(newpass) && TextUtils.isEmpty(conpass)) {
                        Toast.makeText(updateAccountFirst.this, "Please set-up your own password to proceed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(updateAccountFirst.this, "Please set-up your own password to proceed", Toast.LENGTH_SHORT).show();
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
                    }else if (!newpass.matches(conpass)) {
                        Toast.makeText(updateAccountFirst.this, "Oops! Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        editTextConfirm.setBackgroundResource(R.drawable.error_login_background);
                        editTextConfirm.setHintTextColor(getResources().getColor(R.color.redoxy));
                        btn_save.setVisibility(View.VISIBLE);
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                        firebaseChangePass.updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(updateAccountFirst.this, "You've successfully changed your password", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(updateAccountFirst.this, activityRoomSelection.class);
                                startActivity(intent);
                                progressBar.setVisibility(View.GONE);
                                return;
                            }
                        });
                    }
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