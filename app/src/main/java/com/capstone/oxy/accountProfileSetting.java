package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class accountProfileSetting extends AppCompatActivity {
    private TextView userName, emailAccount;
    private EditText userNameEditText, passwordEditText, confirmPassword, currentPassword;
    private Button saveChanges;
    private ImageButton backBtn;

    private ProgressDialog dialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private String originalUsername;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_profile_setting);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

        userName = findViewById(R.id.user_nameLabel);
        emailAccount = findViewById(R.id.email_accountLabel);
        userNameEditText = findViewById(R.id.username_update);
        passwordEditText = findViewById(R.id.accountPassword);
        confirmPassword = findViewById(R.id.accountConfirmPassword);
        currentPassword = findViewById(R.id.accountCurrentPassword);
        backBtn = findViewById(R.id.back_button);
        saveChanges = findViewById(R.id.accountSave);

        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            boolean passwordVisible = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[2].getBounds().width())) {
                        // The touch was within the bounds of the drawableEnd (drawable at the end of EditText)

                        passwordVisible = !passwordVisible;
                        if (passwordVisible) {
                            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_account_setting, 0, R.drawable.ic_show_pass, 0);
                        } else {
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_account_setting, 0, R.drawable.ic_unshow_pass, 0);
                        }

                        // Move the cursor to the end of the text
                        passwordEditText.setSelection(passwordEditText.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });

        confirmPassword.setOnTouchListener(new View.OnTouchListener() {
            boolean passwordVisible = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (confirmPassword.getRight() - confirmPassword.getCompoundDrawables()[2].getBounds().width())) {
                        // The touch was within the bounds of the drawableEnd (drawable at the end of EditText)

                        passwordVisible = !passwordVisible;
                        if (passwordVisible) {
                            confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_account_setting, 0, R.drawable.ic_show_pass, 0);
                        } else {
                            confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_account_setting, 0, R.drawable.ic_unshow_pass, 0);
                        }

                        // Move the cursor to the end of the text
                        confirmPassword.setSelection(confirmPassword.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });

        currentPassword.setOnTouchListener(new View.OnTouchListener() {
            boolean passwordVisible = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (currentPassword.getRight() - currentPassword.getCompoundDrawables()[2].getBounds().width())) {
                        // The touch was within the bounds of the drawableEnd (drawable at the end of EditText)

                        passwordVisible = !passwordVisible;
                        if (passwordVisible) {
                            currentPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            currentPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, R.drawable.ic_show_pass, 0);
                        } else {
                            currentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            currentPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, R.drawable.ic_unshow_pass, 0);
                        }

                        // Move the cursor to the end of the text
                        currentPassword.setSelection(currentPassword.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(accountProfileSetting.this);
        dialog.setCancelable(false);
        dialog.setMessage("Saving changes..");

        if (passwordEditText.isEnabled() && confirmPassword.isEnabled()){
            passwordEditText.setAlpha(1);
            confirmPassword.setAlpha(1);
        }

        fetchUserData();

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUsername();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        fetchUserData();
    }

    private void fetchUserData() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DocumentReference userRef = db.collection("users").document(userId);
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String username = document.getString("Username");
                        String email = document.getString("Email");

                        userName.setText(username);
                        emailAccount.setText(email);
                        userNameEditText.setText(username);

                        originalUsername = username;

                        // ... (rest of the code)

                        currentPassword.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                passwordEditText.setEnabled(!charSequence.toString().isEmpty());
                                confirmPassword.setEnabled(!charSequence.toString().isEmpty());
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                            }
                        });
                    } else {
                        Toast.makeText(accountProfileSetting.this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(accountProfileSetting.this, "Error fetching user data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(accountProfileSetting.this, "No user is currently signed in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUsername() {
        String newUsername = userNameEditText.getText().toString();
        String currentPass = currentPassword.getText().toString();
        String newPassword = passwordEditText.getText().toString();
        String confirmPass = confirmPassword.getText().toString();

        if (currentPass.isEmpty()) {
            Toast.makeText(accountProfileSetting.this, "Please enter your current password to apply any changes.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the current password is correct
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPass);
            currentUser.reauthenticate(credential)
                    .addOnSuccessListener(aVoid -> {
                        // Reauthentication successful, you can now update the username and password
                        if (!newPassword.isEmpty() && !confirmPass.isEmpty()) {
                            if (!newPassword.equals(confirmPass)) {
                                Toast.makeText(accountProfileSetting.this, "New password and confirm password do not match.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        DocumentReference userRef = db.collection("users").document(currentUser.getUid());
                        if (!newUsername.isEmpty()) {
                            userRef.update("Username", newUsername);
                            originalUsername = newUsername;
                        }
                        if (!newPassword.isEmpty() && !confirmPass.isEmpty()) {
                            userRef.update("Password", newPassword);
                            passwordEditText.setText("");  // Clear the password fields
                            confirmPassword.setText("");
                            passwordEditText.setEnabled(false);  // Disable the password fields
                            confirmPassword.setEnabled(false);
                        }
                        dialog.show();
                        new Handler().postDelayed(() -> {
                            dialog.dismiss();
                            Toast.makeText(accountProfileSetting.this, "Changes have been saved. You'll be navigated to room selection.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(accountProfileSetting.this, activityRoomSelection.class);
                            startActivity(intent);
                            finish();
                        }, 2000);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(accountProfileSetting.this, "Incorrect current password.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(accountProfileSetting.this, "No user is currently signed in.", Toast.LENGTH_SHORT).show();
        }
    }
}
