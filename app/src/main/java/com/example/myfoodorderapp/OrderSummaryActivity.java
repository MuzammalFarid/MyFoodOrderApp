package com.example.myfoodorderapp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodorderapp.CustomFont.NativelyCustomTextView;

public class OrderSummaryActivity extends AppCompatActivity {

    private ImageView image_product;
    private NativelyCustomTextView user_name,user_phonenumber,order_number,total_price,discount,delivery_charges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        user_name = findViewById(R.id.user_name);
        user_phonenumber = findViewById(R.id.user_phonenumber);
        order_number = findViewById(R.id.order_number);
        total_price = findViewById(R.id.total_price);
        discount = findViewById(R.id.discount);
        delivery_charges = findViewById(R.id.delivery_charges);

    }
}
