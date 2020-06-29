package com.example.myfoodorderapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodorderapp.Adapter.CartAdapter;
import com.example.myfoodorderapp.CustomFont.NativelyCustomTextView;
import com.example.myfoodorderapp.Database.Database;
import com.example.myfoodorderapp.Helper.RecyclerItemTouchHelper;
import com.example.myfoodorderapp.Interface.ItemClickListener;
import com.example.myfoodorderapp.Interface.RecyclerItemTouchHelperListener;
import com.example.myfoodorderapp.Model.Order;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    public NativelyCustomTextView textViewPrice;
    LinearLayout buttonOrder;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference request;
    LinearLayout activity_cart;
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
        activity_cart = findViewById(R.id.activity_cart);

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
        //Swipe to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.CartViewHolder)
        {
            String name = ((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();

            final Order deleteItem = ((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());

            final int deleteIndex = viewHolder.getAdapterPosition();
            cartAdapter.removeItem(deleteIndex);

            new Database(getBaseContext()).cleanCart();

            //update txttotal
            //calculation total price
            float total = 0;
            List<Order> orders = new Database(getBaseContext()).getCarts();
            for(Order item:orders)
                total +=(Float.parseFloat(item.getPrice()))*(Integer.parseInt(item.getQuantity()));
            Locale locale = new Locale("en","MY");
            java.text.NumberFormat fmt = java.text.NumberFormat.getCurrencyInstance(locale);
            textViewPrice.setText(fmt.format(total));

            //snackbar
            Snackbar snackbar = Snackbar.make(activity_cart,name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreItem(deleteItem,deleteIndex);
                    new Database(getBaseContext()).addToCart(deleteItem);

                    //update txttotal
                    //calculation total price
                    float total = 0;
                    List<Order> orders = new Database(getBaseContext()).getCarts();
                    for(Order item:orders)
                        total +=(Float.parseFloat(item.getPrice()))*(Integer.parseInt(item.getQuantity()));
                    Locale locale = new Locale("en","MY");
                    java.text.NumberFormat fmt = java.text.NumberFormat.getCurrencyInstance(locale);
                    textViewPrice.setText(fmt.format(total));
                }
            });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    }

}
