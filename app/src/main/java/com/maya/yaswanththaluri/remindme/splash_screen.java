package com.maya.yaswanththaluri.remindme;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.FileObserver;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActionBar b = getSupportActionBar();
        if (b != null) {
            b.hide();
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user!=null)
                {
                    Intent i = new Intent(getApplicationContext(),DashBoard.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(getApplicationContext(),Login.class);
                    ImageView s = (ImageView)findViewById(R.id.splash);
                    Pair[] p = new Pair[1];
                    p[0] = new Pair(s, "imgTrans");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(splash_screen.this, p);
                    startActivity(i, options.toBundle());

                }
            }
        },2000);
    }
}
