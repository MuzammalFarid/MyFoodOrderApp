package com.example.myfoodorderapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodorderapp.CustomFont.NativelyCustomTextView;
import com.example.myfoodorderapp.Interface.ItemClickListener;
import com.example.myfoodorderapp.Model.Order;
import com.example.myfoodorderapp.R;

import java.util.List;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder>{

    private List<Order> orders;
    private Context context;
    private ItemClickListener itemClickListener;

    public OrderSummaryAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private NativelyCustomTextView product_name, product_quantity,product_price;
        //        private ImageView imageView;
        ViewHolder(View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            product_quantity = itemView.findViewById(R.id.product_quantity);
            product_price = itemView.findViewById(R.id.product_price);
        }
    }

    @NonNull
    @Override
    public OrderSummaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inner_listview_orders, parent, false);
        final OrderSummaryAdapter.ViewHolder viewHolder = new OrderSummaryAdapter.ViewHolder(view);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull final OrderSummaryAdapter.ViewHolder holder, final int position) {
        holder.product_name.setText(orders.get(position).getProductName());
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }
}
