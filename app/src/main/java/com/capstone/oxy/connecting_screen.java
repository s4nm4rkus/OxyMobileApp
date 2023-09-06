package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class connecting_screen extends AppCompatActivity {

    private int CurrentProgress = 0;
    private CircularProgressIndicator connectingprogress;
    private TextView connectinglbl;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connecting_screen);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealsecondary));

        connectingprogress = findViewById(R.id.connecting_progress);
        connectinglbl = findViewById(R.id.connecting_label);

        CountDownTimer countDownTimer = new CountDownTimer(8*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                CurrentProgress = CurrentProgress + 10;
                connectingprogress.setMax(100);

                if (CurrentProgress == 10) {
                    connectinglbl.setText(R.string.connecting);
                }
                else if (CurrentProgress == 20) {
                    connectinglbl.setText(R.string.connecting1);
                }
                else if (CurrentProgress == 30) {
                    connectinglbl.setText(R.string.connecting2);
                }
                else if (CurrentProgress == 40) {
                    connectinglbl.setText(R.string.connecting3);
                }
                else if (CurrentProgress == 50) {
                    connectinglbl.setText(R.string.connecting);
                }
                else if (CurrentProgress == 60) {
                    connectinglbl.setText(R.string.connecting1);
                }
                else if (CurrentProgress == 70) {
                    connectinglbl.setText(R.string.connecting2);
                }
                else if (CurrentProgress == 80) {
                    connectinglbl.setText(R.string.connecting3);
                }
                else if (CurrentProgress == 90) {
                    connectinglbl.setText(R.string.connecting);
                }
                else if (CurrentProgress == 100) {
                    connectinglbl.setText(R.string.connecting1);
                }
                else {
                    Intent intent = new Intent(connecting_screen.this, dashboard_room101_home.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(connecting_screen.this, dashboard_room101_home.class);
                startActivity(intent);
            }
        };
        countDownTimer.start();
    }
}