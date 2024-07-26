package com.example.sharedprefrence;

import static android.provider.Contacts.SettingsColumns.KEY;
import static android.view.KeyEvent.KEYCODE_ENTER;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editText ;
    private Button button;
    String arr = "";
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


        editText = findViewById(R.id.editTextText);
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        arr=sp.getString("data","");
        ed.putString("data", arr);
        ed.apply();
        if(!(arr.length()==0))
            editText.setText(arr);


        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr = editText.getText().toString();
                ed.putString("data", arr);
                ed.apply();
                editText.setText(arr);
            }
        });
    }
}