package com.example.dontaco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class CreateOrder extends AppCompatActivity {
    Button btnShowProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        initializeElements();
    }

    protected void initializeElements() {
        btnShowProducts = findViewById(R.id.btnShowProducts);

        btnShowProducts.setOnClickListener(v -> {
            Intent i = new Intent(CreateOrder.this, ShowProducts.class);
            startActivity(i);
        });
    };
}