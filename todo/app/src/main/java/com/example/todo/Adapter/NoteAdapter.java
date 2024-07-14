package com.example.todo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.AddNewNote;
import com.example.todo.MainActivity2;
import com.example.todo.Model.NoteModel;
import com.example.todo.R;
import com.example.todo.login;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private List<NoteModel> notes;
    private MainActivity2 activity;
    private FirebaseFirestore firestore;

    public NoteAdapter(MainActivity2 activity, List<NoteModel> notes) {
        this.notes = notes;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firestore = FirebaseFirestore.getInstance();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_each_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteModel noteModel = notes.get(position);
        holder.title.setText(noteModel.getTitle());
        //holder.content.setText(noteModel.getContent());
        holder.timespan.setText(noteModel.getTimestamp());

        holder.itemView.setOnClickListener(v -> editTask(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void deleteTask(int position) {
        NoteModel noteModel = notes.get(position);
        firestore.collection(login.unotes).document(noteModel.getNoteId()).delete()
                .addOnSuccessListener(aVoid -> {
                    notes.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(activity, "Note deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(activity, "Error deleting note", Toast.LENGTH_SHORT).show();
                });
    }

    public void editTask(int position) {
        NoteModel noteModel = notes.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("title", noteModel.getTitle());
        bundle.putString("content", noteModel.getContent());
        bundle.putString("id", noteModel.getNoteId());
        AddNewNote addNewNote = new AddNewNote();
        addNewNote.setArguments(bundle);
        addNewNote.show(activity.getSupportFragmentManager(), addNewNote.getTag());
    }

    public Context getContext() {
        return activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, timespan;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
           // content = itemView.findViewById(R.id.textViewContent);
            timespan = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}
