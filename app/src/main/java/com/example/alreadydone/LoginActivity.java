package com.example.alreadydone;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        emailEditText = findViewById(R.id.editText_email);
        passwordEditText = findViewById(R.id.editText_password);
        loginButton = findViewById(R.id.button_login);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // בדיקת התחברות (במקום זה יש להוסיף את הלוגיקה שלך לאימות משתמש)
            if (email.equals("user@example.com") && password.equals("password")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                // טיפול במצב של התחברות שגויה
                Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

