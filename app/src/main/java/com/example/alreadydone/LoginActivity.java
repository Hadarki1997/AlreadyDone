package com.example.alreadydone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String PREFS_NAME = "MyPrefs";

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView, forgotPasswordTextView;
    private SharedPreferences sharedPreferences;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.button_login);
        registerTextView = findViewById(R.id.button_register);
        forgotPasswordTextView = findViewById(R.id.clickHere);

        // Check if user is already logged in
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // Check if user still exists in Firebase
                currentUser.reload().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        navigateToHome();
                    } else {
                        // User does not exist, clear shared preferences and redirect to login
                        clearUserPreferences();
                        Toast.makeText(LoginActivity.this, "משתמש לא נמצא. יש להתחבר מחדש.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                clearUserPreferences();
            }
        }

        loadUserPreferences();

        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        forgotPasswordTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

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
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserPreferences(email, password);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            Log.d(TAG, "User logged in: " + user.getUid());
                            checkIfUserIsAdmin(user.getUid());
                        } else {
                            Toast.makeText(LoginActivity.this, "משהו השתבש. נסה שוב.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "מייל או סיסמה אינם נכונים", Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void clearUserPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void checkIfUserIsAdmin(String uid) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Boolean isAdmin = snapshot.child("isAdmin").getValue(Boolean.class);
                    Log.d(TAG, "User isAdmin: " + isAdmin);
                    if (isAdmin != null && isAdmin) {
                        navigateToAdminHome();
                    } else {
                        navigateToHome();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "משתמש לא קיים בבסיס הנתונים.", Toast.LENGTH_SHORT).show();
                    clearUserPreferences();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "שגיאה בגישה לנתוני המשתמש.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToAdminHome() {
        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }
}
