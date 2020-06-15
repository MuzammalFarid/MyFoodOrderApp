package com.example.myfoodorderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.rey.material.widget.Button;

public class OrderCompleteActivity extends AppCompatActivity {

    Button my_orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);

        my_orders = findViewById(R.id.my_orders);

        my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderCompleteActivity.this, MyOrdersActivity.class));
                finish();
            }
        });
    }
}
