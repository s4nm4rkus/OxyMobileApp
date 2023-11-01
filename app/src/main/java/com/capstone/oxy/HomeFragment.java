package com.capstone.oxy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private CircularProgressIndicator progressIndicator;
    private CardView progressIndicator_co_bg, progressIndicator_voc_bg, cardView_Aqi;
    private Button SanitizeBtn;
    private TextView Aqi_lvl_desc, Aqi_lvl_subdesc, Aqi_num, Co_num, Voc_num;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an instance of the ViewModel using ViewModelProvider
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
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

        homeViewModel.getCoLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long sensorValue) {
                if (sensorValue != null) {
                    Co_num.setText(String.valueOf(sensorValue));
                    updateAQI();

                    try {
                        int coValue = Integer.parseInt(String.valueOf(sensorValue));
                        if (coValue >= 0 && coValue <= 50) {
                            progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.tealmain));
                        } else if (coValue > 50 && coValue <= 100) {
                            progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.yellow));
                        } else if (coValue > 100 && coValue <= 200) {
                            progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.orangeoxy));
                        } else if (coValue > 200 && coValue <= 300) {
                            progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.redoxy));
                        } else if (coValue > 300 && coValue <= 400) {
                            progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.purpleoxy));
                        } else if (coValue > 400) {
                            progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.oxybrown));
                        }

                    } catch (NumberFormatException e) {
                        Log.e("Realtime Database", "Invalid sensor value format.");
                        progressIndicator_co_bg.setCardBackgroundColor(getResources().getColor(R.color.lightgrey)); // Default color
                    }
                } else {
                    Log.e("Realtime Database", "Error on fetching data.");
                }

            }
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Realtime Database", "Failed to read CO sensor data.", error.toException());
            }
        });

        homeViewModel.getTvocLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long sensorValue) {
                if (sensorValue != null) {
                    Voc_num.setText(String.valueOf(sensorValue));
                    updateAQI();

                    try {
                        int tvocValue = Integer.parseInt(String.valueOf(sensorValue));
                        if (tvocValue >= 0 && tvocValue <= 50) {
                            progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.tealmain));
                        } else if (tvocValue > 50 && tvocValue < 100) {
                            progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.yellow));
                        } else if (tvocValue > 100 && tvocValue <= 200) {
                            progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.orangeoxy));
                        } else if (tvocValue > 200 && tvocValue <= 300) {
                            progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.redoxy));
                        } else if (tvocValue > 300 && tvocValue <= 400) {
                            progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.purpleoxy));
                        } else if (tvocValue > 400) {
                            progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.oxybrown));
                        }

                    } catch (NumberFormatException e) {
                        Log.e("Realtime Database", "Invalid sensor value format.");
                        progressIndicator_voc_bg.setCardBackgroundColor(getResources().getColor(R.color.lightgrey)); // Default color
                    }
                }else{
                    Log.e("Realtime Database", "Error on fetching data.");
                }

            }
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Realtime Database", "Failed to read CO sensor data.", error.toException());
            }
        });

        updateAQI();

        //From Users decision, When the user needs to sanitize at any time
        SanitizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), activity_start_sanitation.class);
                startActivity(intent);
            }
        });

        //Unnecessary, naglagay lang po ako to stop the timer to prevent overlapping or error when i'm checking my UI
        cardView_Aqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void updateAQI() {
        int coValue = 0;
        int tvocValue = 0;
        progressIndicator.setMax(500);

        try {
            coValue = Integer.parseInt(Co_num.getText().toString());
            tvocValue = Integer.parseInt(Voc_num.getText().toString());

        } catch (NumberFormatException e){
            Log.e("Realtime Database", "Invalid sensor value format.");
        }

        if (coValue > tvocValue) {
            setAqiValues(coValue);
        } else {
            setAqiValues(tvocValue);
        }

    }

    private void setAqiValues(int value) {
        Aqi_num.setText(String.valueOf(value));
        progressIndicator.setProgress(value);

        if (value >= 0 && value <=50){
            setAqiLevel(R.color.tealmain, R.string.Aqi_lvl_desc);
        }else if (value > 50 && value <= 100) {
            setAqiLevel(R.color.yellow, R.string.Aqi_lvl_desc_moderate);
        } else if (value >= 101 && value <= 200) {
            setAqiLevel(R.color.orangeoxy, R.string.Aqi_lvl_desc_unhealthy1);
            // Add the Toast and Intent code here
            Aqi_lvl_desc.setTextSize(22);
            Aqi_lvl_desc.setPadding(0, 0, 0, 20);
            Aqi_lvl_subdesc.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Warning: Indoor air quality is currently unhealthy.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), activity_start_sanitation.class);
            intent.putExtra("progressValue", value);
            startActivity(intent);

        } else if (value > 200 && value <= 300) {
            setAqiLevel(R.color.redoxy, R.string.Aqi_lvl_desc_unhealthy2);
        } else if (value > 300 && value <= 400) {
            setAqiLevel(R.color.purpleoxy, R.string.Aqi_lvl_desc_veryunhealthy);
        } else if (value > 400) {
            setAqiLevel(R.color.oxybrown, R.string.Aqi_lvl_desc_hazardous);
        }

    }

    private void setAqiLevel(int colorResource, int descriptionResource) {
        progressIndicator.setIndicatorColor(getResources().getColor(colorResource));
        Aqi_lvl_desc.setText(getString(descriptionResource));
        Aqi_lvl_desc.setTextColor(getResources().getColor(colorResource));
    }
}