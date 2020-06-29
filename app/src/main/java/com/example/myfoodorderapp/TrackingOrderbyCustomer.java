package com.example.myfoodorderapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import lecho.lib.hellocharts.view.PieChartView;

public class TrackingOrderbyCustomer extends AppCompatActivity {

    PieChartView pieChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_orderby_customer);
        pieChartView = findViewById(R.id.chart);
    }
}
