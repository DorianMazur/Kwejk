package com.kwejk.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kwejk.R;

public class MainActivity extends AppCompatActivity {

    private static int SplashTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            Intent HomeIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(HomeIntent);
            finish();
            }
        }, SplashTime);
    }
}
