package com.example.quizgame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizgame.R;
import com.example.quizgame.data.Prefs;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prefs = new Prefs(this);
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);

        loginButton.setOnClickListener(view -> attemptLogin());
        registerButton.setOnClickListener(view -> registerUser());
    }

    private void attemptLogin() {
        clearErrors();
        String username = getTrimmedValue(usernameInput);
        String password = getTrimmedValue(passwordInput);

        if (!validateInputs(username, password)) {
            return;
        }

        if (prefs.validateUser(username, password)) {
            Intent intent = new Intent();
            intent.setClassName(this, "com.example.quizgame.ui.DifficultyActivity");
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser() {
        clearErrors();
        String username = getTrimmedValue(usernameInput);
        String password = getTrimmedValue(passwordInput);

        if (!validateInputs(username, password)) {
            return;
        }

        prefs.saveUser(username, password);
        Toast.makeText(this, "Registration saved. You can log in now.", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs(String username, String password) {
        boolean valid = true;
        if (TextUtils.isEmpty(username)) {
            usernameInput.setError("Username is required.");
            valid = false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required.");
            valid = false;
        }
        return valid;
    }

    private void clearErrors() {
        usernameInput.setError(null);
        passwordInput.setError(null);
    }

    private String getTrimmedValue(EditText editText) {
        return editText.getText().toString().trim();
    }
}
