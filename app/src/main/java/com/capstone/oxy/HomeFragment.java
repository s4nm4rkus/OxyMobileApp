package com.capstone.oxy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.Objects;


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

        homeViewModel.getTankLevelLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long tankValue) {
                if(tankValue == 0){
                    SanitizeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Warning!")
                                    .setMessage("The sanitizer is empty. You should refill the sanitizer tank first.")
                                    .setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getActivity(), "Refill your tank to avoid inaccurate sanitation process.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .show();
                        }
                    });
                }else{
                    homeViewModel.getGlobalProcessEstateLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String globalState) {
                            if (globalState.equals("OFF")) {
                                SanitizeBtn.setElevation(8);
                                SanitizeBtn.setText("Sanitize");
                                SanitizeBtn.setTextSize(17);
                                SanitizeBtn.setTextColor(getResources().getColor(R.color.oxyblack));
                                SanitizeBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        confirmClick();
                                    }
                                });
                            } else if (globalState.equals("ON")) {
                                // Disable the button and display a message
                                SanitizeBtn.setElevation(0);
                                SanitizeBtn.setText("On Process...");
                                SanitizeBtn.setTextSize(16);
                                SanitizeBtn.setTextColor(getResources().getColor(R.color.grey));
                                SanitizeBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getActivity(), "The sanitation process is ongoing, please wait.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });


        return view;
    }

    private void confirmClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Start Sanitation")
                .setMessage("Would you like to start the manual sanitization process? You cannot undo this action.")
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        homeViewModel.setOnGoingProcessValue("YES");
                        homeViewModel.setGlobalProcessEstateValue("ON");
                        Intent intent = new Intent(getActivity(), activityInitialDelaySanitation.class);
                        startActivity(intent);
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
            Aqi_lvl_desc.setTextSize(25);
            Aqi_lvl_desc.setPadding(0, 0, 0, 0);
            Aqi_lvl_subdesc.setVisibility(View.GONE);
        }else if (value > 50 && value <= 100) {
            setAqiLevel(R.color.yellow, R.string.Aqi_lvl_desc_moderate);
            Aqi_lvl_desc.setTextSize(25);
            Aqi_lvl_desc.setPadding(0, 0, 0, 0);
            Aqi_lvl_subdesc.setVisibility(View.GONE);
        } else {
            homeViewModel.getTankLevelLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
                @Override
                public void onChanged(Long tankValue) {
                    if(tankValue <= 0){
                        homeViewModel.setGlobalProcessEstateValue("OFF");
                        homeViewModel.setOnGoingProcessValue("NO");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Warning!")
                                .setMessage("Cannot start the sanitation, the sanitizer is empty. You should refill the sanitizer tank first.")
                                .setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(), "Refill your tank to avoid inaccurate sanitation process.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                    }else{
                        homeViewModel.getIsOngoingProcessLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String isOngoing) {
                                if (isOngoing.equals("NO")){
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "Warning: Indoor air quality is currently unhealthy.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getActivity(), activityInitialDelaySanitation.class);
                                            intent.putExtra("progressValue", value);
                                            startActivity(intent);
                                        }
                                    }, 3000);
                                }
                            }
                        });
                    }
                }
            });


            if (value >= 101 && value <= 200) {

                setAqiLevel(R.color.orangeoxy, R.string.Aqi_lvl_desc_unhealthy1);
                Aqi_lvl_desc.setTextSize(22);
                Aqi_lvl_desc.setPadding(0, 0, 0, 20);
                Aqi_lvl_subdesc.setVisibility(View.VISIBLE);


            } else if (value > 200 && value <= 300) {
                setAqiLevel(R.color.redoxy, R.string.Aqi_lvl_desc_unhealthy2);
                Aqi_lvl_desc.setTextSize(25);
                Aqi_lvl_desc.setPadding(0, 0, 0, 0);
                Aqi_lvl_subdesc.setVisibility(View.GONE);
            } else if (value > 300 && value <= 400) {
                setAqiLevel(R.color.purpleoxy, R.string.Aqi_lvl_desc_veryunhealthy);
                Aqi_lvl_desc.setTextSize(22);
                Aqi_lvl_desc.setPadding(0, 0, 0, 0);
                Aqi_lvl_subdesc.setVisibility(View.GONE);
            } else if (value > 400) {
                setAqiLevel(R.color.oxybrown, R.string.Aqi_lvl_desc_hazardous);
                Aqi_lvl_desc.setTextSize(25);
                Aqi_lvl_desc.setPadding(0, 0, 0, 0);
                Aqi_lvl_subdesc.setVisibility(View.GONE);
            }

        }

    }

    private void setAqiLevel(int colorResource, int descriptionResource) {
        progressIndicator.setIndicatorColor(getResources().getColor(colorResource));
        Aqi_lvl_desc.setText(getString(descriptionResource));
        Aqi_lvl_desc.setTextColor(getResources().getColor(colorResource));
    }
}