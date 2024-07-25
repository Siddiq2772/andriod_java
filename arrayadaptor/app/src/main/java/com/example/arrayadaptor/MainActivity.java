package com.example.arrayadaptor;

import static android.provider.Contacts.SettingsColumns.KEY;
import static android.view.KeyEvent.KEYCODE_ENTER;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView list;
    private EditText editText ;
    private Button button;

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
        list = findViewById(R.id.listView);
        List arr = new ArrayList<String>();
        editText = findViewById(R.id.editTextText);
        arr.add("abc");


        // standrad adaptor
//        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arr);
//        list.setAdapter(ad);

        //custom adaptor
        customapdator ad= new customapdator(this,R.layout.activity_customapdator,arr);
        list.setAdapter(ad);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr.add(editText.getText().toString());
                list.setAdapter(ad);
            }
        });
    }
}