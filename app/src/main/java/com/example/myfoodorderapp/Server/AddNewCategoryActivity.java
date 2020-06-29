package com.example.myfoodorderapp.Server;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodorderapp.R;

import java.util.ArrayList;
import java.util.List;

public class AddNewCategoryActivity extends AppCompatActivity {

    Spinner spinner;
    List<String> list  = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_category_activity);

//        //Define spinner
//        spinner=(Spinner) findViewById(R.id.spinner_services);
//        spinnerAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, android.R.id.text1);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(spinnerAdapter);
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                int selectedPosition= position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        list.add("Baryani");
//        list.add("Pizza");
//        list.add("Burger");
//        list.add("Shawarma");
//        list.add("Others Fast Foods");
//
//        spinnerAdapter.addAll(list);
//        spinnerAdapter.notifyDataSetChanged();


    }
}