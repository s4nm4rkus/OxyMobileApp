package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class roomLoadingConnecting extends AppCompatActivity {

    private int CurrentProgress = 0;
    private CircularProgressIndicator connectingprogress;
    private TextView connectinglbl;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity
        setContentView(R.layout.connecting_screen);

        // Set the status bar color to teal
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));

        // Make the navigation bar translucent
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

        String selectedRoom = getIntent().getStringExtra("selectedRoom");

        // Initialize UI elements
        connectingprogress = findViewById(R.id.connecting_progress);
        connectinglbl = findViewById(R.id.connecting_label);

        CountDownTimer countDownTimer;// Update the current progress and set the maximum progress
// If the progress reaches 110, start the main_dashboard activity
// When the timer finishes, start the main_dashboard activity
        if (selectedRoom != null) {
            countDownTimer = new CountDownTimer(2 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // Update the current progress and set the maximum progress
                    CurrentProgress = CurrentProgress + 10;
                    connectingprogress.setMax(100);

                    if (CurrentProgress == 10) {
                        connectinglbl.setText(R.string.connecting);
                    } else if (CurrentProgress == 20) {
                        connectinglbl.setText(R.string.connecting1);
                    } else if (CurrentProgress == 30) {
                        connectinglbl.setText(R.string.connecting2);
                    } else if (CurrentProgress == 40) {
                        connectinglbl.setText(R.string.connecting3);
                    } else if (CurrentProgress == 50) {
                        connectinglbl.setText(R.string.connecting);
                    } else if (CurrentProgress == 60) {
                        connectinglbl.setText(R.string.connecting1);
                    } else if (CurrentProgress == 70) {
                        connectinglbl.setText(R.string.connecting2);
                    } else if (CurrentProgress == 80) {
                        connectinglbl.setText(R.string.connecting3);
                    } else if (CurrentProgress == 90) {
                        connectinglbl.setText(R.string.connecting);
                    } else if (CurrentProgress == 100) {
                        connectinglbl.setText(R.string.connecting1);
                    } else {
                        // If the progress reaches 110, start the main_dashboard activity
                        Intent intent = new Intent(roomLoadingConnecting.this, mainDashboard.class);
                        intent.putExtra("selectedRoom", "Room01");
                        startActivity(intent);
                    }
                }


                @Override
                public void onFinish() {
                    // When the timer finishes, start the main_dashboard activity
                    Intent intent = new Intent(roomLoadingConnecting.this, mainDashboard.class);
                    intent.putExtra("selectedRoom", "Room01");
                    startActivity(intent);
                }
            };

            // Start the countdown timer
        }
        else{
            countDownTimer = new CountDownTimer(2 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // Update the current progress and set the maximum progress
                    CurrentProgress = CurrentProgress + 10;
                    connectingprogress.setMax(100);

                    if (CurrentProgress == 10) {
                        connectinglbl.setText(R.string.connecting2r2);
                    } else if (CurrentProgress == 20) {
                        connectinglbl.setText(R.string.connecting1r2);
                    } else if (CurrentProgress == 30) {
                        connectinglbl.setText(R.string.connecting2r2);
                    } else if (CurrentProgress == 40) {
                        connectinglbl.setText(R.string.connecting3r2);
                    } else if (CurrentProgress == 50) {
                        connectinglbl.setText(R.string.connectingr2);
                    } else if (CurrentProgress == 60) {
                        connectinglbl.setText(R.string.connecting1r2);
                    } else if (CurrentProgress == 70) {
                        connectinglbl.setText(R.string.connecting2r2);
                    } else if (CurrentProgress == 80) {
                        connectinglbl.setText(R.string.connecting3r2);
                    } else if (CurrentProgress == 90) {
                        connectinglbl.setText(R.string.connectingr2);
                    } else if (CurrentProgress == 100) {
                        connectinglbl.setText(R.string.connecting1r2);
                    } else {
                        // If the progress reaches 110, start the main_dashboard activity
                        Intent intent = new Intent(roomLoadingConnecting.this, mainDashboard.class);
                        startActivity(intent);
                    }
                }


                @Override
                public void onFinish() {
                    // When the timer finishes, start the main_dashboard activity
                    Intent intent = new Intent(roomLoadingConnecting.this, mainDashboard.class);
                    startActivity(intent);
                }
            };

                // Start the countdown timer
        }
        countDownTimer.start();
    }
    }


