package com.example.myfoodorderapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class TrackingOrderbyCustomer extends AppCompatActivity {

    PieChartView pieChartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_orderby_customer);

        pieChartView = findViewById(R.id.chart);

        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(15, Color.parseColor("#FF6500")).setLabel("Placed"));
        pieData.add(new SliceValue(15, Color.parseColor("#d8d8d8")).setLabel("Shipping"));
        pieData.add(new SliceValue(15, Color.parseColor("#d8d8d8")).setLabel("Shipped"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(10);
        pieChartData.setHasCenterCircle(true).setCenterText1("Order Tracking").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#FF6500"));
        pieChartView.setPieChartData(pieChartData);
    }
//    private void drawChart() {
//        PieChart pieChart = findViewById(R.id.pieChart);
//        pieChart.setUsePercentValues(true);
//
//        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
//        yvalues.add(new PieEntry(8f, "Placed", 0));
//        yvalues.add(new PieEntry(8f, "Shipping", 1));
//        yvalues.add(new PieEntry(8f, "Shipped", 2));
//
//        PieDataSet dataSet = new PieDataSet(yvalues, "Results");
//        PieData data = new PieData(dataSet);
//
//        data.setValueFormatter(new PercentFormatter());
//        pieChart.setData(data);
//        Description description = new Description();
//        description.setText(getString(R.string.pie_chart));
//        pieChart.setDescription(description);
//        pieChart.setDrawHoleEnabled(true);
//        pieChart.setTransparentCircleRadius(58f);
//        pieChart.setHoleRadius(58f);
//        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
//        data.setValueTextSize(13f);
//        data.setValueTextColor(Color.DKGRAY);
//
//    }
}
