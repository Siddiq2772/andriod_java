package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class sign_up extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.editTextText1);
        passwordInput = findViewById(R.id.editTextTextPassword1);
        Button signupButton = findViewById(R.id.button);
        Button loginButton = findViewById(R.id.button2);

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(sign_up.this, MainActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signupButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (isFieldEmpty(emailInput)) {
                emailInput.setError("Email cannot be empty");
            } else if (isFieldEmpty(passwordInput)) {
                passwordInput.setError("Password cannot be empty");
            } else {
                createUserAccount(email, password);
            }
        });
    }

    private boolean isFieldEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private void createUserAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign up success, send verification email
                        FirebaseUser user = mAuth.getCurrentUser();
                        sendVerificationEmail(user);
                    } else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(sign_up.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendVerificationEmail(FirebaseUser user) {
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(sign_up.this, "Verification email sent. Please verify and login.",
                                    Toast.LENGTH_LONG).show();
                            // Redirect to login page
                            Intent intent = new Intent(sign_up.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Optional: Finish the sign-up activity
                        } else {
                            Toast.makeText(sign_up.this, "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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