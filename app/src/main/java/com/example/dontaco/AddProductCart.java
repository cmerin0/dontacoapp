package com.example.dontaco;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dontaco.datos.Order;
import com.example.dontaco.datos.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddProductCart extends AppCompatActivity {
    private FirebaseAuth mAuth;

    TextView textViewName, textViewPrice;
    EditText editTextQuantity;
    Button btnReturn, btnAddProduct;

    DatabaseReference mDatabase;

    String userId, productId, productName, productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_cart);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        Bundle extras = getIntent().getExtras();
        productId = extras.getString("id");
        productName = extras.getString("name");
        productPrice = extras.getString("price");

        initializeElements();
        initializeDatabase();
    }

    protected void initializeElements() {
        textViewName = findViewById(R.id.textViewName);
        textViewPrice = findViewById(R.id.textViewPrice);

        textViewName.setText("Nombre de producto: " + productName);
        textViewPrice.setText("Precio unitario: $" + productPrice);

        editTextQuantity = findViewById(R.id.editTextQuantity);

        btnReturn = findViewById(R.id.btnReturn);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        btnReturn.setOnClickListener(v -> {
            finish();
        });

        btnAddProduct.setOnClickListener(v -> {
            btnAddProductFunction();
        });
    };

    protected void initializeDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    protected void btnAddProductFunction() {
        if(editTextQuantity.getText().toString().equals("")) {
            Toast.makeText(AddProductCart.this, "Debe colocar la cantidad de productos que desea", Toast.LENGTH_LONG).show();
        }
        else {
            Order order = new Order(productId, productName, productPrice, editTextQuantity.getText().toString());

            try {
                mDatabase.child("cart").child(userId).push().setValue(order);

                Toast.makeText(AddProductCart.this, "El producto ha sido agregado correctamente al carrito", Toast.LENGTH_LONG).show();
                finish();
            }
            catch (Exception e){
                Toast.makeText(AddProductCart.this, "Hubo un error al agregar el producto al carrito", Toast.LENGTH_LONG).show();
            }
        }
    }
}