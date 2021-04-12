package com.example.dontaco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.dontaco.datos.Order;
import com.example.dontaco.datos.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowOrders extends AppCompatActivity {
    private FirebaseAuth mAuth;

    Button btnReturn;

    DatabaseReference mDatabase;

    ListView listViewOrders;
    ArrayList<Order> arrayListOrders;
    ArrayAdapter<Order> arrayAdapterOrders;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_orders);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        initializeElements();
        initializeDatabase();
    }

    protected void initializeElements() {
        btnReturn = findViewById(R.id.btnReturn);

        btnReturn.setOnClickListener(v -> {
            finish();
        });

        arrayListOrders = new ArrayList<>();
        arrayAdapterOrders = new ArrayAdapter<>(ShowOrders.this, android.R.layout.simple_list_item_1, arrayListOrders);

        listViewOrders = findViewById(R.id.listViewOrders);
        listViewOrders.setAdapter(arrayAdapterOrders);
    };

    protected void initializeDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(orderListEvent());
    }

    protected ValueEventListener orderListEvent() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListOrders.clear();

                for (DataSnapshot orderSnapshot : snapshot.child("orders").child(userId).getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);

                    arrayListOrders.add(order);
                    arrayAdapterOrders.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }
}