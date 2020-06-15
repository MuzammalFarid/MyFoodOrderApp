package com.example.myfoodorderapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodorderapp.CustomFont.NativelyCustomTextView;
import com.example.myfoodorderapp.Model.Order;
import com.example.myfoodorderapp.R;

import java.util.List;

public class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.MyViewHolder> {

    private List<Order> moviesList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public NativelyCustomTextView product_name;
        public NativelyCustomTextView product_quantity;
        public NativelyCustomTextView product_price;

        public MyViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.product_name);
            product_quantity = view.findViewById(R.id.product_quantity);
            product_price = view.findViewById(R.id.product_price);
        }
    }


    public InnerAdapter(List<Order> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inner_listview_orders, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order movie = moviesList.get(position);
        holder.product_name.setText(movie.getProductName());
        holder.product_quantity.setText(movie.getQuantity());
        holder.product_price.setText(movie.getPrice());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

