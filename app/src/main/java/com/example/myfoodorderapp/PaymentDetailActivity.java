package com.example.myfoodorderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.rey.material.widget.Button;

public class PaymentDetailActivity extends AppCompatActivity {

    Button payment_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        payment_detail = findViewById(R.id.payment_detail);

        payment_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentDetailActivity.this, OrderCompleteActivity.class));
                finish();
            }
        });
    }
}
