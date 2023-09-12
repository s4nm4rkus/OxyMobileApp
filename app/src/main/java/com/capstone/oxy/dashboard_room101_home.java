package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class dashboard_room101_home extends AppCompatActivity {

        private int CurrentProgress = 0;
        private CircularProgressIndicator progressIndicator,progressIndicator_voc;
        private CardView progressIndicator_co_bg, progressIndicator_voc_bg;
        private Button SanitizeBtn;
        private TextView Aqi_lvl_desc, Aqi_lvl_subdesc, Aqi_num, Co_num, Voc_num;
        ImageButton switch_room_btn;
        LinearLayout btn_sanitize_back;
        Dialog switchpopupDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_room101_home);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealsecondary));
        progressIndicator_voc_bg = findViewById(R.id.cardView_Voc_bg);
        progressIndicator_co_bg = findViewById(R.id.cardView_Co_bg);

        progressIndicator = findViewById(R.id.aqi_progress_indicator);
        SanitizeBtn = findViewById(R.id.btn_sanitize);
        Aqi_lvl_desc = findViewById(R.id.aqiLevel_Description);
        Aqi_lvl_subdesc = findViewById(R.id.aqiLevel_sub_Description);

        Aqi_num = findViewById(R.id.aqiNum);
        Co_num = findViewById(R.id.coNum);
        Voc_num = findViewById(R.id.vocNum);

        CountDownTimer countDownTimer = new CountDownTimer(100*1000,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                CurrentProgress = CurrentProgress + 1;
                progressIndicator.setProgress(CurrentProgress);
                progressIndicator.setMax(500);

                Aqi_num.setText(String.valueOf(CurrentProgress));
                Co_num.setText(String.valueOf(CurrentProgress));
                Voc_num.setText(String.valueOf(CurrentProgress));

                if (CurrentProgress == 51){
                    progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.yellow));
                    progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.yellow));
                    progressIndicator.setIndicatorColor(getResources().getColor(R.color.yellow));
                    Aqi_lvl_desc.setText(getString(R.string.Aqi_lvl_desc_moderate));
                    Aqi_lvl_desc.setTextColor(getResources().getColor(R.color.yellow));
                }
                else if (CurrentProgress == 101 ){
                    progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.orangeoxy));
                    progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.orangeoxy));
                    progressIndicator.setIndicatorColor(getResources().getColor(R.color.orangeoxy));
                    Aqi_lvl_desc.setTextSize(22);
                    Aqi_lvl_desc.setPadding(0,0,0,20);
                    Aqi_lvl_subdesc.setVisibility(View.VISIBLE);
                    Aqi_lvl_desc.setText(getString(R.string.Aqi_lvl_desc_unhealthy1));
                    Aqi_lvl_desc.setTextColor(getResources().getColor(R.color.orangeoxy));
                }
                else if (CurrentProgress == 151 ){
                    progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.redoxy));
                    progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.redoxy));
                    progressIndicator.setIndicatorColor(getResources().getColor(R.color.redoxy));
                    Aqi_lvl_desc.setPadding(0,0,0,0);
                    Aqi_lvl_desc.setTextSize(25);
                    Aqi_lvl_subdesc.setVisibility(View.GONE);
                    Aqi_lvl_desc.setText(getString(R.string.Aqi_lvl_desc_unhealthy2));
                    Aqi_lvl_desc.setTextColor(getResources().getColor(R.color.redoxy));
                }
                else if (CurrentProgress == 201 ){
                    progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.purpleoxy));
                    progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.purpleoxy));
                    progressIndicator.setIndicatorColor(getResources().getColor(R.color.purpleoxy));
                    Aqi_lvl_desc.setTextSize(20);
                    Aqi_lvl_desc.setText(getString(R.string.Aqi_lvl_desc_veryunhealthy));
                    Aqi_lvl_desc.setTextColor(getResources().getColor(R.color.purpleoxy));
                }
                else if (CurrentProgress == 301){
                    progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.oxybrown));
                    progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.oxybrown));
                    progressIndicator.setIndicatorColor(getResources().getColor(R.color.oxybrown));
                    Aqi_lvl_desc.setTextSize(25);
                    Aqi_lvl_desc.setText(getString(R.string.Aqi_lvl_desc_hazardous));
                    Aqi_lvl_desc.setTextColor(getResources().getColor(R.color.oxybrown));
                }
                else if (CurrentProgress == 500){
                    Intent intent = new Intent(dashboard_room101_home.this, dashboard_room101_home.class);
                    startActivity(intent);
                }

            }
            @Override
            public void onFinish() {

            }
        };

        SanitizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.start();

            }
        });

        switchpopupDialog = new Dialog(this);
    }
    public void switch_room_btn (View v){
        ImageButton icClose;
        TextView switchRoomBtn;
        switchpopupDialog.setContentView(R.layout.changeroom_popup);

        icClose = (ImageButton) switchpopupDialog.findViewById(R.id.closePopup);
        switchRoomBtn = (TextView) switchpopupDialog.findViewById(R.id.leaveRoom);

        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchpopupDialog.dismiss();
            }
        });
        switchpopupDialog.show();
    }
}