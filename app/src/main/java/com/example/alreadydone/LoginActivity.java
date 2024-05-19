package com.example.alreadydone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alreadydone.api.ApiService;
import com.example.alreadydone.api.ApiResponse;
import com.example.alreadydone.api.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.button_login);
        registerTextView = findViewById(R.id.button_register);

        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (!validateLoginInput(email, password)) {
                return;
            }

            try {
                loginUser(email, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean validateLoginInput(String email, String password) {
        if (!isValidEmail(email)) {
            emailEditText.setError("פורמט מייל לא תקין");
            return false;
        }
        if (TextUtils.isEmpty(password) || password.length() <= 6) {
            passwordEditText.setError("סיסמה נדרשת והסיסמה חייבת להיות יותר מ-6 תווים");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.toString().endsWith(".com");
    }

    private void loginUser(String email, String password) {
        String baseUrl = "https://985d412b-c46f-47fb-aa1b-898fa60ebb10.mock.pstmn.io/";
        Retrofit retrofit = RetrofitClient.getClient(baseUrl);
        ApiService apiService = retrofit.create(ApiService.class);

        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<ApiResponse> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "שגיאה בעת ניסיון ההתחברות", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "שגיאה בעת ניסיון ההתחברות", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
