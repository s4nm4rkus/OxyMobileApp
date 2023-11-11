package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class splashScreen extends AppCompatActivity {

    private AnimatorSet flipAnimation;
    private TextView proceedButton, greetingsWelcome;
    private CheckBox termsCheck;
    private static final long SPLASH_DELAY = 5000; // Splash screen delay in milliseconds
    private static final long FADE_IN_DURATION = 1000; // Duration for the fade-in animation in milliseconds

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
        // Set the content view to the main layout
        setContentView(R.layout.activity_main);

        // Find the ImageView for the logo
        ImageView logoImageView = findViewById(R.id.imageView);

        termsCheck = findViewById(R.id.termsCheck);
        proceedButton = findViewById(R.id.proceedButton);
        greetingsWelcome = findViewById(R.id.greetingsWelcome);

        // Load and start the splash animation
        flipAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.splash);
        flipAnimation.setTarget(logoImageView);
        flipAnimation.start();

        proceedButton.setEnabled(false);
        proceedButton.setAlpha(0.5f);


        // Create a delayed handler to navigate to the Login activity after the splash delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeInViews(greetingsWelcome);
                fadeInViews(proceedButton);
                fadeInViews(termsCheck);
                Toast.makeText(splashScreen.this, "Note: Terms of Use should be Accepted to continue.", Toast.LENGTH_SHORT).show();

            }
        }, SPLASH_DELAY);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(splashScreen.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        termsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isChecked) {
                            showTermsAlertDialog();
                        } else {
                            proceedButton.setEnabled(false);
                            proceedButton.setAlpha(0.5f);
                        }
                    }
                });
            }
        });
    }

    private void showTermsAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Terms of Use")
                    .setMessage("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, " +
                            "as opposed to using 'Content here, content here', making it look like readable English.")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            proceedButton.setEnabled(true);
                            proceedButton.setAlpha(1f);
                        }
                    })
                    .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            termsCheck.setChecked(false);
                            proceedButton.setAlpha(0.5f);
                            dialog.dismiss();
                            Toast.makeText(splashScreen.this, "Note: Terms of Use should be Accepted to continue.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
    }

    private void fadeInViews(View view) {
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setInterpolator(new DecelerateInterpolator());
        fadeInAnimation.setDuration(FADE_IN_DURATION);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(fadeInAnimation);

        view.setVisibility(View.VISIBLE);
        view.startAnimation(animationSet);
    }
}
