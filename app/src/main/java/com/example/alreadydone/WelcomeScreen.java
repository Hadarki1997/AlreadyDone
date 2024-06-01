package com.example.alreadydone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000; // 3 שניות

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        // נוודא שהמשתמש תמיד יועבר למסך ההתחברות
        Button getStartedButton = findViewById(R.id.button_get_started);
        getStartedButton.setOnClickListener(v -> navigateToLogin());

        // ניווט אוטומטי לאחר פרק זמן מסויים
        new android.os.Handler().postDelayed(this::navigateToLogin, SPLASH_TIME_OUT);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(WelcomeScreen.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
