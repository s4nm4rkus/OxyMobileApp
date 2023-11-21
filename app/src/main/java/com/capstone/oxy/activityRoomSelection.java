package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class activityRoomSelection extends AppCompatActivity {

    TextView logout_btn;
    TextView room101_btn, room102_btn;
    FirebaseAuth mAuth;
    public static final String SHARED_PREPS = "sharedPrefs";

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
        setContentView(R.layout.activity_select_classroom);

        // Initialize UI elements
        logout_btn = findViewById(R.id.btn_logout);
        room101_btn = findViewById(R.id.room101);
        room102_btn = findViewById(R.id.room102);
        mAuth = FirebaseAuth.getInstance();

        // Set a click listener for the "Room 101" button
        room101_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to the connecting_screen activity
                Intent intent = new Intent(activityRoomSelection.this, roomLoadingConnecting.class);
                intent.putExtra("selectedRoom", "Room01");
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            }
        });

        room102_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to the connecting_screen activity
                Intent intent = new Intent(activityRoomSelection.this, roomLoadingConnecting.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // Set a click listener for the "Logout" button
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    // Method to handle logout button click
    private void logout() {
        // Sign out the user
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREPS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear the stored username
        editor.putString("name", "");

        // Set a flag indicating that the checkbox should be enabled
        editor.putBoolean("enableCheckbox", true);

        // Set isLoggedIn to false to indicate that the user is no longer logged in
        editor.putBoolean("isLoggedIn", false);

        editor.apply();

        mAuth.signOut();

        // Navigate back to the login activity
        Intent intent = new Intent(activityRoomSelection.this, Login.class);
        startActivity(intent);
        finish(); // Finish the current activity to prevent going back to it with the back button
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