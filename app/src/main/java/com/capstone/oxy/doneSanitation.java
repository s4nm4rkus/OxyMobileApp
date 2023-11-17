package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

public class doneSanitation extends AppCompatActivity {

    private ImageView doneLogo;
    private AnimatorSet flipAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the sanitation_done layout
        setContentView(R.layout.activity_done_sanitation);

        // Set the status bar color to teal
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));

        // Make the navigation bar translucent
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

        // Find the ImageView for the logo
        doneLogo = findViewById(R.id.done_logo);

        // Load and start the flip animation
        flipAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.done_anim);
        flipAnimation.setTarget(doneLogo);
        flipAnimation.start();

        // Create a delayed handler to navigate to the main_dashboard activity after a delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an intent to start the main_dashboard activity
                Intent intent = new Intent(doneSanitation.this, mainDashboard.class);
                intent.putExtra("selectedRoom", "Room01");
                startActivity(intent);
                finish(); // Finish the current activity to prevent going back to it
            }
        }, 2000);
    }
}
