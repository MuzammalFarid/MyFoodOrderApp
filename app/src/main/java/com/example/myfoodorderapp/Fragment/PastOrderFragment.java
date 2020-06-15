package com.example.myfoodorderapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodorderapp.Adapter.CartAdapter;
import com.example.myfoodorderapp.CustomFont.NativelyCustomTextView;
import com.example.myfoodorderapp.Model.Order;
import com.example.myfoodorderapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PastOrderFragment extends Fragment {

    RecyclerView recyclerView;
    public NativelyCustomTextView textViewPrice;
    LinearLayout buttonOrder;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference request;
    CartAdapter cartAdapter;
    List<Order> orders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ongoing_fragment_customer, container, false);
        //init firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");

        orders = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_order_status);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

//        loadCart();

        return view;
    }

//    private void loadCart() {
//        orders = new Database(getContext()).getCarts();
//        cartAdapter = new CartAdapter(getActivity(), orders, new ItemClickListener() {
//            @Override
//            public void onclick(View view, int position, boolean isLongClick) {
//                Toast.makeText(getActivity(), orders.get(position).getProductName(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        recyclerView.setAdapter(cartAdapter);
//    }
}
