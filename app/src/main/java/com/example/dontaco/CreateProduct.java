package com.example.dontaco;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dontaco.datos.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateProduct extends AppCompatActivity {
    EditText editTextName, editTextPrice;
    TextView textViewTitle;

    Button btnReturn, btnCreate;

    DatabaseReference mDatabase;

    boolean editProduct = false;
    String productId, productName, productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        Bundle extras = getIntent().getExtras();

        initializeElements();
        initializeDatabase();

        if(extras.getBoolean("edit")) {
            editProduct = true;
            productId = extras.getString("id");
            productName = extras.getString("name");
            productPrice = extras.getString("price");

            textViewTitle.setText("Editar producto");
            btnCreate.setText("Editar producto");
        }
    }

    protected void initializeElements() {
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);

        if(editProduct) {
            editTextName.setText(productName);
            editTextPrice.setText(productPrice);
        }

        btnReturn = findViewById(R.id.btnReturn);
        btnCreate = findViewById(R.id.btnCreate);

        btnReturn.setOnClickListener(v -> {
            finish();
        });

        btnCreate.setOnClickListener(v -> {
            btnCreateFunction();
        });
    }

    protected void initializeDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    protected void btnCreateFunction() {
        if(editTextName.getText().equals("") || editTextPrice.getText().equals("")) {
            Toast.makeText(CreateProduct.this, "Debe rellenar todos los campos", Toast.LENGTH_LONG).show();
        }
        else {
            Product product = new Product(editTextName.getText().toString(), editTextPrice.getText().toString());

            try {
                if(!editProduct) {
                    mDatabase.child("products").push().setValue(product);
                }
                else {
                    mDatabase.child("products").child(productId).setValue(product);
                }

                Toast.makeText(CreateProduct.this, "El producto ha sido agregado correctamente", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(CreateProduct.this, "Hubo un error al agregar el producto", Toast.LENGTH_LONG).show();
            }
        }
    }
}