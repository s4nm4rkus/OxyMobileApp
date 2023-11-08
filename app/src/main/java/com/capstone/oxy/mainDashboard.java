package com.capstone.oxy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class mainDashboard extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageButton navHome, navReport, navTank, navGuide, navAbout, switch_room_btn, accountSetting;
    ImageButton lastClickedButton;
    FrameLayout fragmentContainer;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    TextView roomLabel;


    private boolean doubleBackToExitPressedOnce = false;
    private static final int DOUBLE_BACK_EXIT_DELAY = 2000;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.tealmain));
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        );

        String selectedRoom = getIntent().getStringExtra("selectedRoom");

        roomLabel = findViewById(R.id.RoomLabel);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        drawerLayout = findViewById(R.id.main_dashboard_container2);
        navigationView = findViewById(R.id.drawer_nav);
        toolbar = findViewById(R.id.toolbar);
        navigationView.setNavigationItemSelectedListener(menuItem -> handleNavigationItemSelected(menuItem));
        toolbar.setNavigationIcon(R.drawable.ic_menu_ham);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        // Initialize UI elements
        switch_room_btn = findViewById(R.id.switch_room_btn);

        navHome = findViewById(R.id.nav_home_btn);
        navReport = findViewById(R.id.nav_reports_btn);
        navTank = findViewById(R.id.nav_tank_btn);
        navGuide = findViewById(R.id.nav_guide_btn);
        navAbout = findViewById(R.id.nav_about_btn);

        fragmentContainer = findViewById(R.id.home_framelayout);

        if (selectedRoom != null) {
            roomLabel.setText(getString(R.string.room101));
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(fragmentContainer.getId(), new HomeFragment())
                        .commit();
                lastClickedButton = navHome;

                // Set click listeners for navigation buttons

                navHome.setOnClickListener(new ToggleClickListener(new HomeFragment(), navHome));
                navReport.setOnClickListener(new ToggleClickListener(new ReportsFragment(), navReport));
                navTank.setOnClickListener(new ToggleClickListener(new TankFragment(), navTank));
                navGuide.setOnClickListener(new ToggleClickListener(new GuideFragment(), navGuide));
                navAbout.setOnClickListener(new ToggleClickListener(new AboutFragment(), navAbout));
            }
        }else{
            roomLabel.setText(getString(R.string.room102));
            room2Selected();

            navHome.setOnClickListener(new ToggleClickListener(new HomeFragmentRoom2(), navHome));
            navReport.setOnClickListener(new ToggleClickListener(new ReportsFragment(), navReport));
            navTank.setOnClickListener(new ToggleClickListener(new TankFragmentRoom2(), navTank));
            navGuide.setOnClickListener(new ToggleClickListener(new GuideFragment(), navGuide));
            navAbout.setOnClickListener(new ToggleClickListener(new AboutFragment(), navAbout));
        }



        switch_room_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSwitchRoomDialog();
            }
        });

        updateNavHeader();

    }

    private void room2Selected() {
        HomeFragmentRoom2 fragmentRoom2 = new HomeFragmentRoom2();
        getSupportFragmentManager().beginTransaction()
                .replace(fragmentContainer.getId(), fragmentRoom2)
                .commit();
    }

    public void updateNavHeader() {
            navigationView = (NavigationView) findViewById(R.id.drawer_nav);
            View headerView = navigationView.getHeaderView(0);
            TextView userName = headerView.findViewById(R.id.user_name_drawerLabel);
            TextView userEmail = headerView.findViewById(R.id.email_drawerLabel);

            userEmail.setText(currentUser.getEmail());

            String userID = firebaseAuth.getUid();

            if (userID != null) {
                CollectionReference collection = db.collection("users");
                DocumentReference document = collection.document(userID);

                document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                String text = documentSnapshot.getString("Username");
                                userName.setText(text);
                            } else {
                                // Document doesn't exist
                                // Handle this case if needed
                            }
                        } else {
                            // Handle errors, e.g., network issues or permissions problems
                            Exception exception = task.getException();
                            if (exception != null) {
                                exception.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
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

    private boolean handleNavigationItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case R.id.room_name:
                break;
            case R.id.userAccountsList:
                Intent intent = new Intent(mainDashboard.this, ListOfUserAccount.class);
                startActivity(intent);
                break;
            case R.id.logout:
                showLogoutConfirmationDialog();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainDashboard.this);
        builder.setTitle("Logout Confirmation")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(mainDashboard.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
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
                if (fragment.equals(navHome)){
                    transaction.setCustomAnimations(
                            R.anim.fragment_slide_in_right,    // Enter animation (from right)
                            R.anim.fragment_slide_out_left,    // Exit animation (to left)
                            R.anim.fragment_slide_in_left,     // Pop enter animation (from left)
                            R.anim.fragment_slide_out_right   // Pop exit animation (to right)
                    );
                }
                if(fragment.equals(navReport)){
                    transaction.setCustomAnimations(
                            R.anim.fragment_slide_in_right,    // Enter animation (from right)
                            R.anim.fragment_slide_out_left,   // Exit animation (to left)
                            R.anim.fragment_slide_in_left,     // Pop enter animation (from left)
                            R.anim.fragment_slide_out_right   // Pop exit animation (to right)
                    );
                }

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
