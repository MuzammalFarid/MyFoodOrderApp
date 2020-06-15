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

public class OnGoingOrderCustomerAdapter extends RecyclerView.Adapter<OnGoingOrderCustomerAdapter.MyViewHolder> {

    private List<BestDealModel> moviesList;
    private Context context;
    private final RecyclerView.RecycledViewPool recycledViewPool;
    InnerAdapter innerAdapter;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public NativelyCustomTextView title;
        private CircleImageView imageView;
        public RecyclerView rvOuter;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.order_id);
            imageView = view.findViewById(R.id.status_image);
            rvOuter = view.findViewById(R.id.quantity_info);

          //  setupRv();
        }

//        private void setupRv() {
//            rvOuter.setHasFixedSize(true);
//            rvOuter.setLayoutManager(new LinearLayoutManager(context));
//            innerAdapter = new InnerAdapter(moviesList, context);
//            rvOuter.setAdapter(innerAdapter);
//        }
    }

    public void addOuter(BestDealModel outer) {
        moviesList.add(outer);
        notifyDataSetChanged();
    }

    public OnGoingOrderCustomerAdapter(List<BestDealModel> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ongoing_order_list_customer, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BestDealModel movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.imageView.setImageResource(movie.getProductImage());
        holder.rvOuter.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

