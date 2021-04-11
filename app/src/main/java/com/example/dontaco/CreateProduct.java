package com.example.dontaco;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.dontaco.datos.Product;
import com.google.firebase.database.DatabaseReference;

public class CreateProduct extends AppCompatActivity {
    EditText editTextName, editTextPrice;

    Button btnReturn, btnCreate;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        initializeElements();
    }

    protected void initializeElements() {
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);

        btnReturn = findViewById(R.id.btnReturn);
        btnCreate = findViewById(R.id.btnCreate);

        btnReturn.setOnClickListener(v -> {
            finish();
        });

        btnCreate.setOnClickListener(v -> {
            Product product = new Product(editTextName.getText().toString(), editTextPrice.getText().toString());

            mDatabase.child("products").push().setValue(product);
        });
    }
}