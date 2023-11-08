package com.capstone.oxy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListOfUserAccount extends AppCompatActivity {

    RecyclerView recyclerView;
    userAdapter userAdapter;
    ArrayList<QueryDocumentSnapshot> list;
    LinearLayout adduserBtn, backBtn;
    private boolean doubleBackToExitPressedOnce = false;
    private static final int DOUBLE_BACK_EXIT_DELAY = 2000;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_user_account);

        recyclerView = findViewById(R.id.usersList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        userAdapter = new userAdapter(this, list);
        recyclerView.setAdapter(userAdapter);

        // Initialize Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        list.clear(); // Clear the list before adding new data
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            list.add(document);
                        }
                        userAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error here
                    }
                });

        adduserBtn = findViewById(R.id.add_user_btn);
        backBtn = findViewById(R.id.back_button);

        adduserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListOfUserAccount.this, activityRegisterNewuser.class);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, DOUBLE_BACK_EXIT_DELAY);
    }
}
