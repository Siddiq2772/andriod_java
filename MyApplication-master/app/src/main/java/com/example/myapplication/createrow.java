package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class createrow extends AppCompatActivity {

    EditText editTextTitle;
    EditText editTextContent;
    FloatingActionButton buttonSave;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    Toolbar toolbar;



    private String documentId; // To store the document ID for editing

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_createrow);

        editTextTitle = findViewById(R.id.editTextText1);
        editTextContent = findViewById(R.id.editTextText2);
        buttonSave = findViewById(R.id.floatingActionButton);
        toolbar = findViewById(R.id.toolbar2);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Check if intent has data (for editing)
        Intent intent = getIntent();
        if (intent.hasExtra("documentId")) {
            documentId = intent.getStringExtra("documentId");
            loadNoteData(documentId); // Load existing note data
        }

        buttonSave.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString().trim();
            String content = editTextContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(createrow.this, "Please enter both fields", Toast.LENGTH_SHORT).show();
            } else if (title.length() < 3) {
                Toast.makeText(createrow.this, "Title should be at least 3 characters long", Toast.LENGTH_SHORT).show();
            } else {
                if (documentId != null) {
                    // Update existing note
                    updateNote(documentId, title, content);
                } else {
                    // Create new note
                    createNewNote(title, content);
                }
            }
        });
    }

    private void loadNoteData(String documentId) {
        if (firebaseUser != null) {
            firestore.collection("Notes")
                    .document(firebaseUser.getUid())
                    .collection("MyNotes")
                    .document(documentId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            FireBaseModel note = documentSnapshot.toObject(FireBaseModel.class);
                            if (note != null) {
                                editTextTitle.setText(note.getTitle());
                                editTextContent.setText(note.getContent());
                            }
                        } else {
                            Toast.makeText(createrow.this, "Note not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(createrow.this, "Error loading note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("createrow", "Error loading note", e);
                        finish();
                    });
        }
    }

    private void updateNote(String documentId, String title, String content) {
        if (firebaseUser != null) {
            DocumentReference documentReference = firestore.collection("Notes")
                    .document(firebaseUser.getUid())
                    .collection("MyNotes")
                    .document(documentId);

            Map<String, Object> note = new HashMap<>();
            note.put("Title", title);
            note.put("content", content);

            documentReference.update(note)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(createrow.this, "Note updated", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(createrow.this, "Failed to update note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("createrow", "Error updating note", e);
                    });
        }
        setResult(RESULT_OK); // Signal to LandingPage that data has changed
        finish();
    }

    private void createNewNote(String title, String content) {
        if (firebaseUser != null) {
            DocumentReference documentReference = firestore.collection("Notes")
                    .document(firebaseUser.getUid())
                    .collection("MyNotes")
                    .document();

            Map<String, Object> note = new HashMap<>();
            note.put("Title", title);
            note.put("content", content);

            documentReference.set(note)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(createrow.this, "Note saved", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(createrow.this, "Failed to save note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("createrow", "Error saving note", e);
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
