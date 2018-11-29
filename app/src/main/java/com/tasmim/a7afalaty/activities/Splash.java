package com.tasmim.a7afalaty.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tasmim.a7afalaty.Home;
import com.tasmim.a7afalaty.NetworkTester;
import com.tasmim.a7afalaty.R;

import es.dmoral.toasty.Toasty;

public class Splash extends AppCompatActivity {
    Animation animAccelerate = null;
    NetworkTester networkTester = new NetworkTester(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splash_logo = findViewById(R.id.splash_img_id);

        animAccelerate = AnimationUtils.loadAnimation(this, R.anim.bounce);
        splash_logo.startAnimation(animAccelerate);
        animAccelerate.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


                startApp();

            }
        });
    }

    private void startApp() {

        if (!networkTester.isNetworkAvailable())
            Toasty.error(Splash.this, getString(R.string.error_connection), 1500).show();

        SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString("name", "");
        int user_id = prefs.getInt("user_id", -1);

//        if (name != null && !name.trim().isEmpty()) {
        Intent intent = new Intent(Splash.this, Home.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
        finish();
//        } else {
//            Intent  intent = new Intent(Splash.this, Login.class);
//            startActivity(intent);
//            finish();
//        }
    }
}
