//package com.example.myfoodorderapp;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.RelativeLayout;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.myfoodorderapp.Adapter.FavoritesAdapter;
//import com.example.myfoodorderapp.Common.Common;
//import com.example.myfoodorderapp.Database.Database;
//import com.example.myfoodorderapp.Helper.RecyclerItemTouchHelper;
//import com.example.myfoodorderapp.Interface.RecyclerItemTouchHelperListener;
//import com.example.myfoodorderapp.Model.Favorites;
//import com.example.myfoodorderapp.ViewHolders.FavoritesViewHolder;
//import com.google.android.material.snackbar.Snackbar;
//
//public class FavoritesActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {
//
//    RecyclerView recyclerView;
//    RecyclerView.LayoutManager layoutManager;
//
//    FavoritesAdapter adapter;
//    RelativeLayout rootLayout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_favorites);
//
//        rootLayout = (RelativeLayout)findViewById(R.id.root_layout);
//
//        recyclerView = (RecyclerView)findViewById(R.id.recycler_fav);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        //Swipe to delete
//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
//
//        loadFavorites();
//    }
//
//    private void loadFavorites() {
//        adapter = new FavoritesAdapter(this, new Database(this).getAllFavorites(Common.currentUser.getPhone()));
//        recyclerView.setAdapter(adapter);
//    }
//
//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
//        if (viewHolder instanceof FavoritesViewHolder)
//        {
//            String name = ((FavoritesAdapter)recyclerView.getAdapter()).getItem(position).getFoodName();
//
//            final Favorites deleteItem = ((FavoritesAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
//            final int deleteIndex = viewHolder.getAdapterPosition();
//
//            adapter.removeItem(viewHolder.getAdapterPosition());
//            new Database(getBaseContext()).removeFromFavourites(deleteItem.getFoodId(), Common.currentUser.getPhone());
//
//            Snackbar snackbar = Snackbar.make(rootLayout,name + " removed from favorites!", Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    adapter.restoreItem(deleteItem,deleteIndex);
//                    new Database(getBaseContext()).addToFavourites(deleteItem);
//
//
//
//                }
//            });
//            snackbar.setActionTextColor(Color.RED);
//            snackbar.show();
//        }
//    }
//}
//
