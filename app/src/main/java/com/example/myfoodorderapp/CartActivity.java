package com.example.myfoodorderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodorderapp.Adapter.CartAdapter;
import com.example.myfoodorderapp.CustomFont.NativelyCustomTextView;
import com.example.myfoodorderapp.Database.Database;
import com.example.myfoodorderapp.Interface.ItemClickListener;
import com.example.myfoodorderapp.Model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public NativelyCustomTextView textViewPrice;
    LinearLayout buttonOrder;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference request;
    CartAdapter cartAdapter;
    List<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //init firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");

        orders = new ArrayList<>();
        textViewPrice = findViewById(R.id.order_price);
        buttonOrder = findViewById(R.id.btnPlaceOrder);

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewPrice.getText().toString().equals("$0.00")){
                    Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                } else{
                    Intent check_out = new Intent(CartActivity.this, CheckOutActivity.class);
                    startActivity(check_out);
                    finish();
                }

            }
        });

        recyclerView = findViewById(R.id.recycler_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        loadCart();
    }


    //Helper Methods
    private void loadCart() {
        orders = new Database(this).getCarts();
        cartAdapter = new CartAdapter(this, orders, new ItemClickListener() {
            @Override
            public void onclick(View view, int position, boolean isLongClick) {
                Toast.makeText(CartActivity.this, orders.get(position).getProductName(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(cartAdapter);

        int total = 0;
        //Calculating total price
        for (Order order : orders) {
            total += (Integer.parseInt(order.getPrice()) * Integer.parseInt(order.getQuantity())
                    - Integer.parseInt(order.getDiscount()) * Integer.parseInt(order.getQuantity()));
        }
        textViewPrice.setText(String.format(" $%s", total));
    }

//    private void showDialog() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("One more step!");
//        builder.setMessage("Enter your Address: ");
//        final EditText editText = new EditText(this);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        editText.setLayoutParams(layoutParams);
//        builder.setView(editText);
//        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
//
//        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Request req = new Request(Common.currentUser.getName(),
//                        Common.currentUser.getPhone(),
//                        editText.getText().toString(),
//                        textViewPrice.getText().toString(),
//                        "Home Delivery",
//                        orders);
//
//                //sending to firebase
//                request.child(String.valueOf(System.currentTimeMillis())).setValue(req);
//
//                new Database(CartActivity.this).cleanCart();
//                Toast.makeText(CartActivity.this, "Order is placed. Thank You!", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
//
//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//
//        builder.show();
//    }

}
