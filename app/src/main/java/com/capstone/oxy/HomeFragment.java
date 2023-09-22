package com.capstone.oxy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class HomeFragment extends Fragment {

    private int CurrentProgress = 0;
    private CountDownTimer countDownTimer;
    private CircularProgressIndicator progressIndicator;
    private CardView progressIndicator_co_bg, progressIndicator_voc_bg, cardView_Aqi;
    private Button SanitizeBtn;
    private TextView Aqi_lvl_desc, Aqi_lvl_subdesc, Aqi_num, Co_num, Voc_num;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI elements
        progressIndicator_voc_bg = view.findViewById(R.id.cardView_Voc_bg);
        progressIndicator_co_bg = view.findViewById(R.id.cardView_Co_bg);

        progressIndicator = view.findViewById(R.id.aqi_progress_indicator);
        SanitizeBtn = view.findViewById(R.id.btn_sanitize);
        Aqi_lvl_desc = view.findViewById(R.id.aqiLevel_Description);
        Aqi_lvl_subdesc = view.findViewById(R.id.aqiLevel_sub_Description);
        cardView_Aqi = view.findViewById(R.id.cardView_Aqi);

        Aqi_num = view.findViewById(R.id.aqiNum);
        Co_num = view.findViewById(R.id.coNum);
        Voc_num = view.findViewById(R.id.vocNum);

        // Initialize and start the countdown timer, sample data for the Air Quality Circular Indicators
        countDownTimer = new CountDownTimer(30 * 1000, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
                CurrentProgress = CurrentProgress + 1;
                progressIndicator.setProgress(CurrentProgress);
                progressIndicator.setMax(500);

                Aqi_num.setText(String.valueOf(CurrentProgress));
                Co_num.setText(String.valueOf(CurrentProgress));
                Voc_num.setText(String.valueOf(CurrentProgress));

                if (CurrentProgress == 51) {
                    // Handle logic for CurrentProgress == 51
                    // Change UI elements accordingly
                    progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.yellow));
                    progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.yellow));
                    progressIndicator.setIndicatorColor(getResources().getColor(R.color.yellow));
                    Aqi_lvl_desc.setText(getString(R.string.Aqi_lvl_desc_moderate));
                    Aqi_lvl_desc.setTextColor(getResources().getColor(R.color.yellow));
                } else if (CurrentProgress == 101) {
                    // Handle logic for CurrentProgress == 101
                    // Cancel the timer, show a message, and start an activity
                    countDownTimer.cancel();
                    progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.orangeoxy));
                    progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.orangeoxy));
                    progressIndicator.setIndicatorColor(getResources().getColor(R.color.orangeoxy));
                    Aqi_lvl_desc.setTextSize(22);
                    Aqi_lvl_desc.setPadding(0, 0, 0, 20);
                    Aqi_lvl_subdesc.setVisibility(View.VISIBLE);
                    Aqi_lvl_desc.setText(getString(R.string.Aqi_lvl_desc_unhealthy1));
                    Aqi_lvl_desc.setTextColor(getResources().getColor(R.color.orangeoxy));

                    //When the Unhealthy level of Air Quality has been Reached, 101ppm
                    Toast.makeText(getActivity(), "Warning: Indoor air quality is currently unhealthy.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), activity_start_sanitation.class);
                    intent.putExtra("progressValue", CurrentProgress);
                    startActivity(intent);

                } else if (CurrentProgress == 151) {
                    // Change UI elements accordingly
                    progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.redoxy));
                    progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.redoxy));
                    progressIndicator.setIndicatorColor(getResources().getColor(R.color.redoxy));
                    Aqi_lvl_desc.setPadding(0, 0, 0, 0);
                    Aqi_lvl_desc.setTextSize(25);
                    Aqi_lvl_subdesc.setVisibility(View.GONE);
                    Aqi_lvl_desc.setText(getString(R.string.Aqi_lvl_desc_unhealthy2));
                    Aqi_lvl_desc.setTextColor(getResources().getColor(R.color.redoxy));
                } else if (CurrentProgress == 201) {
                    // Change UI elements accordingly
                    progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.purpleoxy));
                    progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.purpleoxy));
                    progressIndicator.setIndicatorColor(getResources().getColor(R.color.purpleoxy));
                    Aqi_lvl_desc.setTextSize(20);
                    Aqi_lvl_desc.setText(getString(R.string.Aqi_lvl_desc_veryunhealthy));
                    Aqi_lvl_desc.setTextColor(getResources().getColor(R.color.purpleoxy));
                } else if (CurrentProgress == 301) {
                    // Change UI elements accordingly
                    progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.oxybrown));
                    progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.oxybrown));
                    progressIndicator.setIndicatorColor(getResources().getColor(R.color.oxybrown));
                    Aqi_lvl_desc.setTextSize(25);
                    Aqi_lvl_desc.setText(getString(R.string.Aqi_lvl_desc_hazardous));
                    Aqi_lvl_desc.setTextColor(getResources().getColor(R.color.oxybrown));
                } else if (CurrentProgress == 500) {
                    countDownTimer.cancel();
                }
            }

            @Override
            public void onFinish() {
                stopTimer();
            }

            private void stopTimer() {
                countDownTimer.cancel();
            }
        };

        countDownTimer.start();

        //From Users decision, When the user needs to sanitize at any time
        SanitizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                Intent intent = new Intent(getActivity(), activity_start_sanitation.class);
                intent.putExtra("progressValue", CurrentProgress);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //Unnecessary, naglagay lang po ako to stop the timer to prevent overlapping or error when i'm checking my UI
        cardView_Aqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
            }
        });
        return view;
    }

    //I decided to pause the timer lang po kapag lumilipat ng fragments, kase di pa po naka set up na
    // kahit nasa ibang fragment or UI and then naabot yung unhealthy level automatically mag sasanitize
    @Override
    public void onPause() {
        super.onPause();
        // Stop the timer and release resources when leaving the fragment
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
