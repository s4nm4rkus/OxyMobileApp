package com.capstone.oxy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TankFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView sanitizerLevel;
    private RelativeLayout liquidSanitizer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create an instance of the ViewModel using ViewModelProvider
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tank, container, false);

        sanitizerLevel = view.findViewById(R.id.sanitizer_percentage_label);
        liquidSanitizer = view.findViewById(R.id.liquid_sanitizer);

        homeViewModel.getTankLevelLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long tankLevel) {
                if (tankLevel != null) {
                    int percentage = calculatePercentage(tankLevel.intValue()); // Convert Long to int
                    updateSanitizerLevel(tankLevel.intValue()); // Convert Long to int
                }
            }
        });



        return view;
    }

    @SuppressLint("SetTextI18n")
    public void updateSanitizerLevel(int tankLevel) {
        sanitizerLevel.setText(tankLevel + "%");
        // Set the background color of liquidSanitizer based on the tank level
        if (tankLevel <= 15) {
            liquidSanitizer.setBackgroundResource(R.drawable.liquid_sanitizer_low);
            sanitizerLevel.setTextColor(getResources().getColor(R.color.redoxy));
        } else if (tankLevel <= 31) {
            liquidSanitizer.setBackgroundResource(R.drawable.liquid_sanitizer_mid);
            sanitizerLevel.setTextColor(getResources().getColor(R.color.yellow));
        } else if (tankLevel <= 51) {
            liquidSanitizer.setBackgroundResource(R.drawable.liquid_sanitizer);
            sanitizerLevel.setTextColor(getResources().getColor(R.color.sanitizer_lvl_blue));
        }

        // Set the height of liquidSanitizer to correspond to the tank level
        setLiquidSanitizerHeight(tankLevel);
    }

    private int calculatePercentage(int tankLevel) {
        // Define your known range for the tank level (e.g., 0-100)
        int minTankLevel = 0;
        int maxTankLevel = 100;

        // Calculate the percentage based on the range
        int percentage = (tankLevel - minTankLevel) * 100 / (maxTankLevel - minTankLevel);

        // Ensure the percentage is within the valid range (0-100)
        return Math.min(Math.max(percentage, 0), 100);
    }

    private void setLiquidSanitizerHeight(int tankLevel) {
        // Adjust the height of liquidSanitizer based on tank level
        int maxHeight = getResources().getDimensionPixelSize(R.dimen.liquid_sanitizer_max_height);
        int newHeight = (maxHeight * tankLevel) / 100;

        // Set the new height for liquidSanitizer
        ViewGroup.LayoutParams layoutParams = liquidSanitizer.getLayoutParams();
        layoutParams.height = newHeight;
        liquidSanitizer.setLayoutParams(layoutParams);
    }
}
