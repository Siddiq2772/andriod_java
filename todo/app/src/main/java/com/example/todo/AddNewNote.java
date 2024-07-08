// AddNewNote.java
package com.example.todo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.todo.Model.NoteModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddNewNote extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewNote";

    private EditText editTextTitle, editTextContent;
    private Button buttonSave;

    private FirebaseFirestore firestore;

    public static AddNewNote newInstance() {
        return new AddNewNote();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_add_new_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextContent = view.findViewById(R.id.editTextContent);
        buttonSave = view.findViewById(R.id.buttonSave);

        firestore = FirebaseFirestore.getInstance();

        buttonSave.setOnClickListener(v -> saveNoteToFirestore());
    }

    private void saveNoteToFirestore() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            return;
        }

        String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());

        NoteModel note = new NoteModel(title, content, timestamp);

        firestore.collection("notes")
                .add(note)
                .addOnSuccessListener(documentReference -> {
                    dismiss();
                    ((OnDialogCloseListner) requireActivity()).onDialogClose();
                })
                .addOnFailureListener(e -> {
                    dismiss();
                });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), getTheme());
    }
}
