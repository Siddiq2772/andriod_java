package com.example.todo;

import static com.example.todo.login.unotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.DialogFragment;

import com.example.todo.Model.NoteModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddNewNote extends DialogFragment {

    public static final String TAG = "AddNewNote";

    private EditText editTextTitle, editTextContent;
    private Button buttonSave;
    private String id = "";
    private String content = "";
    private boolean isUpdate = false;
    private Context context;
    private FirebaseFirestore firestore;

    public static AddNewNote newInstance() {
        return new AddNewNote();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_add_new_note, null);

        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextContent = view.findViewById(R.id.editTextContent);
        buttonSave = view.findViewById(R.id.buttonSave);

        firestore = FirebaseFirestore.getInstance();

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String title = bundle.getString("title");
            id = bundle.getString("id");
            content = bundle.getString("content");

            editTextTitle.setText(title);
            editTextContent.setText(content);

            if (title.length() <= 0) {
                buttonSave.setEnabled(false);
                buttonSave.setBackgroundColor(Color.GRAY);
            }
        }

        buttonSave.setOnClickListener(v -> saveNoteToFirestore());

        builder.setView(view)
                .setTitle(isUpdate ? "Update Note" : "Add Note")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            saveButton.setOnClickListener(v -> saveNoteToFirestore());
        });

        return dialog;
    }

    private void saveNoteToFirestore() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(context, "Title and Content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());

        if (isUpdate) {
            firestore.collection(unotes)
                    .document(id)
                    .update("title", title, "content", content, "timestamp", timestamp)
                    .addOnSuccessListener(aVoid -> {
                        dismiss();
                        ((OnDialogCloseListner) requireActivity()).onDialogClose();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error updating note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error updating note", e);
                        dismiss();
                    });
        } else {
            NoteModel note = new NoteModel(title, content, timestamp);

            firestore.collection(unotes)
                    .add(note)
                    .addOnSuccessListener(documentReference -> {
                        dismiss();
                        ((OnDialogCloseListner) requireActivity()).onDialogClose();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error saving note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error saving note", e);
                        dismiss();
                    });
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListner) {
            ((OnDialogCloseListner) activity).onDialogClose(dialog);
        }
    }
}
