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
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

public class activityRoom2Misting extends AppCompatActivity {

    // Array of bubble view IDs to animate
    private static final int[] bubbleIds = {
            R.id.mist_bubble, R.id.mist_bubble9, R.id.mist_bubble10,
            R.id.mist_bubble11, R.id.mist_bubble12, R.id.mist_bubble13,
            R.id.mist_bubble2, R.id.mist_bubble3, R.id.mist_bubble4,
            R.id.mist_bubble5, R.id.mist_bubble6, R.id.mist_bubble8,
            R.id.mist_bubble14, R.id.mist_bubble15, R.id.mist_bubble7
    };

    private int currentBubbleIndex = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean animationRunning = true;
    private ConstraintLayout mistingLogoLayout;
    private TextView mistingText, misting_duration;
    private String[] mistingPhrases;
    private CountDownTimer countDownTimer;
    private int currentPhraseIndex = 0;
    private ImageButton imageHomeButton;
    HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misting);

        // Set the status bar color to teal
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));

        // Make the navigation bar translucent
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Initialize UI elements
        imageHomeButton = findViewById(R.id.home_btn_onprocess);
        mistingLogoLayout = findViewById(R.id.misting_logo);
        misting_duration = findViewById(R.id.misting_time);
        mistingText = findViewById(R.id.misting_text);

        // Define phrases for text animation
        mistingPhrases = new String[] {
                getString(R.string.misting_text),
                getString(R.string.misting_text1),
                getString(R.string.misting_text2),
                getString(R.string.misting_text3)
        };

        // Start bubble animation
        startBubbleAnimation();

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

        countDownTimer = new CountDownTimer(2 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerUI(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                stopTimer();
            }
        };

        homeViewModel.getSoakingRoom2StateLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String soakingState) {
                if(soakingState.equals("ON")){
                    Toast.makeText(activityRoom2Misting.this, "Warning: The sanitation has started. Wait until it is done.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activityRoom2Misting.this, activityRoom2SoakingProcess.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

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
                        Intent intent = new Intent(activityRoom2Misting.this, mainDashboard.class);

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
        misting_duration.setText(timeLeftFormatted);
    }

    private void startTimer() {
        countDownTimer.start();
    }

    private void stopTimer() {
        countDownTimer.cancel();

    }

    private void startBubbleAnimation() {
        final int animationDelayMillis = 100;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animationRunning) {
                    ImageView currentBubble = findViewById(bubbleIds[currentBubbleIndex]);
                    if (currentBubble != null) {
                        if (currentBubble.getVisibility() == View.GONE) {
                            currentBubble.setVisibility(View.VISIBLE);
                        } else {
                            currentBubble.setVisibility(View.GONE);
                        }
                    }

                    currentBubbleIndex = (currentBubbleIndex + 1) % bubbleIds.length;

                    handler.postDelayed(this, animationDelayMillis);
                }
            }
        }, animationDelayMillis);
    }

    private void showMistingLogo() {
        // Load and start an animation for the misting logo
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_up);
        mistingLogoLayout.startAnimation(animation);
        mistingLogoLayout.setVisibility(View.VISIBLE);
    }

    private void startTextAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mistingText.setText(mistingPhrases[currentPhraseIndex]);

                currentPhraseIndex = (currentPhraseIndex + 1) % mistingPhrases.length;

                handler.postDelayed(this, 700);
            }
        }, 700);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animationRunning = false;
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
