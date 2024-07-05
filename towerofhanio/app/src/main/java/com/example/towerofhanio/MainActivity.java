package com.example.towerofhanio;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

class demo extends Towerofhanio{

}
public class MainActivity extends AppCompatActivity {

    private final demo t = new demo();

    {
        findViewById(R.id.button);
    }

    private final TextView output = findViewById(R.id.textView) ;

    {
        findViewById(R.id.editTextNumber);
    }

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

        game();

    }
    void game(){
        while (t.top[2] != 2) {
            printStack();
            if(t.count%2!=0){
               // System.out.print("Enter to pop form: ");
                //t.pop(sc.nextInt());
            }

            else{
               // System.out.print("Enter to push to: ");
                // t.push(sc.nextInt());
            }

        }
        printStack();

    }


    void printStack(){
        output.setText("");
       // t.print();
        output.setText(t.s);
    }


}