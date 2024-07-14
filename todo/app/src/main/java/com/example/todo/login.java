package com.example.todo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class login extends AppCompatActivity {
    private DBHelper dbHelper = new DBHelper(this);
    private EditText eMail;
    private Button button;
    public StringBuffer userid = new StringBuffer();
    public static String utask;
    public static String unotes;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        eMail = findViewById(R.id.email);
        button = findViewById(R.id.login);
        String fetchedVariable = dbHelper.getVariable();

        // Use the fetched variable
        if (fetchedVariable != null) {
           userid = userid.append(fetchedVariable);
           utask = String.valueOf(userid.append("task"));
            unotes = String.valueOf(userid.append("notes"));
            Toast.makeText(login.this, "logged in to "+fetchedVariable, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(login.this , MainActivity.class));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    insertVariable(eMail.getText().toString());
                String fetchedVariable = dbHelper.getVariable();

                // Use the fetched variable
                if (fetchedVariable != null) {
                    userid = userid.append(fetchedVariable);
                    utask = String.valueOf(userid.append("task"));
                    unotes = String.valueOf(userid.append("notes"));

                    Toast.makeText(login.this, "logged in to "+fetchedVariable, Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(login.this , MainActivity.class));
            }
        });

    }
    private void insertVariable(String variableValue) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_VARIABLE, variableValue);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DBHelper.TABLE_NAME, null, values);
    }
}