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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
            i.putExtra("edit", false);
            startActivity(i);
        });

        arrayListProducts = new ArrayList<>();
        arrayAdapterProducts = new ArrayAdapter<>(ShowProducts.this, android.R.layout.simple_list_item_1, arrayListProducts);

        listViewProducts = findViewById(R.id.listViewProducts);
        listViewProducts.setAdapter(arrayAdapterProducts);

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ShowProducts.this, CreateProduct.class);
                i.putExtra("edit", true);
                i.putExtra("id", arrayListProducts.get(position).getId());
                i.putExtra("name", arrayListProducts.get(position).getName());
                i.putExtra("price", arrayListProducts.get(position).getPrice());
                startActivity(i);
            }
        });

        listViewProducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowProducts.this);

                builder.setMessage("¿Está seguro de eliminar este producto?").setTitle("Confirmación");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            mDatabase.child("products").child(arrayListProducts.get(position).getId()).removeValue();
                            Toast.makeText(ShowProducts.this, "El producto ha sido eliminado correctamente", Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e){
                            Toast.makeText(ShowProducts.this, "Hubo un error al eliminar el producto", Toast.LENGTH_LONG).show();
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