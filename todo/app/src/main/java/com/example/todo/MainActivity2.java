// MainActivity2.java
package com.example.todo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todo.Adapter.NoteAdapter;
import com.example.todo.Adapter.ToDoAdapter;
import com.example.todo.Model.NoteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements OnDialogCloseListner {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddNote;
    private FirebaseFirestore firestore;
    private NoteAdapter noteAdapter;
    private RadioGroup radioGroup;

    private List<NoteModel> noteList;
    private Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        firestore = FirebaseFirestore.getInstance();
        radioGroup = findViewById(R.id.radioGroup);

        recyclerView = findViewById(R.id.recyclerView2);
        fabAddNote = findViewById(R.id.fabAddNote);


        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, noteList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                checkedId =radioGroup.getCheckedRadioButtonId();
                RadioButton selectedButton = findViewById(checkedId);
                Intent intent = null;
                if (checkedId == R.id.radioButtonToDo)
                {
                    intent = new Intent(MainActivity2.this, MainActivity.class);
                    startActivityWithAnimation(intent);
                }
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(noteAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
        query = firestore.collection(login.unotes).orderBy("timestamp", Query.Direction.DESCENDING);

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

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        noteList.clear();
        fetchData();
        noteAdapter.notifyDataSetChanged();
    }
    private void startActivityWithAnimation(Intent intent) {
        // Apply animation
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(intent);
    }
    @Override
    public void onDialogClose() {

    }
}
