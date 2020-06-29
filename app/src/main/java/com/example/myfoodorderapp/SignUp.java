package com.example.myfoodorderapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodorderapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    EditText editName, editPassword;
    TextView address_ed_txt;
    LinearLayout btnSignUp;
    EditText editPhone;
    String streetNo = " ", House_no = " ", Area = " ", Main_point = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editName = findViewById(R.id.name_ed_txt);
        editPhone = findViewById(R.id.editPhone);
        editPassword = findViewById(R.id.editPassord);
        btnSignUp = findViewById(R.id.btnSignUp);
        address_ed_txt = findViewById(R.id.address_ed_txt);


        address_ed_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHomeDialog();
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String phoneNumber = user.getPhoneNumber();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("User");

        editPhone.setText(phoneNumber);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this, "User already exists!", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            User user = new User(editName.getText().toString(), editPassword.getText().toString(), address_ed_txt.getText().toString());
                            table_user.child(editPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void addHomeDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Add Home Address");
        alertDialog.setMessage("Please provide information about your current address");
        alertDialog.setIcon(R.drawable.add_adress);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.custom_add_address_dialog, null);
        alertDialog.setView(view);

        final MaterialEditText street_no = view.findViewById(R.id.street_no);
        final MaterialEditText house_no = view.findViewById(R.id.house_no);
        final MaterialEditText area = view.findViewById(R.id.area);
        final MaterialEditText main_point = view.findViewById(R.id.main_point);


        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                streetNo = street_no.getText().toString();
                House_no = house_no.getText().toString();
                Area = area.getText().toString();
                Main_point = main_point.getText().toString();

                address_ed_txt.setText(streetNo + " " + House_no + " " + Area + " " + Main_point);

                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }
}
