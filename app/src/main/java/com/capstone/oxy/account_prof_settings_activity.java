package com.capstone.oxy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class account_prof_settings_activity extends AppCompatActivity {
    ImageButton back_btn;
    LinearLayout linearL_users_list_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_prof_settings);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealsecondary));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        linearL_users_list_btn = findViewById(R.id.linearL_users_list_btn);
        back_btn = findViewById(R.id.back_btn);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( account_prof_settings_activity.this, dashboard_room101_home.class);
                startActivity(intent);
            }
        });

        linearL_users_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(account_prof_settings_activity.this, activity_start_sanitation.class);
                startActivity(intent);
            }
        });
    }
}