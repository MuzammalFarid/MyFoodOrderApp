package com.example.myfoodorderapp.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodorderapp.Adapter.ProductsAdapter;
import com.example.myfoodorderapp.Common.Common;
import com.example.myfoodorderapp.CustomFont.NativelyCustomTextView;
import com.example.myfoodorderapp.Model.Request;
import com.example.myfoodorderapp.R;
import com.example.myfoodorderapp.TrackingOrderbyCustomer;
import com.example.myfoodorderapp.UIUtils;
import com.example.myfoodorderapp.ViewHolders.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.Button;

public class OnGoingOrderFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference request;
    FirebaseRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ongoing_fragment_customer, container, false);

        //init Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");

        recyclerView = view.findViewById(R.id.recycler_order_status);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        showOrders(Common.currentUser.getPhone());

        return view;
    }

    private void showOrders(String phone) {
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(
                request.orderByChild("phone").equalTo(phone), Request.class).build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoing_order_list_customer, parent, false);
                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position, @NonNull final Request model) {

                NativelyCustomTextView textViewId = holder.itemView.findViewById(R.id.order_id);
                textViewId.setText(adapter.getRef(position).getKey());

                NativelyCustomTextView customerName = holder.itemView.findViewById(R.id.order_status);
                customerName.setText(Common.getStatus(model.getStatus()));

                NativelyCustomTextView dateTime = holder.itemView.findViewById(R.id.order_address);
                dateTime.setText(model.getAddress());

                NativelyCustomTextView totalAmount = holder.itemView.findViewById(R.id.order_price);
                totalAmount.setText(model.getTotal());

                NativelyCustomTextView order_datetime = holder.itemView.findViewById(R.id.order_datetime);
                order_datetime.setText(Common.getDate(Long.parseLong(adapter.getRef(position).getKey())));


                ListView listView = holder.itemView.findViewById(R.id.quantity_info);
                ProductsAdapter productsAdapter = new ProductsAdapter(getContext(), model.getOrders());
                listView.setAdapter(productsAdapter);
                UIUtils.setListViewHeightBasedOnItems(listView);


                if (model.getOrders().get(0) != null) {
                    Log.d("ORDERS", "onBindViewHolder: new orders products quantity  " + model.getOrders().size());

                }
                //For tracking pie chart
                final String placed = String.valueOf(model.getStatus().equals("0"));
                final String shipping = String.valueOf(model.getStatus().equals("1"));
                final String shipped = String.valueOf(model.getStatus().equals("2"));


                // For Tracking Orders
                Button tracking_order = holder.itemView.findViewById(R.id.tracking_order);
                tracking_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.currentKey = adapter.getRef(position).getKey();
                        if (model.getStatus().equals("1")) {
                            Intent trackingIntent = new Intent(getActivity(), TrackingOrderbyCustomer.class);
                            startActivity(trackingIntent);
                        } else
                            Toast.makeText(getActivity(), "You cannot track this Order before shipping!", Toast.LENGTH_SHORT).show();
                    }
                });

                //For Cancel Orders
                Button cancel_order = holder.itemView.findViewById(R.id.cancel_orders);
                cancel_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getStatus().equals("0"))
                            deleteOrder(adapter.getRef(position).getKey());
                        else
                            Toast.makeText(getActivity(), "You cannot delete this Order! Because your 0rder is shipping now", Toast.LENGTH_SHORT).show();
                    }
                });
                Button confirm_order = holder.itemView.findViewById(R.id.confirm_order);
                confirm_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getStatus().equals("2")) {
                            ConfirmReceiveOrder(adapter.getRef(position).getKey());
                        } else
                            Toast.makeText(getActivity(), "You cannot confirm receive this Order!", Toast.LENGTH_SHORT).show();
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void deleteOrder(final String key) {

        showConfirmDeleteDialog(key);
    }

    private void ConfirmReceiveOrder(String key) {

        showConfirmReceiveOrder(key);
    }

    private void showConfirmDeleteDialog(final String key) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialog.setTitle("Confirm Delete?");

        LayoutInflater inflater = this.getLayoutInflater();
        View confirm_delete_layout = inflater.inflate(R.layout.confirm_signout_layout, null);
        alertDialog.setView(confirm_delete_layout);
        alertDialog.setIcon(R.drawable.ic_delete_24);

        alertDialog.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                request.child(key).removeValue();
                Toast.makeText(getActivity(), new StringBuilder("Order" + " ")
                        .append(key)
                        .append(" " + "has been deleted").toString(), Toast.LENGTH_SHORT).show();

            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showConfirmReceiveOrder(final String key) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialog.setTitle("Confirm Receive?");

        LayoutInflater inflater = this.getLayoutInflater();
        View confirm_delete_layout = inflater.inflate(R.layout.confirm_signout_layout, null);
        alertDialog.setView(confirm_delete_layout);
        alertDialog.setIcon(R.drawable.ic_order_complete);

        alertDialog.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                request.child(key).removeValue();
                Toast.makeText(getActivity(), new StringBuilder("Order" + " ")
                        .append(key)
                        .append(" " + "has been confirm received").toString(), Toast.LENGTH_SHORT).show();

            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}