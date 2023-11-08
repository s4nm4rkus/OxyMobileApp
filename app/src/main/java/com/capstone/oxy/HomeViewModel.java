package com.capstone.oxy;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeViewModel extends ViewModel {

    private final DatabaseReference sensorDataCO, room2CO;
    private final DatabaseReference sensorDataTVOC, room2VOC;
    private final DatabaseReference sensorDataTank, room2Tank;
    private final DatabaseReference isOngoingProcess, isOngoingProcessRoom2;
    private final DatabaseReference initialDelay, mistingSanitation, initialDelayRoom2, mistingSanitationRoom2;
    private final DatabaseReference globalProcessEstate, globalProcessEstateRoom2;

    private MutableLiveData<Long> coLiveData = new MutableLiveData<>();
    private MutableLiveData<Long> tvocLiveData = new MutableLiveData<>();
    private final MutableLiveData<Long> tankLevelLiveData = new MutableLiveData<>();

    private MutableLiveData<Long> coLiveData2 = new MutableLiveData<>();
    private MutableLiveData<Long> tvocLiveData2 = new MutableLiveData<>();
    private final MutableLiveData<Long> tankLevelLiveData2 = new MutableLiveData<>();

    private MutableLiveData<String> globalProcessEstateLiveData = new MutableLiveData<>();
    private MutableLiveData<String> globalProcessEstateLiveDataRoom2 = new MutableLiveData<>();

    public HomeViewModel() {
        //room01
        sensorDataCO = FirebaseDatabase.getInstance().getReference("Room_1").child("CO");
        sensorDataTVOC = FirebaseDatabase.getInstance().getReference("Room_1").child("TVOC");
        sensorDataTank = FirebaseDatabase.getInstance().getReference("Room_1").child("TANK-LEVEL");

        room2CO = FirebaseDatabase.getInstance().getReference("Room_2").child("CO");
        room2VOC = FirebaseDatabase.getInstance().getReference("Room_2").child("TVOC");
        room2Tank = FirebaseDatabase.getInstance().getReference("Room_2").child("TANK-LEVEL");

        isOngoingProcess = FirebaseDatabase.getInstance().getReference("Room_1").child("isOngoing");
        isOngoingProcessRoom2 = FirebaseDatabase.getInstance().getReference("Room_2").child("isOngoing");

        initialDelay = FirebaseDatabase.getInstance().getReference("Room_1").child("EXEC-PROCESS").child("INITIAL-DELAY-STATE");
        mistingSanitation = FirebaseDatabase.getInstance().getReference("Room_1").child("EXEC-PROCESS").child("ATOM-SANI-STATE");

        initialDelayRoom2 = FirebaseDatabase.getInstance().getReference("Room_2").child("EXEC-PROCESS").child("INITIAL-DELAY-STATE");
        mistingSanitationRoom2 = FirebaseDatabase.getInstance().getReference("Room_2").child("EXEC-PROCESS").child("ATOM-SANI-STATE");

        globalProcessEstate = FirebaseDatabase.getInstance().getReference("Room_1").child("EXEC-PROCESS").child("GLOBAL-STATE");
        globalProcessEstateRoom2 = FirebaseDatabase.getInstance().getReference("Room_2").child("EXEC-PROCESS").child("GLOBAL-STATE");


        globalProcessEstate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String globalEstate = snapshot.getValue(String.class);
                globalProcessEstateLiveData.setValue(globalEstate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });

        globalProcessEstateRoom2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String globalEstate = snapshot.getValue(String.class);
                globalProcessEstateLiveDataRoom2.setValue(globalEstate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });

        sensorDataCO.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long sensorValue = snapshot.getValue(Long.class);
                coLiveData.setValue(sensorValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });

        sensorDataTVOC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long sensorValue = snapshot.getValue(Long.class);
                tvocLiveData.setValue(sensorValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
        sensorDataTank.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long tankLevel = snapshot.getValue(Long.class);
                if (tankLevel != null) {
                    tankLevelLiveData.setValue(tankLevel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });

        room2CO.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long sensorValue = snapshot.getValue(Long.class);
                coLiveData2.setValue(sensorValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });

        room2VOC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long sensorValue = snapshot.getValue(Long.class);
                tvocLiveData2.setValue(sensorValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });

        room2Tank.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long tankLevel = snapshot.getValue(Long.class);
                if (tankLevel != null) {
                    tankLevelLiveData2.setValue(tankLevel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });

    }
    public LiveData<Long> getCoLiveData() {
        return coLiveData;
    }
    public LiveData<Long> getTvocLiveData() {
        return tvocLiveData;
    }
    public LiveData<Long> getTankLevelLiveData() {
        return tankLevelLiveData;
    }

    public LiveData<Long> getCoLiveData2() {
        return coLiveData2;
    }
    public LiveData<Long> getTvocLiveData2() {
        return tvocLiveData2;
    }
    public LiveData<Long> getTank2LevelLiveData() {
        return tankLevelLiveData2;
    }


    public void setOnGoingProcessValue(String value) {
        isOngoingProcess.setValue(value);
    }

    public void setOnGoingProcessValueRoom2(String value) {
        isOngoingProcessRoom2.setValue(value);
    }

    public void setInitialDelayValue(String value) {
        initialDelay.setValue(value);
    }

    public void setMistingSanitationValue(String value) {
        mistingSanitation.setValue(value);
    }

    public void setInitialDelayRoom2Value(String value) {
        initialDelayRoom2.setValue(value);
    }

    public void setMistingSanitationRoom2Value(String value) {
        mistingSanitationRoom2.setValue(value);
    }
    public LiveData<String> getGlobalProcessEstateLiveData() {
        return globalProcessEstateLiveData;
    }

    public LiveData<String> getGlobalProcessEstateLiveDataRoom2() {
        return globalProcessEstateLiveDataRoom2;
    }
}
