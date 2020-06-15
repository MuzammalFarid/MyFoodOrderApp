package com.example.myfoodorderapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignUp, btnSignIn;
    TextView textSlogan;
    String phonenumber;
//    private boolean isSinglePressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        phonenumber = getIntent().getStringExtra("phonenumber");
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);

        textSlogan = findViewById(R.id.txtSlogan);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        textSlogan.setTypeface(typeface);

        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSignUp) {
//            SignUp
            Intent intent = new Intent(MainActivity.this, SignUp.class);
            intent.putExtra("phonenumber", phonenumber);
            startActivity(intent);
        } else if (v.getId() == R.id.btnSignIn) {
//            Login
            Intent intent = new Intent(MainActivity.this, SignIn.class);
            intent.putExtra("phonenumber", phonenumber);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
