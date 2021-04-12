package com.example.dontaco;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class ShowOrders extends AppCompatActivity {
    private FirebaseAuth mAuth;

    Button btnReturn;

    DatabaseReference mDatabase;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_orders);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        initializeElements();
    }

    protected void initializeElements() {
        btnReturn = findViewById(R.id.btnReturn);

        btnReturn.setOnClickListener(v -> {
            finish();
        });
    };
}