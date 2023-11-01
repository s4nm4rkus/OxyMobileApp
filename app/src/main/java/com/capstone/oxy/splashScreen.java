package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splashScreen extends AppCompatActivity {

    private AnimatorSet flipAnimation;
    private static final long SPLASH_DELAY = 5000; // Splash screen delay in milliseconds

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

        // Set the content view to the main layout
        setContentView(R.layout.activity_main);

        // Find the ImageView for the logo
        ImageView logoImageView = findViewById(R.id.imageView);

        // Load and start the splash animation
        flipAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.splash);
        flipAnimation.setTarget(logoImageView);
        flipAnimation.start();

        // Create a delayed handler to navigate to the Login activity after the splash delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an intent to start the Login activity
                Intent intent = new Intent(splashScreen.this, Login.class);
                startActivity(intent);
                finish(); // Finish the current activity to prevent going back to it
            }
        }, SPLASH_DELAY);
    }
}
