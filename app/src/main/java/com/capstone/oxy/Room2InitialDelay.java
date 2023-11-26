package com.capstone.oxy;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.content.ContextCompat;
        import androidx.lifecycle.LifecycleOwner;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProvider;

        import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.material.progressindicator.CircularProgressIndicator;

public class Room2InitialDelay extends AppCompatActivity {

    private HomeViewModel homeViewModel;
    private CountDownTimer countDownTimer;
    CircularProgressIndicator timer_indicator;
    ImageView warning_sign;
    TextView description_lvl, sub_description, proceed_sanitation_btn, countdown_before_sanitation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Set the layout for this activity
        setContentView(R.layout.activity_initial_delay_sanitation);

        // Set the status bar color to teal
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));

        // Make the navigation bar translucent
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

        // Initialize UI elements
        timer_indicator = findViewById(R.id.circular_timer_indicator);
        warning_sign = findViewById(R.id.warning_sign);
        description_lvl = findViewById(R.id.description_lvl);
        sub_description = findViewById(R.id.sub_description);
        proceed_sanitation_btn = findViewById(R.id.proceed_sanitation_btn);
        countdown_before_sanitation = findViewById(R.id.time_interval_before);

        // Retrieve progressValue from the intent
        Intent intent = getIntent();
        int progressValue = intent.getIntExtra("progressValue", 0);

        // Set warning sign and description based on progressValue
        if (progressValue >= 0 && progressValue <= 50) {
            // Set resources for a healthy range
            warning_sign.setImageResource(R.drawable.warnig_green);
            description_lvl.setText(getResources().getText(R.string.Aqi_lvl_desc));
            description_lvl.setTextColor(getResources().getColor(R.color.tealmain));
            sub_description.setTextColor(getResources().getColor(R.color.tealmain));
        } else if (progressValue >= 51 && progressValue <= 100) {
            // Set resources for a moderate range
            warning_sign.setImageResource(R.drawable.warnig_yellow);
            description_lvl.setText(getResources().getText(R.string.Aqi_lvl_desc_moderate));
            description_lvl.setTextColor(getResources().getColor(R.color.yellow));
            sub_description.setTextColor(getResources().getColor(R.color.yellow));
        } else if (progressValue >= 101 && progressValue <= 150) {
            // Set resources for an unhealthy range (level 1)
            warning_sign.setImageResource(R.drawable.warnig_orange);
            description_lvl.setText(getResources().getText(R.string.Aqi_lvl_desc_unhealthy1));
            description_lvl.setTextColor(getResources().getColor(R.color.orangeoxy));
            sub_description.setTextColor(getResources().getColor(R.color.orangeoxy));
        } else if (progressValue >= 151 && progressValue <= 200) {
            // Set resources for an unhealthy range (level 2)
            warning_sign.setImageResource(R.drawable.warnig_red);
            description_lvl.setText(getResources().getText(R.string.Aqi_lvl_desc_unhealthy2));
            description_lvl.setTextColor(getResources().getColor(R.color.redoxy));
            sub_description.setTextColor(getResources().getColor(R.color.redoxy));
        } else if (progressValue >= 201 && progressValue <= 300) {
            // Set resources for a very unhealthy range
            warning_sign.setImageResource(R.drawable.warnig_purple);
            description_lvl.setText(getResources().getText(R.string.Aqi_lvl_desc_veryunhealthy));
            description_lvl.setTextColor(getResources().getColor(R.color.purpleoxy));
            sub_description.setTextColor(getResources().getColor(R.color.purpleoxy));
        } else if (progressValue >= 301) {
            // Set resources for a hazardous range
            warning_sign.setImageResource(R.drawable.warnig_brown);
            description_lvl.setText(getResources().getText(R.string.Aqi_lvl_desc_hazardous));
            description_lvl.setTextColor(getResources().getColor(R.color.oxybrown));
            sub_description.setTextColor(getResources().getColor(R.color.oxybrown));
        }

        // Initialize and start a countdown timer
        countDownTimer = new CountDownTimer(2 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerUI(millisUntilFinished);

                int progress = (int) (100 * millisUntilFinished / (2 * 60 * 1000));

                timer_indicator.setProgress(progress);
            }

            @Override
            public void onFinish() {
                stopTimer();
                Toast.makeText(Room2InitialDelay.this, "Warning: The sanitation has started. Wait until it is done.", Toast.LENGTH_SHORT).show();
            }
        };


        homeViewModel.getMistingSanitationLiveDataRoom2().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String MistingState) {
                    if (MistingState.equals("ON")) {
                        Intent intent = new Intent(Room2InitialDelay.this, activityRoom2Misting.class);
                        startActivity(intent);
                        finish();
                    }
                }
             });

        proceed_sanitation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.setOnGoingProcessValueRoom2("YES");
                homeViewModel.setGlobalProcessEstateRoom2Value("ON");
                homeViewModel.setInitialDelayRoom2Value("OFF");
                homeViewModel.setMistingSanitationValueRoom2("ON");
                Toast.makeText(Room2InitialDelay.this, "Warning: The sanitation has started. Wait until it is done.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Room2InitialDelay.this, activityRoom2Misting.class);
                startActivity(intent);
                countDownTimer.cancel();
                finish();
            }
        });
        // Start the countdown timer
        startTimer();
    }

    // Method to start the countdown timer
    private void startTimer() {
        countDownTimer.start();
    }

    // Method to update the timer UI
    private void updateTimerUI(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        countdown_before_sanitation.setText(timeLeftFormatted);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(Room2InitialDelay.this, "You are unable to return from this state.", Toast.LENGTH_SHORT).show();
    }

    // Method to stop the countdown timer
    private void stopTimer() {
        countDownTimer.cancel();
    }

    // Finish the activity when paused
    protected void onPause() {
        super.onPause();
        finish();
    }
}
