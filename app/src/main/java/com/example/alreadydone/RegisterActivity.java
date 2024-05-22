package com.example.alreadydone;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alreadydone.api.ApiService;
import com.example.alreadydone.api.RegisterRequest;
import com.example.alreadydone.api.ApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText fullNameEditText, emailEditText, passwordEditText;
    private Button registerButton;
    private CheckBox termsCheckBox;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullNameEditText = findViewById(R.id.full_name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.button_register);
        termsCheckBox = findViewById(R.id.terms_checkbox);
        loginLink = findViewById(R.id.login_text);

        fullNameEditText.setFilters(new InputFilter[]{new HebrewInputFilter()});

        registerButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (validateInput(fullName, email, password)) {
                if (termsCheckBox.isChecked()) {
                    registerUser(fullName, email, password);
                } else {
                    showTermsDialog();
                }
            }
        });

        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInput(String fullName, String email, String password) {
        if (TextUtils.isEmpty(fullName)) {
            fullNameEditText.setError("שם מלא נדרש");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("פורמט מייל לא תקין");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("סיסמה נדרשת");
            return false;
        }
        return true;
    }

    private void registerUser(String fullName, String email, String password) {
        String baseUrl = "https://674d-2a0d-6fc2-6830-6c00-2da1-7ece-ca3e-e552.ngrok-free.app";
        Retrofit retrofit = RetrofitClient.getClient(baseUrl);
        ApiService apiService = retrofit.create(ApiService.class);

        RegisterRequest registerRequest = new RegisterRequest(fullName, email, password);
        Call<ApiResponse> registerCall = apiService.registerUser(registerRequest);
        registerCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    if ("Successfully registered!".equals(message)) {
                        Toast.makeText(RegisterActivity.this, "נרשמת בהצלחה!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "שגיאה בעת ניסיון הרישום", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "שגיאה בעת ניסיון הרישום", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showTermsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("תנאים וההגבלות");
        builder.setMessage("1. הקדמה\n\n");

        builder.setPositiveButton("אשר", (dialog, which) -> {
            termsCheckBox.setChecked(true);
            dialog.dismiss();
        });

        builder.setNegativeButton("בטל", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static class HebrewInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.toString(source.charAt(i)).matches("[א-ת ]")) {
                    return "";
                }
            }
            return null;
        }
    }
}
