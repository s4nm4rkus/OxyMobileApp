package com.capstone.oxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    TextView login_btn;
    FirebaseAuth mAuth;
    CircularProgressIndicator progressBar;
    Drawable drawableEnd;
    TextView errorTextView;
    Button forgotBtn;
    LinearLayout loginPage;
    ImageView loginLogo;
    Animation slideUpAnimation, slideDownAnimation;
    CardView cardView;
    CheckBox checkBox;
    FirebaseFirestore usersData;
    public static final String SHARED_PREPS = "sharedPrefs";

    @SuppressLint("MissingInflatedId")
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

        usersData=FirebaseFirestore.getInstance();

        checkBox();

        // Set the layout for this activity
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        checkBox = findViewById(R.id.checkBox);
        cardView = findViewById(R.id.cardView);
        loginLogo = findViewById(R.id.loginLogo);
        loginPage = findViewById(R.id.loginPage);
        forgotBtn = findViewById(R.id.forgotButton);
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        editTextUsername = findViewById(R.id.Username);
        editTextPassword = findViewById(R.id.Password);
        errorTextView = findViewById(R.id.errorTextView);
        login_btn = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        drawableEnd = getResources().getDrawable(R.drawable.err_info2);
        login_btn.setVisibility(View.VISIBLE);

        slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up_anim);
        slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down_anim);

        loginLogo.startAnimation(slideDownAnimation);
        cardView.startAnimation(slideUpAnimation);

        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, forgotPassword.class);
                startActivity(intent);
            }
        });

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

                // If the checkbox is checked, store a value indicating that the user should stay logged in
                if (checkBox.isChecked()) {
                    saveLoginStatus(true);
                }

                // Attempt to sign in with the provided email and password
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    checkIfAdmin();
                                } else {
                                    // If login fails, show an error message
                                    Toast.makeText(Login.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    editTextUsername.setBackgroundResource(R.drawable.error_login_background);
                                    editTextPassword.setBackgroundResource(R.drawable.error_login_background);
                                    login_btn.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });
    }
    private void checkIfAdmin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(userId);

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            progressBar.setVisibility(View.GONE);
                            String userLevel = document.getString("USER-LEVEL");
                            if (userLevel != null && userLevel.equals("Administrator")) {
                                Toast.makeText(getApplicationContext(), "Logged in as Administrator", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), updateAccountFirst.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Log in Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), updateAccountFirst.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // Handle the case where the document does not exist
                        }
                    } else {
                        // Handle errors here if needed
                    }
                }
            });
        }
    }



    private void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREPS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    private boolean getLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREPS, MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void checkBox() {
        boolean isLoggedIn = getLoginStatus();
        if (isLoggedIn) {
            Toast.makeText(getApplicationContext(), "Already Logged In", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), updateAccountFirst.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
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
