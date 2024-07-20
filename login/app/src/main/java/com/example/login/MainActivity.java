package com.example.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText eMail,pass;
    FirebaseFirestore firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        button = findViewById(R.id.button);
        eMail = findViewById(R.id.Email);
        pass = findViewById(R.id.Password);
        firebase = FirebaseFirestore.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String Email= eMail.getText().toString();
               String password = pass.getText().toString();


                Map store= new HashMap<>();
                store.put(Email,password);

                firebase.collection("loginapp").add(store).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful())
                    Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "login UnSuccessful", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });


    }
}