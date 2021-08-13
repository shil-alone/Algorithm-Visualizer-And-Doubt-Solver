package com.codershil.algorithmvisualizer.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codershil.algorithmvisualizer.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private static boolean splashLoaded = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_login));
        }

        if (!splashLoaded){
            setContentView(R.layout.splash);
            int secondDelay = 1;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    }
                    else{
                        startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                    }
                    finish();
                }
            },secondDelay*1000);
            splashLoaded = true;
        }
        else{
            if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                Intent gotoMainActivity = new Intent(SplashScreen.this,MainActivity.class);
                gotoMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(gotoMainActivity);
            }
            else{
                Intent gotToLoginActivity = new Intent(SplashScreen.this, LoginActivity.class);
                gotToLoginActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(gotToLoginActivity);
            }
            finish();

        }

    }
}
