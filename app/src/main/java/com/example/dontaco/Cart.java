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
import com.example.dontaco.datos.OrderProduct;
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
    ArrayList<OrderProduct> arrayListOrderProduct;
    ArrayAdapter<OrderProduct> arrayAdapterOrders;

    String userId;
    int quantityProducts = 0;
    double sumTotals = 0;

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
            finishOrder();
        });

        arrayListOrderProduct = new ArrayList<>();
        arrayAdapterOrders = new ArrayAdapter<>(Cart.this, android.R.layout.simple_list_item_1, arrayListOrderProduct);

        listViewProducts = findViewById(R.id.listViewProducts);
        listViewProducts.setAdapter(arrayAdapterOrders);

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Cart.this, CreateProduct.class);
                i.putExtra("edit", true);
                i.putExtra("id", arrayListOrderProduct.get(position).getId());
                i.putExtra("productId", arrayListOrderProduct.get(position).getProductId());
                i.putExtra("productName", arrayListOrderProduct.get(position).getProductName());
                i.putExtra("productPrice", arrayListOrderProduct.get(position).getProductPrice());
                i.putExtra("quantity", arrayListOrderProduct.get(position).getQuantity());
                i.putExtra("total", arrayListOrderProduct.get(position).getTotal());
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
                            mDatabase.child("cart").child(userId).child(arrayListOrderProduct.get(position).getId()).removeValue();
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
                quantityProducts = 0;
                sumTotals = 0;

                arrayListOrderProduct.clear();

                for (DataSnapshot orderSnapshot : snapshot.child("cart").child(userId).getChildren()) {
                    OrderProduct orderProduct = orderSnapshot.getValue(OrderProduct.class);
                    orderProduct.setId(orderSnapshot.getKey());

                    arrayListOrderProduct.add(orderProduct);
                    arrayAdapterOrders.notifyDataSetChanged();

                    quantityProducts++;
                    sumTotals += Double.parseDouble(orderProduct.getTotal());
                }

                textViewQuantity.setText("Cantidad de productos: " + quantityProducts);
                textViewTotal.setText("Total a pagar: $" + sumTotals);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
    }

    protected void finishOrder() {
        Order order = new Order(String.valueOf(quantityProducts), String.valueOf(sumTotals));

        if(quantityProducts > 0) {
            try {
                mDatabase.child("orders").child(userId).push().setValue(order);
                mDatabase.child("cart").child(userId).removeValue();
                Toast.makeText(Cart.this, "La orden se ha procesado correctamente", Toast.LENGTH_LONG).show();
                finish();
            }
            catch (Exception e){
                Toast.makeText(Cart.this, "Hubo un error procesar la orden", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(Cart.this, "Debe de agregar al menos un producto al carrito para procesar el pedido", Toast.LENGTH_LONG).show();
        }
    }
}