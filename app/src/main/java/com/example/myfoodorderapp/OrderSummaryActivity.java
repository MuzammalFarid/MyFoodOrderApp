package com.example.myfoodorderapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodorderapp.Adapter.OrderSummaryAdapter;
import com.example.myfoodorderapp.Common.Common;
import com.example.myfoodorderapp.CustomFont.NativelyCustomTextView;
import com.example.myfoodorderapp.Database.Database;
import com.example.myfoodorderapp.Model.Order;
import com.example.myfoodorderapp.Model.Request;
import com.example.myfoodorderapp.ViewHolders.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rey.material.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class OrderSummaryActivity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private ImageView image_product;
    private NativelyCustomTextView user_name, user_phonenumber, order_number, total_price, discounts, delivery_charges, address_details;
    List<Order> orders;
    private RecyclerView rv_product_infos;
    private OrderSummaryAdapter productsAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference request;
    FirebaseRecyclerAdapter adapter;
    Button place_order;
    //location
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private static final int UPDATE_INTERVAL = 5000;
    private static final int FASTEST_INTERVAL = 3000;
    private static final int DISPLACEMENT = 10;
    private static final int LOCATION_REQUEST_CODE = 9999;
    private static final int PLAY_SERVICES_REQUEST = 9997;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        user_name = findViewById(R.id.user_name);
        user_phonenumber = findViewById(R.id.user_phonenumber);
        order_number = findViewById(R.id.order_number);
        total_price = findViewById(R.id.total_price);
        discounts = findViewById(R.id.discount);
        delivery_charges = findViewById(R.id.delivery_charges);
        address_details = findViewById(R.id.address_details);
        place_order = findViewById(R.id.place_order);
        //init Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");

        rv_product_infos = findViewById(R.id.rv_product_info);
        rv_product_infos.setHasFixedSize(true);
        rv_product_infos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        orders = new ArrayList<>();

        Intent intent = getIntent();
        String Name = intent.getStringExtra("name");
        String Phone = intent.getStringExtra("phone");
        String Address = intent.getStringExtra("address");
        String TotalCost = intent.getStringExtra("totalcost");

        user_name.setText(Name);
        user_phonenumber.setText(Phone);
        order_number.setText(Address);
        address_details.setText(Address);
        // fetch the price
        int total = 0;
        orders = new Database(this).getCarts();
        for (Order order : orders) {
            total += (Integer.parseInt(order.getPrice()) * Integer.parseInt(order.getQuantity())
                    - Integer.parseInt(order.getDiscount()) * Integer.parseInt(order.getQuantity()));
        }
        total_price.setText(String.format(" $%s", total));

        showOrders(Common.currentUser.getPhone());

        //Runtime permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this, new String[]
                    {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },LOCATION_REQUEST_CODE);
        }else
        {
            if (checkPlayServices()) //if have play service on device
            {
                buildGoogleApiClient();
                createLocationRequest();
            }
        }

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(OrderSummaryActivity.this,R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.order_place_dialog, viewGroup, false);
                android.widget.Button btnCancelId=dialogView.findViewById(R.id.btnCancelId);
                android.widget.Button btnReopenId=dialogView.findViewById(R.id.btnReopenId);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

                btnReopenId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Request req = new Request(Common.currentUser.getName(),
                                Common.currentUser.getPhone(),
                                order_number.getText().toString(),
                                total_price.getText().toString(),
                                "Cash On Delivery",
                                String.format("%s,%s", mLastLocation.getLatitude(), mLastLocation.getLongitude()),
                                refreshedToken,
                                orders);
                        //sending to firebase
                        request.child(String.valueOf(System.currentTimeMillis())).setValue(req);
                        new Database(OrderSummaryActivity.this).cleanCart();
                        Intent placedOrder = new Intent(OrderSummaryActivity.this, OrderCompleteActivity.class);
                        startActivity(placedOrder);
                        finish();
                        alertDialog.dismiss();
                    }
                });

                btnCancelId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void showOrders(String phone) {
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(
                request.orderByChild("phone").equalTo(phone), Request.class).build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inner_listview_orders, parent, false);
                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Request model) {

                orders = model.getOrders();

                NativelyCustomTextView customerName = holder.itemView.findViewById(R.id.product_name);
                customerName.setText(orders.get(position).getProductName());

                NativelyCustomTextView dateTime = holder.itemView.findViewById(R.id.product_quantity);
                dateTime.setText(orders.get(position).getQuantity());
                NativelyCustomTextView totalAmount = holder.itemView.findViewById(R.id.product_price);
                totalAmount.setText(orders.get(position).getPrice());
            }
        };
        rv_product_infos.setAdapter(adapter);
    }

    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null)
        {
            Log.d("LOCATION", "Your location : " + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
        }
        else
        {
            Log.d("LOCATION", "Could not get your location.");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_REQUEST).show();
            else
            {
                Toast.makeText(this, "This device is not supported.", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }
    private void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }
}
