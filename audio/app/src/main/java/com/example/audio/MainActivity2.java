package com.example.audio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    private SeekBar seekBar;
    private MediaPlayer mp = new MediaPlayer();
    private Button pause,next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.list);

        seekBar = findViewById(R.id.seekBar2);
        next = findViewById(R.id.button2);
        pause = findViewById(R.id.button4);
        mp= MediaPlayer.create(this,R.raw.a2);
        mp.start();
        if(mp.isPlaying()){
            mp.pause();
            pause.setText("Play");
        }
        else{
            mp.start();
            pause.setText("Pause");
        }

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    mp.pause();
                    pause.setText("Play");
                }
                else{
                    mp.start();
                    pause.setText("Pause");
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                Intent intent =new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        seekBar.setMax(mp.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mp.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }
}