package com.example.alreadydone;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alreadydone.api.ApiService;
import com.example.alreadydone.api.ApiResponse;
import com.example.alreadydone.api.RegisterRequest;
import com.example.alreadydone.callback.ApiCallback;
import com.example.alreadydone.callback.OnApiResponse;
import com.example.alreadydone.RetrofitClient;

import retrofit2.Call;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final String BASE_URL = "https://d77ad679-7033-4412-87f6-6604e1868684.mock.pstmn.io/";

    private EditText fullNameEditText, emailEditText, passwordEditText;
    private CheckBox termsCheckBox;
    private Button registerButton;
    private TextView termsTextView1, loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullNameEditText = findViewById(R.id.full_name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        termsCheckBox = findViewById(R.id.terms_checkbox);
        registerButton = findViewById(R.id.button_register);
        termsTextView1 = findViewById(R.id.terms_text1);
        loginTextView = findViewById(R.id.login_text);

        fullNameEditText.setFilters(new InputFilter[]{new HebrewInputFilter()});

        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        registerButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (validateInput(fullName, email, password)) {
                checkEmailExistence(fullName, email, password);
            }
        });
    }

    private boolean validateInput(String fullName, String email, String password) {
        if (TextUtils.isEmpty(fullName) || !fullName.matches("[א-ת ]+")) {
            fullNameEditText.setError("שם מלא נדרש וחייב להכיל אותיות עבריות בלבד");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.endsWith(".com")) {
            emailEditText.setError("פורמט מייל לא תקין או שהאימייל לא מסתיים ב-.com");
            return false;
        }
        if (TextUtils.isEmpty(password) || password.length() <= 6) {
            passwordEditText.setError("סיסמה נדרשת וחייבת להיות יותר מ-6 תווים");
            return false;
        }
        if (!termsCheckBox.isChecked()) {
            Toast.makeText(this, "עליך לאשר את התנאים וההגבלות", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void checkEmailExistence(String fullName, String email, String password) {
        Retrofit retrofit = RetrofitClient.getClient(BASE_URL);
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ApiResponse> call = apiService.checkEmail(email);
        call.enqueue(new ApiCallback(response -> {
            if (response.isSuccess()) {
                Toast.makeText(RegisterActivity.this, "האימייל כבר קיים במערכת", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(fullName, email, password);
            }
        }, this, "שגיאה בבדיקת קיום האימייל"));
    }

    private void registerUser(String fullName, String email, String password) {
        Retrofit retrofit = RetrofitClient.getClient(BASE_URL);
        ApiService apiService = retrofit.create(ApiService.class);
        RegisterRequest registerRequest = new RegisterRequest(fullName, email, password);
        Call<ApiResponse> call = apiService.registerUser(registerRequest);
        call.enqueue(new ApiCallback(response -> {
            if (response.isSuccess()) {
                Toast.makeText(RegisterActivity.this, "נרשמת בהצלחה!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, this, "שגיאה בעת ניסיון הרישום"));
    }


    private void showTermsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("תנאים וההגבלות");
        builder.setMessage(
                "1. הקדמה\n\n" +
                        "ברוכים הבאים לאפליקציה 'כבר תרמתי'. אפליקציה זו נועדה לסייע לך להשתתף בפעילויות תרומה והתנדבות בקהילה. באמצעות האפליקציה, תוכל למצוא ארגונים הזקוקים לעזרה ולתרום למטרות החשובות לך. אנו מעריכים את נכונותך לקחת חלק במאמץ לשיפור החברה והקהילה.\n\n" +
                        "2. שימוש באפליקציה\n\n" +
                        "עליך להיות בן 18 לפחות כדי להשתמש באפליקציה זו.\n" +
                        "אתה מסכים להשתמש באפליקציה על פי תנאי השימוש שלנו.\n\n" +
                        "3. חשבונות משתמש\n\n" +
                        "כאשר אתה יוצר חשבון אצלנו, עליך לספק מידע מדויק ומלא.\n" +
                        "אתה אחראי לשמירה על סודיות החשבון והסיסמה שלך.\n\n" +
                        "4. תרומות\n\n" +
                        "כל התרומות ישמשו למטרות המצוינות בבקשה.\n\n" +
                        "5. מדיניות פרטיות\n\n" +
                        "אנו אוספים מידע אישי כדי לספק ולשפר את השירותים שלנו. בשימוש באפליקציה, אתה מסכים לאיסוף ושימוש במידע בהתאם למדיניות הפרטיות שלנו.\n\n" +
                        "6. קניין רוחני\n\n" +
                        "התוכן, העיצוב והפריסה של האפליקציה מוגנים בזכויות יוצרים, סימנים מסחריים וזכויות קניין רוחני אחרות.\n" +
                        "אינך רשאי לשכפל, להפיץ או להשתמש בכל דרך אחרת בתוכן ללא הסכמתנו מראש ובכתב.\n\n" +
                        "7. הגבלת אחריות\n\n" +
                        "האפליקציה מסופקת \"כמות שהיא\" ללא כל אחריות, מפורשת או משתמעת.\n" +
                        "איננו מבטיחים שהאפליקציה תהיה זמינה בכל עת או שהיא תהיה נקייה משגיאות.\n\n" +
                        "8. סיום\n\n" +
                        "אנו עשויים להפסיק או להשעות את הגישה שלך לאפליקציה בכל עת, ללא הודעה מוקדמת או חבות, מכל סיבה שהיא.\n\n" +
                        "9. שינויים בתנאים וההגבלות\n\n" +
                        "אנו שומרים לעצמנו את הזכות לעדכן או לשנות את התנאים וההגבלות שלנו בכל עת. המשך השימוש שלך באפליקציה לאחר כל שינוי מעיד על הסכמתך לתנאים החדשים.\n\n" +
                        "10. צור קשר\n\n" +
                        "אם יש לך שאלות כלשהן לגבי תנאים אלה, אנא צור איתנו קשר בכתובת support@alreadydone.com."
        );

        builder.setPositiveButton("אשר", (dialog, which) -> {
            termsCheckBox.setChecked(true);
            dialog.dismiss();
        });

        builder.setNegativeButton("בטל", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class HebrewInputFilter implements InputFilter {
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
