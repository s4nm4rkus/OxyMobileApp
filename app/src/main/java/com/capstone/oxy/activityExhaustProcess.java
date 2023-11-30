package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class activityExhaustProcess extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean animationRunning = true;
    private ImageButton imageHomeButton;
    private ConstraintLayout exhaustingLogoLayout;
    private TextView exhausting_Text, exhausting_duration;
    private String[] exhaust_Phrases;
    private CountDownTimer countDownTimer;
    private int currentPhraseIndex = 0;
    private ImageView exhaustFan;
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        // Set the layout for this activity
        setContentView(R.layout.activity_exhausting);

        // Set the status bar color to teal
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));

        // Make the navigation bar translucent
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

        // Initialize UI elements
        exhaustFan = findViewById(R.id.exhaust_fan);
        exhaustingLogoLayout = findViewById(R.id.exhausting_logo);
        exhausting_duration = findViewById(R.id.exhausting_time);
        exhausting_Text = findViewById(R.id.exhausting_text);
        imageHomeButton = findViewById(R.id.home_btn_onprocess);

        // Define phrases for text animation
        exhaust_Phrases = new String[] {
                getString(R.string.exhausting_text),
                getString(R.string.exhausting_text1),
                getString(R.string.exhausting_text2),
                getString(R.string.exhausting_text3)
        };

        // Show misting logo animation
        showMistingLogo();

        // Start a scale animation for a shadow logo
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
        countDownTimer = new CountDownTimer(2 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerUI(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                stopTimer();
                Toast.makeText(activityExhaustProcess.this, "Warning: The sanitation has started. Wait until it is done.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activityExhaustProcess.this, doneSanitation.class);
                startActivity(intent);
            }
        };

        homeViewModel.getExhaustStateLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String exhausStateValue) {
                if(exhausStateValue.equals("OFF")){
                    homeViewModel.setGlobalProcessEstateValue("OFF");
                    homeViewModel.setOnGoingProcessValue("NO");
                    Intent intent = new Intent(activityExhaustProcess.this, doneSanitation.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // Start the timer, text animation, scale animation, and rotation animation
        startTimer();
        startTextAnimation();
        scaleAnimator.start();
        startRotationAnimation();

        imageHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnHome();
            }
        });
    }

    @Override
    public void onBackPressed() {
        returnHome();
    }

    public void returnHome() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Background Process")
                .setMessage("Would you like the sanitization process to continue in the background?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(activityExhaustProcess.this, mainDashboard.class);
                        intent.putExtra("selectedRoom", "Room01");
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


    private void updateTimerUI(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        exhausting_duration.setText(timeLeftFormatted);
    }

    private void startTimer() {
        countDownTimer.start();
    }

    private void stopTimer() {
        countDownTimer.cancel();
    }

    private void showMistingLogo() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_up);
        exhaustingLogoLayout.startAnimation(animation);
        exhaustingLogoLayout.setVisibility(View.VISIBLE);
    }

    private void startRotationAnimation() {
        RotateAnimation rotate = new RotateAnimation(0, 360 * 5360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotate.setDuration(600000);
        rotate.setRepeatCount(Animation.INFINITE);
        exhaustFan.startAnimation(rotate);
    }

    private void startTextAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                exhausting_Text.setText(exhaust_Phrases[currentPhraseIndex]);
                currentPhraseIndex = (currentPhraseIndex + 1) % exhaust_Phrases.length;
                handler.postDelayed(this, 700);
            }
        }, 700);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exhaustFan.clearAnimation();
    }
}
