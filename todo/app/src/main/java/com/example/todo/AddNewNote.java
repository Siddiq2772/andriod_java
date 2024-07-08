// AddNewNote.java
package com.example.todo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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
    private String id = "";
    private String content = "";
    boolean isUpdate = false;
    private Context context;

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


        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("title");
            id = bundle.getString("id");
            content = bundle.getString("content");


            editTextTitle.setText(task);
            editTextContent.setText(content);


            if (task.length() <= 0){
                buttonSave.setEnabled(false);
                buttonSave.setBackgroundColor(Color.GRAY);
            }

        }



        buttonSave.setOnClickListener(v -> saveNoteToFirestore());
    }

    private void saveNoteToFirestore() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                return;
            }

            String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());
        if(isUpdate){

            firestore.collection("notes")
                    .document(id)
                    .update("title" , title , "content" , content,"timestamp",timestamp);
            dismiss();



        }else {

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

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), getTheme());
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof  OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}
