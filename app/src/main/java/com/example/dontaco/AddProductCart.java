package com.example.dontaco;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dontaco.datos.OrderProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProductCart extends AppCompatActivity {
    private FirebaseAuth mAuth;

    TextView textViewTitle, textViewName, textViewPrice;
    EditText editTextQuantity;
    Button btnReturn, btnAddProduct;

    DatabaseReference mDatabase;

    boolean editOrderProduct;
    String orderProductId, userId, productId, productName, productPrice, productQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_cart);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        Bundle extras = getIntent().getExtras();
        editOrderProduct = extras.getBoolean("editOrderProduct");

        productId = extras.getString("productId");
        productName = extras.getString("productName");
        productPrice = extras.getString("productPrice");

        if(editOrderProduct) {
            orderProductId = extras.getString("orderProductId");
            productQuantity = extras.getString("productQuantity");
        }

        initializeElements();
        initializeDatabase();
    }

    protected void initializeElements() {
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewName = findViewById(R.id.textViewName);
        textViewPrice = findViewById(R.id.textViewPrice);

        textViewName.setText("Nombre de producto: " + productName);
        textViewPrice.setText("Precio unitario: $" + productPrice);

        editTextQuantity = findViewById(R.id.editTextQuantity);

        btnReturn = findViewById(R.id.btnReturn);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        if(editOrderProduct) {
            textViewTitle.setText("Modificar producto del carrito");
            editTextQuantity.setText(productQuantity);
            btnAddProduct.setText("Modificar producto");
        }

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
            OrderProduct orderProduct = new OrderProduct(productId, productName, productPrice, editTextQuantity.getText().toString());

            try {
                if(!editOrderProduct) {
                    mDatabase.child("cart").child(userId).push().setValue(orderProduct);
                    Toast.makeText(AddProductCart.this, "El producto ha sido agregado correctamente al carrito", Toast.LENGTH_LONG).show();
                }
                else {
                    mDatabase.child("cart").child(userId).child(orderProductId).setValue(orderProduct);
                    Toast.makeText(AddProductCart.this, "El producto a comprar se ha modificado correctamente", Toast.LENGTH_LONG).show();
                }


                finish();
            }
            catch (Exception e){
                Toast.makeText(AddProductCart.this, "Hubo un error al agregar el producto al carrito", Toast.LENGTH_LONG).show();
            }
        }
    }
}