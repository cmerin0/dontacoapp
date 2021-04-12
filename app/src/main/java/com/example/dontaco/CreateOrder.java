package com.example.dontaco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dontaco.datos.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateOrder extends AppCompatActivity {
    private FirebaseAuth mAuth;

    TextView textViewUser;
    Button btnShowProducts, btnShowOrders, btnCart, btnLogout;

    DatabaseReference mDatabase;

    ListView listViewProducts;
    ArrayList<Product> arrayListProducts;
    ArrayAdapter<Product> arrayAdapterProducts;

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userEmail = user.getEmail();

        initializeElements();
        initializeDatabase();
    }

    protected void initializeElements() {
        btnShowProducts = findViewById(R.id.btnShowProducts);
        btnShowOrders = findViewById(R.id.btnShowOrders);
        btnCart = findViewById(R.id.btnCart);
        btnLogout = findViewById(R.id.btnLogout);

        textViewUser = findViewById(R.id.textViewUser);
        textViewUser.setText("Bienvenido, " + userEmail);

        btnShowProducts.setOnClickListener(v -> {
            Intent i = new Intent(CreateOrder.this, ShowProducts.class);
            startActivity(i);
        });

        btnShowOrders.setOnClickListener(v -> {
            Intent i = new Intent(CreateOrder.this, ShowOrders.class);
            startActivity(i);
        });

        btnCart.setOnClickListener(v -> {
            Intent i = new Intent(CreateOrder.this, Cart.class);
            startActivity(i);
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();

            Intent i = new Intent(CreateOrder.this, Login.class);
            startActivity(i);
        });

        arrayListProducts = new ArrayList<>();
        arrayAdapterProducts = new ArrayAdapter<>(CreateOrder.this, android.R.layout.simple_list_item_1, arrayListProducts);

        listViewProducts = findViewById(R.id.listViewProducts);
        listViewProducts.setAdapter(arrayAdapterProducts);

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(CreateOrder.this, AddProductCart.class);
                i.putExtra("editOrderProduct", false);
                i.putExtra("productId", arrayListProducts.get(position).getId());
                i.putExtra("productName", arrayListProducts.get(position).getName());
                i.putExtra("productPrice", arrayListProducts.get(position).getPrice());
                startActivity(i);
            }
        });
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
                    product.setId(productSnapshot.getKey());

                    arrayListProducts.add(product);
                    arrayAdapterProducts.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }
}