
package com.example.alreadydone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefs";
    private static final int SPLASH_TIME_OUT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            new Handler().postDelayed(this::navigateToHome, SPLASH_TIME_OUT);
        } else {
            Button getStartedButton = findViewById(R.id.button_get_started);
            getStartedButton.setOnClickListener(v -> navigateToLogin());
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(WelcomeScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(WelcomeScreen.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}