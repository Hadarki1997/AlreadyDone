package com.example.alreadydone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ImageButton btnBack = findViewById(R.id.btnBack);
        MaterialButton btnContinue = findViewById(R.id.btnContinue);
        emailEditText = findViewById(R.id.emailEditText);

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });



        btnContinue.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "אנא הכנס מייל", Toast.LENGTH_SHORT).show();
            } else if (!isValidEmail(email)) {
                Toast.makeText(ForgotPasswordActivity.this, "אנא הכנס מייל תקין", Toast.LENGTH_SHORT).show();
            } else {
                // Add your code to handle the email input and send reset password instructions
                Toast.makeText(ForgotPasswordActivity.this, "הוראות לאיפוס סיסמה נשלחו אל-" + email, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
