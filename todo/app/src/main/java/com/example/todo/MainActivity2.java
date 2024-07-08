// MainActivity.java
package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;


import com.example.todo.Adapter.NoteAdapter;
import com.example.todo.Model.NoteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddNote;

    private FirebaseFirestore firestore;
    private NoteAdapter noteAdapter;
    private List<NoteModel> noteList;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView2);
        fabAddNote = findViewById(R.id.fabAddNote);

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, noteList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(noteAdapter);

        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewNote.newInstance().show(getSupportFragmentManager(), AddNewNote.TAG);
            }
        });

        fetchData();
    }

    private void fetchData() {
        query = firestore.collection("notes").orderBy("timestamp", Query.Direction.DESCENDING);

        query.addSnapshotListener((value, error) -> {
            if (error != null) {
                Snackbar.make(findViewById(android.R.id.content), "Error: " + error.getMessage(), Snackbar.LENGTH_SHORT).show();
                return;
            }

            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String id = documentChange.getDocument().getId();
                    NoteModel note = documentChange.getDocument().toObject(NoteModel.class).withId(id);
                    noteList.add(note);
                    noteAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    public void onDialogClose() {
        noteList.clear();
        fetchData();
        noteAdapter.notifyDataSetChanged();
    }
}
