package com.example.myfoodorderapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodorderapp.Common.Common;
import com.example.myfoodorderapp.Database.Database;
import com.example.myfoodorderapp.FoodDetailActivity;
import com.example.myfoodorderapp.Interface.ItemClickListener;
import com.example.myfoodorderapp.Model.Favorites;
import com.example.myfoodorderapp.Model.Order;
import com.example.myfoodorderapp.R;
import com.example.myfoodorderapp.ViewHolders.FavoritesViewHolder;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private Context context;
    private List<Favorites> favoritesList;

    public FavoritesAdapter(Context context, List<Favorites> favoritesList) {
        this.context = context;
        this.favoritesList = favoritesList;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.favorites_item, parent, false);
        return new FavoritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder viewHolder, final int position) {
        viewHolder.food_name.setText(favoritesList.get(position).getProductName());
        viewHolder.food_price.setText(String.format("RM %s", favoritesList.get(position).getPrice().toString()));

        //Quick cart

        viewHolder.quick_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isExists = new Database(context).checkFoodExists(favoritesList.get(position).getProductId(), Common.currentUser.getPhone());

                if (!isExists) {
                    new Database(context).addToCart(new Order(
                            favoritesList.get(position).getProductId(),
                            favoritesList.get(position).getProductName(),
                            "1",
                            favoritesList.get(position).getPrice(),
                            favoritesList.get(position).getFoodImage()

                    ));
                } else {
                    new Database(context).increaseCart(Common.currentUser.getPhone(),
                            favoritesList.get(position).getProductId());
                }
                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        final Favorites local = favoritesList.get(position);
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onclick(View view, int position, boolean isLongClick) {
                //start new activity
                Intent foodDetail = new Intent(context, FoodDetailActivity.class);
                foodDetail.putExtra("FoodId", favoritesList.get(position).getProductId()); //send FoodId to new activity
                context.startActivity(foodDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public void removeItem(int position) {
        favoritesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Favorites item, int position) {
        favoritesList.add(position, item);
        notifyItemInserted(position);
    }

    public Favorites getItem(int position) {
        return favoritesList.get(position);
    }
}

