package com.example.dontaco;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class ShowOrders extends AppCompatActivity {
    Button btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_orders);

        initializeElements();
    }

    protected void initializeElements() {
        btnReturn = findViewById(R.id.btnReturn);

        btnReturn.setOnClickListener(v -> {
            finish();
        });
    };
}