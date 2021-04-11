package com.example.dontaco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.dontaco.datos.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowProducts extends AppCompatActivity {
    Button btnReturn, btnCreateProduct;

    DatabaseReference mDatabase;

    ListView listViewProducts;
    ArrayList<Product> arrayListProducts;
    ArrayAdapter<Product> arrayAdapterProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);

        initializeElements();
        initializeDatabase();
    }

    protected void initializeElements() {
        btnReturn = findViewById(R.id.btnReturn);
        btnCreateProduct = findViewById(R.id.btnCreateProduct);

        btnReturn.setOnClickListener(v -> {
            finish();
        });

        btnCreateProduct.setOnClickListener(v -> {
            Intent i = new Intent(ShowProducts.this, CreateProduct.class);
            startActivity(i);
        });

        arrayListProducts = new ArrayList<>();
        arrayAdapterProducts = new ArrayAdapter<>(ShowProducts.this, android.R.layout.simple_list_item_1, arrayListProducts);

        listViewProducts = findViewById(R.id.listViewProducts);
        listViewProducts.setAdapter(arrayAdapterProducts);
    };

    protected void initializeDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(productListEvent());
    }

    protected ValueEventListener productListEvent() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListProducts.clear();

                for (DataSnapshot productSnapshot : snapshot.child("products").getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    arrayListProducts.add(product);
                }

                arrayAdapterProducts.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }
}