package com.example.myfoodorderapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myfoodorderapp.Model.Order;
import com.example.myfoodorderapp.R;

import java.util.List;

public class ProductsAdapter extends BaseAdapter {
    private Context context;
    private final List<Order> productList;

    //String BASE_ULR="http://smartbroshop.com/products/public";


    public ProductsAdapter(Context context, List<Order> productList) {
        this.context = context;
        this.productList = productList;
        Log.i("TAG", "LocationsAdapter: ");
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        gridView = inflater.inflate(R.layout.inner_listview_orders, parent, false);
       final TextView price=gridView.findViewById(R.id.product_price);
       final TextView quantity=gridView.findViewById(R.id.product_quantity);
       final TextView name=gridView.findViewById(R.id.product_name);

       price.setText("Rs"+productList.get(position).getPrice());
       quantity.setText("Qty:"+productList.get(position).getQuantity());
       name.setText(productList.get(position).getProductName());



        /*final LocationsViewHolder viewHolder = new LocationsViewHolder();
        viewHolder.locationText = gridView.findViewById(R.id.dropOffLocationTv);
        viewHolder.cancelIcon = gridView.findViewById(R.id.cancelBtn);

        viewHolder.locationText.setText(historyList.get(position).getLocationText());
        viewHolder.cancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i=0;i< LocationActivity.locationsList.size();i++){
                    if (LocationActivity.locationsList.get(i).getIndex()==historyList.get(position).getIndex()){
                        LocationActivity.locationsList.remove(i);
                    }
                }

                LocationActivity.noOfLocations--;
                historyList.remove(historyList.get(position));

                LocationActivity.locationsTextList=historyList;
                notifyDataSetChanged();
            }
        });
        Log.d("LIST", "locationsList before setting index : "+Utils.getGsonParser().toJson(LocationActivity.locationsList));
        for (int i=0;i<LocationActivity.locationsList.size();i++){
            LocationActivity.locationsList.get(i).setIndex(i);
        }
        for (int i=0;i<LocationActivity.locationsTextList.size();i++){
            LocationActivity.locationsTextList.get(i).setIndex(i);
        }
        Log.d("LIST", "locationsList After setting index : "+Utils.getGsonParser().toJson(LocationActivity.locationsList));


*/

        return gridView;
    }

    @Override
    public int getCount() {

        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}