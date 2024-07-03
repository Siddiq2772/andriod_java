package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LandingPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirestoreRecyclerAdapter<FireBaseModel, CompanyViewHolder> adapter;
    Button menu ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.companyRecyclerView);
        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(LandingPage.this, createrow.class);
            startActivityForResult(intent, 1); // Use startActivityForResult to get a result from createrow
        });

        if (firebaseUser != null) {
            Query query = firestore.collection("Notes")
                    .document(firebaseUser.getUid())
                    .collection("MyNotes")
                    .orderBy("Title", Query.Direction.ASCENDING);

            FirestoreRecyclerOptions<FireBaseModel> allusernotes = new FirestoreRecyclerOptions.Builder<FireBaseModel>()
                    .setQuery(query, FireBaseModel.class)
                    .build();

            adapter = new FirestoreRecyclerAdapter<FireBaseModel, CompanyViewHolder>(allusernotes) {
                @Override
                protected void onBindViewHolder(@NonNull CompanyViewHolder holder, int position, @NonNull FireBaseModel model) {
                    holder.TitleTextView.setText(model.getTitle());
                    holder.ContentTextView.setText(model.getContent());

                    // Set click listener for item actions (edit/delete)
                    holder.itemView.setOnClickListener(view -> {
                        int pos = holder.getBindingAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            showActionDialog(pos);
                        }
                    });
                }

                @NonNull
                @Override
                public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
                    return new CompanyViewHolder(view);
                }
            };

            recyclerView.setAdapter(adapter);

            // Add error listener to the query
            query.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    Log.w("LandingPage", "Error fetching documents.", e);
                    Toast.makeText(LandingPage.this, "Error fetching notes.", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            // Handle case where user is not logged in
            Toast.makeText(LandingPage.this, "Please log in to view notes.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LandingPage.this, MainActivity.class));
            finish(); // Finish LandingPage if not logged in
        }
        menu= findViewById(R.id.button3);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) { // Start listening only if adapter is initialized
            adapter.startListening();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            performLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showActionDialog(int position) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Choose Action")
                .setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {
                    if (which == 0) {
                        handleEditAction(position);
                    } else if (which == 1) {
                        handleDeleteAction(position);
                    }
                })
                .show();
    }

    private void handleEditAction(int position) {
        DocumentSnapshot documentSnapshot = adapter.getSnapshots().getSnapshot(position);
        String documentId = documentSnapshot.getId();

        Intent intent = new Intent(LandingPage.this, createrow.class);
        intent.putExtra("documentId", documentId);
        startActivityForResult(intent, 1); // Use startActivityForResult to get a result from createrow
    }

    private void handleDeleteAction(int position) {
        DocumentSnapshot documentSnapshot = adapter.getSnapshots().getSnapshot(position);
        String documentId = documentSnapshot.getId();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            firestore.collection("Notes")
                    .document(firebaseUser.getUid())
                    .collection("MyNotes")
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> Toast.makeText(LandingPage.this, "Note deleted successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                        Log.w("LandingPage", "Error deleting document", e);
                        Toast.makeText(LandingPage.this, "Error deleting note", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void performLogout() {
        mAuth.signOut();
        Intent intent = new Intent(LandingPage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public static class CompanyViewHolder extends RecyclerView.ViewHolder {
        TextView TitleTextView;
        TextView ContentTextView;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleTextView = itemView.findViewById(R.id.NameTextView);
            ContentTextView = itemView.findViewById(R.id.noteContent);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            adapter.notifyDataSetChanged(); // Refresh the adapter
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
