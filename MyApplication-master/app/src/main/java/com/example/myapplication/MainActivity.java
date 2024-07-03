package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the correct layout

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, redirect to LandingPage
            Intent intent = new Intent(MainActivity.this, LandingPage.class);
            startActivity(intent);
            finish(); // Optional: Finish the current activity
        }

        emailInput = findViewById(R.id.editTextText1);
        passwordInput = findViewById(R.id.editTextTextPassword1);
        TextView forgotPasswordTextView = findViewById(R.id.textView2);
        Button loginButton = findViewById(R.id.button);
        Button signUpButton = findViewById(R.id.button2);

        forgotPasswordTextView.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, forgotpass.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (isFieldEmpty(emailInput)) {
                emailInput.setError("Email cannot be empty");
            } else if (!isValidEmail(email)) {
                emailInput.setError("Invalid email format");
            } else if (isFieldEmpty(passwordInput)) {
                passwordInput.setError("Password cannot be empty");
            } else if (password.length() < 6) {
                passwordInput.setError("Password must be at least 6 characters");
            } else {
                performLogin(email, password);
            }
        });

        signUpButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, sign_up.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    private boolean isFieldEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern.trim());
        return pattern.matcher(email).matches();
    }

    private void performLogin(String email, String password) {
        // Disable login button to prevent multiple clicks
        findViewById(R.id.button).setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Re-enable login button
                    findViewById(R.id.button).setEnabled(true);

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null || user.isEmailVerified()) {
                            showToast("Login successful!");
                            Intent intent = new Intent(MainActivity.this, LandingPage.class);
                            startActivity(intent);
                            finish(); // Optional: Finish the login activity
                        } else {
                            // User's email is not verified
                            showToast("Please verify your email before logging in.");
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        showToast("Authentication failed.");
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
    private long backPressedTime;
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
