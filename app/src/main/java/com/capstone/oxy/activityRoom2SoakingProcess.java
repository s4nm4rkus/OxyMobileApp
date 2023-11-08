package com.capstone.oxy;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.constraintlayout.widget.ConstraintLayout;
        import androidx.core.content.ContextCompat;

        import android.animation.ObjectAnimator;
        import android.animation.PropertyValuesHolder;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.os.Handler;
        import android.os.Looper;
        import android.view.View;
        import android.view.WindowManager;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

public class activityRoom2SoakingProcess extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean animationRunning = true;
    private ConstraintLayout soakingLogoLayout;
    private TextView soaking_Text, soaking_duration;
    private String[] soaking_Phrases;
    private CountDownTimer countDownTimer;
    private int currentPhraseIndex = 0;
    private ImageButton imageHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity
        setContentView(R.layout.activity_soaking);

        // Set the status bar color to teal
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));

        // Make the navigation bar translucent
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

        // Initialize UI elements
        imageHomeButton = findViewById(R.id.home_btn_onprocess);
        soakingLogoLayout = findViewById(R.id.soaking_logo);
        soaking_duration = findViewById(R.id.soaking_time);
        soaking_Text = findViewById(R.id.soaking_text);

        // Define phrases for text animation
        soaking_Phrases = new String[] {
                getString(R.string.soaking_text),
                getString(R.string.soaking_text1),
                getString(R.string.soaking_text2),
                getString(R.string.soaking_text3)
        };

        // Show misting logo animation
        showMistingLogo();

        // Initialize a scale animation for a shadow logo
        ImageView shadowLogo = findViewById(R.id.shadow_logo);
        ObjectAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(
                shadowLogo,
                PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.8f),
                PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.8f)
        );
        scaleAnimator.setDuration(1000);
        scaleAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scaleAnimator.setRepeatMode(ObjectAnimator.REVERSE);

        // Initialize and start a countdown timer
        countDownTimer = new CountDownTimer(10 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerUI(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                stopTimer();
                // Show a toast message when the timer finishes
                Toast.makeText(activityRoom2SoakingProcess.this, "Warning: The sanitation has started. Wait until it is done.", Toast.LENGTH_SHORT).show();
                // Navigate to the activity_exhausting activity
                Intent intent = new Intent(activityRoom2SoakingProcess.this, activityRoom2ExhaustProcess.class);
                startActivity(intent);
            }
        };

        // Start the timer, scale animation, and text animation
        startTimer();
        scaleAnimator.start();
        startTextAnimation();

        imageHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnHome();
            }
        });
    }

    private void updateTimerUI(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        soaking_duration.setText(timeLeftFormatted);
    }

    private void startTimer() {
        countDownTimer.start();
    }

    private void stopTimer() {
        countDownTimer.cancel();
    }

    private void showMistingLogo() {
        // Load and start an animation for the misting logo
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_up);
        soakingLogoLayout.startAnimation(animation);
        soakingLogoLayout.setVisibility(View.VISIBLE);
    }

    private void startTextAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                soaking_Text.setText(soaking_Phrases[currentPhraseIndex]);

                currentPhraseIndex = (currentPhraseIndex + 1) % soaking_Phrases.length;

                handler.postDelayed(this, 700);
            }
        }, 700);
    }

    @Override
    public void onBackPressed() {
        returnHome();
    }

    public void returnHome() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Confirmation")
                .setMessage("Would you like the sanitization process to continue in the background?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(activityRoom2SoakingProcess.this, mainDashboard.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    // Finish the activity when paused
    protected void onPause() {
        super.onPause();
        finish();
    }
}
