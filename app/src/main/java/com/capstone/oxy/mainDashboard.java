package com.capstone.oxy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class mainDashboard extends AppCompatActivity {

    ImageButton navHome, navReport, navTank, navGuide, navAbout, switch_room_btn, accountSetting;
    ImageButton lastClickedButton;
    FrameLayout fragmentContainer;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view for this activity
        setContentView(R.layout.activity_main_dashboard);

        // Set the status bar color to teal
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));

        // Make the navigation bar translucent
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

        // Initialize UI elements
        switch_room_btn = findViewById(R.id.switch_room_btn);
        accountSetting = findViewById(R.id.accountSetting);

        navHome = findViewById(R.id.nav_home_btn);
        navReport = findViewById(R.id.nav_reports_btn);
        navTank = findViewById(R.id.nav_tank_btn);
        navGuide = findViewById(R.id.nav_guide_btn);
        navAbout = findViewById(R.id.nav_about_btn);

        fragmentContainer = findViewById(R.id.home_framelayout);

        // Check if savedInstanceState is null and initialize the HomeFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(fragmentContainer.getId(), new HomeFragment())
                    .commit();
            lastClickedButton = navHome;
        }

        // Set click listeners for navigation buttons
        navHome.setOnClickListener(new ToggleClickListener(new HomeFragment(), navHome));
        navReport.setOnClickListener(new ToggleClickListener(new ReportsFragment(), navReport));
        navTank.setOnClickListener(new ToggleClickListener(new TankFragment(), navTank));
        navGuide.setOnClickListener(new ToggleClickListener(new GuideFragment(), navGuide));
        navAbout.setOnClickListener(new ToggleClickListener(new AboutFragment(), navAbout));

        // Click listener for the account setting button
        accountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainDashboard.this, accountProfileSetting.class);
                startActivity(intent);
                finish();
            }
        });

        // Click listener for the switch room button
        switch_room_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSwitchRoomDialog();
            }
        });

    }

    // Method to show the switch room dialog
    private void showSwitchRoomDialog() {
        Dialog switchpopupDialog = new Dialog(mainDashboard.this);
        switchpopupDialog.setContentView(R.layout.changeroom_popup);

        ImageButton icClose = switchpopupDialog.findViewById(R.id.closePopup);
        TextView switchRoomBtn = switchpopupDialog.findViewById(R.id.leaveRoom);

        switchRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainDashboard.this, activityRoomSelection.class);
                startActivity(intent);
            }
        });

        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchpopupDialog.dismiss();
            }
        });

        switchpopupDialog.show();
    }

    // Inner class for handling button clicks and fragment navigation
    private class ToggleClickListener implements View.OnClickListener {
        private Fragment fragment;
        private ImageButton button;

        public ToggleClickListener(Fragment fragment, ImageButton button) {
            this.fragment = fragment;
            this.button = button;
        }

        @Override
        public void onClick(View view) {
            if (fragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(fragmentContainer.getId(), fragment);
                transaction.commit();
            }

            if (button != lastClickedButton) {
                if (lastClickedButton != null) {
                    resetButtonSize(lastClickedButton);
                }
                enlargeButton(button);
                lastClickedButton = button;
            }
        }

        private void enlargeButton(ImageButton button) {
            int newWidth = (int) (button.getWidth() * 1.5f);
            int newHeight = (int) (button.getHeight() * 1.5f);
            ViewGroup.LayoutParams params = button.getLayoutParams();
            params.width = newWidth;
            params.height = newHeight;
            button.setLayoutParams(params);
        }

        private void resetButtonSize(ImageButton button) {
            int originalWidth = getResources().getDimensionPixelSize(R.dimen.nav_button_width_default);
            int originalHeight = getResources().getDimensionPixelSize(R.dimen.nav_button_height_default);

            ViewGroup.LayoutParams params = button.getLayoutParams();
            params.width = originalWidth;
            params.height = originalHeight;
            button.setLayoutParams(params);
        }
    }
}
