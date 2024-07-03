package com.example.towerofhanio;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.towerofhanio.R;
import com.example.towerofhanio.towerofhanio;

public class MainActivity extends AppCompatActivity {

    towerofhanio t ;
    private Button button ;
    private TextView output ;
    private EditText input ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        button = findViewById(R.id.button);
        output = findViewById(R.id.textView);
        input = findViewById(R.id.editTextNumber);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        game();

    }
    void game(){
        while (t.top[2] != 2) {
            printStack();
            if(t.count%2!=0){
                System.out.print("Enter to pop form: ");
                //t.pop(sc.nextInt());
            }

            else{
                System.out.print("Enter to push to: ");
                // t.push(sc.nextInt());
            }

        }
        printStack();

    }


    void printStack(){
        output.setText("");
        t.print();
        output.setText(t.s);
    }
    int input(){
        int in=0;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return in;
    }


}