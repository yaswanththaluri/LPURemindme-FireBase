package com.maya.yaswanththaluri.remindme;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashBoard extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ActionBar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

//        auth = FirebaseAuth.getInstance();
//        FirebaseUser user = auth.getCurrentUser();


        Fragment fragment;

        toolBar = getSupportActionBar();
        toolBar.setTitle("CA Schedule");
        fragment = new Schedule();
        loadFragent(fragment);

        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.bottomnavbar);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment;
                switch (item.getItemId())
                {
                    case R.id.exam:
                        toolBar.setTitle("CA Schedule");
                        fragment = new Schedule();
                        loadFragent(fragment);
                        return true;

                    case R.id.announcements:
                        toolBar.setTitle("Announcements");
                        fragment = new Announcement();
                        loadFragent(fragment);
                        return true;

                    case R.id.profile:
                        toolBar.setTitle("Profile");
                        fragment = new Profile();
                        loadFragent(fragment);
                        return true;
                }
                return false;
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        auth.addAuthStateListener(authStateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (authStateListener != null) {
//            auth.removeAuthStateListener(authStateListener);
//        }
//    }

    public void loadFragent(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }
}
