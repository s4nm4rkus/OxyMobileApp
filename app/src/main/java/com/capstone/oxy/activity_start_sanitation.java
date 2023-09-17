package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

public class activity_start_sanitation extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    CircularProgressIndicator timer_indicator;
    ImageView warning_sign;
    TextView description_lvl, sub_description, proceed_sanitation_btn, countdown_before_sanitation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_sanitation);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealsecondary));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        timer_indicator = findViewById(R.id.circular_timer_indicator);
        warning_sign = findViewById(R.id.warning_sign);
        description_lvl = findViewById(R.id.description_lvl);
        sub_description = findViewById(R.id.sub_description);
        proceed_sanitation_btn = findViewById(R.id.proceed_sanitation_btn);
        countdown_before_sanitation = findViewById(R.id.time_interval_before);

        Intent intent = getIntent();
        int progressValue = intent.getIntExtra("progressValue", 0);

        if (progressValue >= 0 && progressValue <=50){
            warning_sign.setImageResource(R.drawable.warnig_green);
            description_lvl.setText(getResources().getText(R.string.Aqi_lvl_desc));
            description_lvl.setTextColor(getResources().getColor(R.color.tealmain));
            sub_description.setTextColor(getResources().getColor(R.color.tealmain));
        }
        else if (progressValue >= 51 && progressValue <= 100){
            warning_sign.setImageResource(R.drawable.warnig_yellow);
            description_lvl.setText(getResources().getText(R.string.Aqi_lvl_desc_moderate));
            description_lvl.setTextColor(getResources().getColor(R.color.yellow));
            sub_description.setTextColor(getResources().getColor(R.color.yellow));
        }
        else  if (progressValue >= 101 && progressValue <=150) {
            warning_sign.setImageResource(R.drawable.warnig_orange);
            description_lvl.setText(getResources().getText(R.string.Aqi_lvl_desc_unhealthy1));
            description_lvl.setTextColor(getResources().getColor(R.color.orangeoxy));
            sub_description.setTextColor(getResources().getColor(R.color.orangeoxy));
        }
        else if (progressValue >= 151 && progressValue <= 200){
            warning_sign.setImageResource(R.drawable.warnig_red);
            description_lvl.setText(getResources().getText(R.string.Aqi_lvl_desc_unhealthy2));
            description_lvl.setTextColor(getResources().getColor(R.color.redoxy));
            sub_description.setTextColor(getResources().getColor(R.color.redoxy));
        }
        else if (progressValue >= 201 && progressValue <= 300){
            warning_sign.setImageResource(R.drawable.warnig_purple);
            description_lvl.setText(getResources().getText(R.string.Aqi_lvl_desc_veryunhealthy));
            description_lvl.setTextColor(getResources().getColor(R.color.purpleoxy));
            sub_description.setTextColor(getResources().getColor(R.color.purpleoxy));
        }
        else if (progressValue >= 301){
            warning_sign.setImageResource(R.drawable.warnig_brown);
            description_lvl.setText(getResources().getText(R.string.Aqi_lvl_desc_hazardous));
            description_lvl.setTextColor(getResources().getColor(R.color.oxybrown));
            sub_description.setTextColor(getResources().getColor(R.color.oxybrown));
        }



        countDownTimer = new CountDownTimer(1 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerUI(millisUntilFinished);

                int progress = (int) (millisUntilFinished / (1 * 60 * 1000.0) * 100.0);

                timer_indicator.setProgress(progress);
            }

            @Override
            public void onFinish() {
                stopTimer();
                Toast.makeText(activity_start_sanitation.this, "Warning: The sanitation has started. Wait until it is done.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity_start_sanitation.this, activity_misting.class);
                startActivity(intent);
            }
        };

        proceed_sanitation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_start_sanitation.this, activity_misting.class);
                startActivity(intent);
                countDownTimer.cancel();
            }
        });

        startTimer();
    }

    private void startTimer() {
        countDownTimer.start();
    }

    private void updateTimerUI(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        countdown_before_sanitation.setText(timeLeftFormatted);

    }


    private void stopTimer() {
        countDownTimer.cancel();
    }

}
