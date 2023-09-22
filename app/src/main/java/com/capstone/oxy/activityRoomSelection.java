package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class activityRoomSelection extends AppCompatActivity {

    TextView logout_btn;
    TextView room101_btn, room102_btn;
    FirebaseAuth mAuth;

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
                startActivity(intent);
            }
        });
    }

    // Method to handle logout button click
    public void logout(View view) {
        // Sign out the user
        mAuth.signOut();

        // Create an intent to navigate to the Login activity
        Intent intent = new Intent(activityRoomSelection.this, Login.class);
        startActivity(intent);
    }
}
