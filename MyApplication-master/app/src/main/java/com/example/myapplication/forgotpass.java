package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpass extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        EdgeToEdge.enable(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        EditText forgotpassemail = findViewById(R.id.editTextText1);
        Button forgotpassbutton = findViewById(R.id.button1);
        TextView forgotpasslogin = findViewById(R.id.textView3);

        forgotpassbutton.setOnClickListener(view -> {
            String email = forgotpassemail.getText().toString();

            if (email.isEmpty()) {
                forgotpassemail.setError("Email cannot be empty");
            } else {
                sendPasswordResetEmail(email);
            }
        });

        forgotpasslogin.setOnClickListener(view -> {
            Intent intent = new Intent(forgotpass.this, MainActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(forgotpass.this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(forgotpass.this, "Please check your email to reset your password.", Toast.LENGTH_LONG).show();

                        // Redirect to login page
                        Intent intent = new Intent(forgotpass.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Optional: Finish the forgot password activity
                    } else {
                        // If sending email fails, display a message to the user.
                        Log.w("ForgotPasswordActivity", "Failed to send reset email.", task.getException());
                        Toast.makeText(forgotpass.this, "Failed to send reset email. Please check your email and try again.", Toast.LENGTH_LONG).show();
                    }
                });
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