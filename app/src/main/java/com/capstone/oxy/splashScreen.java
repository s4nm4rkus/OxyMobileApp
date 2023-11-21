package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class splashScreen extends AppCompatActivity {
    ConstraintLayout constraintLayout;
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

        constraintLayout = findViewById(R.id.splashScreen);

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
        termsCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTermsAlertDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if terms were previously accepted
        SharedPreferences preferences = getSharedPreferences("TermsPrefs", MODE_PRIVATE);
        boolean termsAccepted = preferences.getBoolean("TermsAccepted", false);

        if (!termsAccepted) {
            termsCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTermsAlertDialog();
                }
            });
        } else {
            updateUIAfterTermsAccepted();
        }
    }

    private void updateUIAfterTermsAccepted() {
        proceedButton.setEnabled(true);
        proceedButton.setAlpha(1f);
        termsCheck.setChecked(true);

        termsCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTermsAlertDialog();
            }
        });
    }

    private void showTermsAlertDialog() {
        SharedPreferences preferences = getSharedPreferences("TermsPrefs", MODE_PRIVATE);
        boolean termsAccepted = preferences.getBoolean("TermsAccepted", false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.customdialog, null);

        TextView textView = dialogView.findViewById(R.id.dialogText);
        textView.setText(getText(R.string.termsAndcon));

        ScrollView scrollView = dialogView.findViewById(R.id.scrollView);

        builder.setView(dialogView)
                .setTitle("Terms of Use and Conditions")
                .setPositiveButton("Accept", null)
                .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        termsCheck.setChecked(false);
                        proceedButton.setAlpha(0.5f);
                        proceedButton.setEnabled(false);
                        dialog.dismiss();
                        Toast.makeText(splashScreen.this, "Note: Terms of Use should be Accepted to continue.", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

                termsCheck.setChecked(termsAccepted);

                if (termsAccepted) {
                    positiveButton.setEnabled(true);
                    proceedButton.setEnabled(true);
                    proceedButton.setAlpha(1f);
                } else {
                    positiveButton.setEnabled(false);
                    proceedButton.setEnabled(false);
                    proceedButton.setAlpha(0.5f);
                }

                if (isScrolledToBottom(scrollView)) {
                    positiveButton.setEnabled(true);
                    positiveButton.setAlpha(1f);
                } else {
                    positiveButton.setEnabled(false);
                    positiveButton.setAlpha(0.7f);
                }

                scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (isScrolledToBottom(scrollView)) {
                            positiveButton.setEnabled(true);
                            positiveButton.setAlpha(1f);
                        }
                    }
                });

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isScrolledToBottom(scrollView)) {
                            proceedButton.setEnabled(true);
                            proceedButton.setAlpha(1f);
                            preferences.edit().putBoolean("TermsAccepted", true).apply();
                            alertDialog.dismiss();
                            termsCheck.setChecked(true);
                        } else {
                            Toast.makeText(splashScreen.this, "Make sure to read all the terms.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }


    // Function to check if scrolled to the bottom
    private boolean isScrolledToBottom(ScrollView scrollView) {
        int scrollY = scrollView.getScrollY();
        int scrollViewHeight = scrollView.getHeight();
        int contentHeight = scrollView.getChildAt(0).getHeight();

        return (scrollY + scrollViewHeight) >= contentHeight;
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
