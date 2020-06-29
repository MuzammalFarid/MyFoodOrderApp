package com.example.myfoodorderapp.Adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myfoodorderapp.CartActivity;
import com.example.myfoodorderapp.Common.Common;
import com.example.myfoodorderapp.Database.Database;
import com.example.myfoodorderapp.Interface.ItemClickListener;
import com.example.myfoodorderapp.Model.Order;
import com.example.myfoodorderapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Order> orders;
    private CartActivity context;
    private ItemClickListener itemClickListener;

    public CartAdapter(CartActivity context, List<Order> orders, ItemClickListener itemClickListener) {
        this.orders = orders;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView txt_cart_name, txt_price;
        public ElegantNumberButton btn_quantity;
        public ImageView cart_image;

        public RelativeLayout view_background;
        public LinearLayout view_foreground;

        //        private ImageView imageView;
        CartViewHolder(View itemView) {
            super(itemView);

            txt_cart_name = (TextView) itemView.findViewById(R.id.cart_item_name);
            txt_price = (TextView) itemView.findViewById(R.id.cart_item_price);
            btn_quantity = (ElegantNumberButton) itemView.findViewById(R.id.btn_quantity);
            cart_image = (ImageView) itemView.findViewById(R.id.cart_image);
            view_background = (RelativeLayout) itemView.findViewById(R.id.view_background);
            view_foreground = (LinearLayout) itemView.findViewById(R.id.view_foreground);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select action");
            menu.add(0,0, getAdapterPosition(), Common.DELETE);
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        final CartViewHolder viewHolder = new CartViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onclick(view, viewHolder.getAdapterPosition(), false);
            }
        });

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {

        holder.txt_cart_name.setText(orders.get(position).getProductName());

        Locale locale = new Locale("en", "US");
        final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        final int price = Integer.parseInt(orders.get(position).getPrice()) * Integer.parseInt(orders.get(position).getQuantity())
                - Integer.parseInt(orders.get(position).getDiscount()) * Integer.parseInt(orders.get(position).getQuantity());
        holder.txt_price.setText(numberFormat.format(price));
        holder.btn_quantity.setNumber(orders.get(position).getQuantity());

        holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                //Updating the price after quantity is changed
                int result = Integer.parseInt(orders.get(position).getPrice()) * newValue;
                holder.txt_price.setText(numberFormat.format(result));

                //Update database
                Order order = orders.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(context).updateCart(order);

                //Update total amount
                int total = 0;
                for (Order cartOrder : orders) {
                    total += (Integer.parseInt(cartOrder.getPrice()) * Integer.parseInt(cartOrder.getQuantity())
                            - Integer.parseInt(cartOrder.getDiscount()) * Integer.parseInt(cartOrder.getQuantity()));
                }
                context.textViewPrice.setText(String.format(" $%s", total));
            }
        });

//        TextDrawable textDrawable = TextDrawable.builder().buildRound(orders.get(position).getQuantity(), Color.RED);
//        holder.imageView.setImageDrawable(textDrawable);
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public Order getItem(int position) {
        return orders.get(position);
    }

    public void removeItem(int position) {
        orders.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item, int position) {
        orders.add(position, item);
        notifyItemInserted(position);
    }

}
