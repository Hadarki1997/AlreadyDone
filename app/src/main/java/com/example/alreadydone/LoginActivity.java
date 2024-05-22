package com.example.alreadydone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alreadydone.api.ApiService;
import com.example.alreadydone.api.ApiResponse;
import com.example.alreadydone.api.LoginRequest;
import com.example.alreadydone.callback.ApiCallback;

import retrofit2.Call;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String BASE_URL = "https://674d-2a0d-6fc2-6830-6c00-2da1-7ece-ca3e-e552.ngrok-free.app";
    private static final String PREFS_NAME = "MyPrefs";

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.button_login);
        registerTextView = findViewById(R.id.button_register);

        // בדוק אם המשתמש מחובר והעבר אותו לדף ה-Home
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            navigateToHome();
        }

        // טען פרטי התחברות אם קיימים
        loadUserPreferences();

        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (validateInput(email, password)) {
                loginUser(email, password);
            }
        });
    }

    private boolean validateInput(String email, String password) {
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

    private void loginUser(String email, String password) {
        Retrofit retrofit = RetrofitClient.getClient(BASE_URL);
        ApiService apiService = retrofit.create(ApiService.class);

        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<ApiResponse> call = apiService.loginUser(loginRequest);

        call.enqueue(new ApiCallback(response -> {
            if (response.getMessage().equals("Successfully logged in!")) {
                // שמור פרטי התחברות
                saveUserPreferences(email, password);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();

                Toast.makeText(LoginActivity.this, "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                navigateToHome();
            } else {
                Log.e(TAG, "Login failed: " + response.getMessage());
            }
        }, this, "שגיאה בעת ניסיון ההתחברות"));
    }

    private void saveUserPreferences(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    private void loadUserPreferences() {
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");

        emailEditText.setText(email);
        passwordEditText.setText(password);
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
