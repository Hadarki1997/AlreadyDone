package com.example.alreadydone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        Button getStartedButton = findViewById(R.id.button_get_started);
        getStartedButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeScreen.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
