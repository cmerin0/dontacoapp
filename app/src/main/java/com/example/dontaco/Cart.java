package com.example.dontaco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Cart extends AppCompatActivity {
    private FirebaseAuth mAuth;

    TextView textViewQuantity, textViewTotal;
    Button btnReturn, btnFinishOrder;

    DatabaseReference mDatabase;

    ListView listViewProducts;
    ArrayList<Order> arrayListOrder;
    ArrayAdapter<Order> arrayAdapterOrders;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        initializeElements();
        initializeDatabase();
    }

    protected void initializeElements() {
        textViewQuantity = findViewById(R.id.textViewQuantity);
        textViewTotal = findViewById(R.id.textViewTotal);

        btnReturn = findViewById(R.id.btnReturn);
        btnFinishOrder = findViewById(R.id.btnFinishOrder);

        btnReturn.setOnClickListener(v -> {
            finish();
        });

        btnFinishOrder.setOnClickListener(v -> {

        });

        arrayListOrder = new ArrayList<>();
        arrayAdapterOrders = new ArrayAdapter<>(Cart.this, android.R.layout.simple_list_item_1, arrayListOrder);

        listViewProducts = findViewById(R.id.listViewProducts);
        listViewProducts.setAdapter(arrayAdapterOrders);

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Cart.this, CreateProduct.class);
                i.putExtra("edit", true);
                i.putExtra("id", arrayListOrder.get(position).getId());
                i.putExtra("productId", arrayListOrder.get(position).getProductId());
                i.putExtra("productName", arrayListOrder.get(position).getProductName());
                i.putExtra("productPrice", arrayListOrder.get(position).getProductPrice());
                i.putExtra("quantity", arrayListOrder.get(position).getQuantity());
                i.putExtra("total", arrayListOrder.get(position).getTotal());
                startActivity(i);
            }
        });

        listViewProducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);

                builder.setMessage("¿Está seguro de eliminar este producto del carrito?").setTitle("Confirmación");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            mDatabase.child("cart").child(userId).child(arrayListOrder.get(position).getId()).removeValue();
                            Toast.makeText(Cart.this, "El producto ha sido eliminado correctamente del carrito", Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e){
                            Toast.makeText(Cart.this, "Hubo un error al eliminar el producto del carrito", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });
    };

    protected void initializeDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(orderListEvent());
    }

    protected ValueEventListener orderListEvent() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int quantityProducts = 0;
                double sumTotals = 0;

                arrayListOrder.clear();

                for (DataSnapshot orderSnapshot : snapshot.child("cart").child(userId).getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    order.setId(orderSnapshot.getKey());

                    arrayListOrder.add(order);
                    arrayAdapterOrders.notifyDataSetChanged();

                    quantityProducts++;
                    sumTotals += Double.parseDouble(order.getTotal());
                }

                textViewQuantity.setText("Cantidad de productos: " + quantityProducts);
                textViewTotal.setText("Total a pagar: $" + sumTotals);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }
}