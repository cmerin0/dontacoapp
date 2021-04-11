package com.example.dontaco;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                finish();
            }
        };

        Timer time = new Timer();
        time.schedule(task, 5000);
    }
}