package com.example.alreadydone;

import android.content.Intent;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView, forgotPasswordTextView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.button_login);
        registerTextView = findViewById(R.id.button_register);
        forgotPasswordTextView = findViewById(R.id.clickHere);

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

    private void checkIfUserIsAdmin(String uid) {
        Log.d(TAG, "Checking if user is admin: " + uid);
        db.collection("users").document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "Snapshot exists");
                    Boolean isAdmin = document.getBoolean("isAdmin");
                    Log.d(TAG, "User isAdmin: " + isAdmin);
                    if (isAdmin != null && isAdmin) {
                        navigateToAdminHome();
                    } else {
                        navigateToHome();
                    }
                } else {
                    Log.d(TAG, "User not found in database.");
                    Toast.makeText(LoginActivity.this, "משתמש לא קיים בבסיס הנתונים.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "Error accessing user data: " + task.getException().getMessage());
                Toast.makeText(LoginActivity.this, "שגיאה בגישה לנתוני המשתמש.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHome() {
        Log.d(TAG, "Navigating to home.");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToAdminHome() {
        Log.d(TAG, "Navigating to admin home.");
        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }
}
