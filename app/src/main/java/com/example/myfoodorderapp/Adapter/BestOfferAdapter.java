package com.example.myfoodorderapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodorderapp.CustomFont.NativelyCustomTextView;
import com.example.myfoodorderapp.Model.BestDealModel;
import com.example.myfoodorderapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BestOfferAdapter extends RecyclerView.Adapter<BestOfferAdapter.MyViewHolder> {

    private List<BestDealModel> moviesList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public NativelyCustomTextView title;
        private CircleImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.product_title);
            imageView = view.findViewById(R.id.imageView);
        }
    }


    public BestOfferAdapter(List<BestDealModel> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.today_bestoffer_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BestDealModel movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.imageView.setImageResource(movie.getProductImage());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

