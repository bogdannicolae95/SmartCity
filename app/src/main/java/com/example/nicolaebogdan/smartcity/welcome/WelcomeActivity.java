package com.example.nicolaebogdan.smartcity.welcome;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.main.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToMainAcativityPage();
            }
        }, 800);

    }

    private void goToMainAcativityPage() {
        Intent homeScreen = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(homeScreen);
        finish();
    }
}
