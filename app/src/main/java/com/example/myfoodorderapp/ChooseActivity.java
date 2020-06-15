package com.example.myfoodorderapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodorderapp.Common.Common;
import com.example.myfoodorderapp.Model.User;
import com.example.myfoodorderapp.Server.HomeActivityServer;
import com.example.myfoodorderapp.Server.MainActivityServer;
import com.google.android.material.snackbar.Snackbar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;

import static com.example.myfoodorderapp.Common.Common.CLIENT;
import static com.example.myfoodorderapp.Common.Common.SERVER;
import static com.example.myfoodorderapp.Common.Common.USER_NAME;
import static com.example.myfoodorderapp.Common.Common.USER_PASSWORD;
import static com.example.myfoodorderapp.Common.Common.USER_PHONE;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnClient, btnServer;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    String phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        Paper.init(this);

        phonenumber  = getIntent().getStringExtra("phonenumber");

        //Get key hash for fb
        try {

            @SuppressLint("PackageManagerGetSignatures") PackageInfo info =
                    getPackageManager().getPackageInfo("com.example.myfoodorderapp",
                            PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        relativeLayout = findViewById(R.id.parent);
        btnClient = findViewById(R.id.btnClient);
        btnServer = findViewById(R.id.btnServer);
        progressBar = findViewById(R.id.progress);

        btnClient.setOnClickListener(this);
        btnServer.setOnClickListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                btnClient.animate().alpha(1).setDuration(300);
                btnServer.animate().alpha(1).setDuration(300);
            }
        }, 1000);

        //Services

    }

    @Override
    public void onClick(View v) {
        //checking internet firstly
        if (Common.isInternetAvailable(this)) {
            if (v.getId() == R.id.btnClient) {
                //check if user is already signed in
                String phone = Paper.book(CLIENT).read(USER_PHONE);
                String password = Paper.book(CLIENT).read(USER_PASSWORD);
                String name = Paper.book(CLIENT).read(USER_NAME);

                if (phone != null && password != null && name != null) {
                    Intent intent = new Intent(ChooseActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Common.currentUser = new User(name, password, phone, "false");
                } else {
                    Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
                    intent.putExtra("phonenumber", phonenumber);
                    startActivity(intent);
                }
            } else if (v.getId() == R.id.btnServer) {
                //check if user is already signed in
                String phone = Paper.book(SERVER).read(USER_PHONE);
                String password = Paper.book(SERVER).read(USER_PASSWORD);
                String name = Paper.book(SERVER).read(USER_NAME);

                if (phone != null && password != null && name != null) {
                    Intent intent = new Intent(ChooseActivity.this, HomeActivityServer.class);
                    startActivity(intent);
                    Common.currentUser = new User(name, password, phone, "true");
                } else {
                    Intent intent = new Intent(ChooseActivity.this, MainActivityServer.class);
                    startActivity(intent);
                }
            }
        }
        else {
            Snackbar.make(relativeLayout, "No Internet Connection!", Snackbar.LENGTH_LONG).show();
        }
    }

}
