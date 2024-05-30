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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullNameEditText, emailEditText, passwordEditText;
    private Button registerButton;
    private CheckBox termsCheckBox;
    private TextView loginLink;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        fullNameEditText.setFilters(new InputFilter[]{new HebrewInputFilter()});

        registerButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (validateInput(fullName, email, password)) {
                if (termsCheckBox.isChecked()) {
                    checkEmailExists(email, new EmailCheckCallback() {
                        @Override
                        public void onEmailChecked(boolean exists) {
                            if (exists) {
                                emailEditText.setError("מייל זה כבר קיים במערכת");
                            } else {
                                registerUser(fullName, email, password);
                            }
                        }
                    });
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
        if (!isPasswordStrong(password)) {
            passwordEditText.setError("הסיסמה צריכה להכיל לפחות 8 תווים שיכילו אות גדולה קטנה מספר מיוחד ותו");
            return false;
        }
        return true;
    }

    private boolean isPasswordStrong(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        return pattern.matcher(password).matches();
    }

    private void registerUser(String fullName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserToFirestore(user.getUid(), fullName, email, password);
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "הרשמה נכשלה: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(String userId, String fullName, String email,String password) {
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("email", email);
        user.put("password", password);
        user.put("isAdmin", false);
        db.collection("users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "נרשמת בהצלחה!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "שגיאה בעת ניסיון הרישום: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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

    private void checkEmailExists(String email, EmailCheckCallback callback) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean exists = !task.getResult().isEmpty();
                        callback.onEmailChecked(exists);
                    } else {
                        Toast.makeText(RegisterActivity.this, "שגיאה בבדיקת המייל: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private interface EmailCheckCallback {
        void onEmailChecked(boolean exists);
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
