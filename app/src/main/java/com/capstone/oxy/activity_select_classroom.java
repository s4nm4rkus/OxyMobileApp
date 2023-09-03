package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class activity_select_classroom extends AppCompatActivity {

    TextView logout_btn;
    TextView room101_btn, room102_btn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_classroom);

        logout_btn = findViewById(R.id.btn_logout);
        room101_btn = findViewById(R.id.room101);
        room102_btn = findViewById(R.id.room102);
        mAuth = FirebaseAuth.getInstance();

        room101_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (activity_select_classroom.this, dashboard_room101.class);
                startActivity(intent);
            }
        });

    }

    public void logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(activity_select_classroom.this, Login.class);
        startActivity(intent);
    }
}