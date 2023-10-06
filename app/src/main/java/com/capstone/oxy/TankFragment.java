package com.capstone.oxy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class TankFragment extends Fragment {
    private TextView sanitizerLevel;
    private int sanitizerLevelPercentage = 0;
    private Handler handler;
    private int totalTimeMillis = 100000; // Total time for the sanitizer level to reach 100%
    private int updateIntervalMillis = 1000; // Update interval in milliseconds
    private RelativeLayout liquidSanitizer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tank, container, false);
        sanitizerLevel = view.findViewById(R.id.sanitizer_percentage_label);
        liquidSanitizer = view.findViewById(R.id.liquid_sanitizer);
        handler = new Handler();
        updateSanitizerLevelContinuously();
        return view;
    }

    public void updateSanitizerLevel(int percentage) {
        if (sanitizerLevel != null) {
            sanitizerLevel.setText(percentage + "%");
        }
        // Update the height of liquid_sanitizer based on the percentage
        updateLiquidSanitizerHeight(percentage);

        if (percentage <= 20) {
            // Set the background color of liquidSanitizer to red
            liquidSanitizer.setBackgroundResource(R.drawable.liquid_sanitizer_low);
            sanitizerLevel.setTextColor(getResources().getColor(R.color.redoxy));
        } else {
            // Set the background color of liquidSanitizer to the default
            liquidSanitizer.setBackgroundResource(R.drawable.liquid_sanitizer);
            sanitizerLevel.setTextColor(getResources().getColor(R.color.sanitizer_lvl_blue));
        }
    }

    private void updateLiquidSanitizerHeight(int percentage) {
        // Calculate the new height based on the percentage
        int maxHeight = getResources().getDimensionPixelSize(R.dimen.liquid_sanitizer_max_height);
        int newHeight = (maxHeight * percentage) / 100;

        // Set the new height for liquid_sanitizer
        ViewGroup.LayoutParams layoutParams = liquidSanitizer.getLayoutParams();
        layoutParams.height = newHeight;
        liquidSanitizer.setLayoutParams(layoutParams);
    }

    private void updateSanitizerLevelContinuously() {
        // Define the total time (e.g., 100 seconds) for the sanitizer level to reach 100%
        final long startTimeMillis = System.currentTimeMillis();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Calculate the sanitizer level as a percentage
                long currentTimeMillis = System.currentTimeMillis();
                long elapsedTimeMillis = currentTimeMillis - startTimeMillis;

                int newSanitizerPercentage = (int) (((float) elapsedTimeMillis / totalTimeMillis) * 1000);

                // Ensure the percentage does not exceed 100%
                if (newSanitizerPercentage > 100) {
                    newSanitizerPercentage = 100;
                }

                // Update the sanitizer level only if it changes
                if (newSanitizerPercentage != sanitizerLevelPercentage) {
                    sanitizerLevelPercentage = newSanitizerPercentage;
                    updateSanitizerLevel(sanitizerLevelPercentage);
                }

                // Continue updating every second until 100% is reached
                if (sanitizerLevelPercentage < 100) {
                    handler.postDelayed(this, updateIntervalMillis);
                }
            }
        }, updateIntervalMillis);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);

    }
}
