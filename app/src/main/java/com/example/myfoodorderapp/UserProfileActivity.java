package com.example.myfoodorderapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodorderapp.Common.Common;
import com.example.myfoodorderapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    public TextView profile_name, profile_phone, profile_address;
    Button btnUpdateUsername, btnUpdateHomeAddress, btnSelect, btnUpload;
    CircleImageView profile_pic;
    RelativeLayout rootLayout;
    Uri saveUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    User newUser;
    FirebaseDatabase db;
    DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        db = FirebaseDatabase.getInstance();

        user = db.getReference("User").child(Common.currentUser.getPhone());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        loadProfile();

        btnUpdateHomeAddress = findViewById(R.id.btn_updateAddress);
        btnUpdateUsername = findViewById(R.id.btn_updateUsername);
        profile_pic = findViewById(R.id.profile_picture);

        btnUpdateUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserNameDialog();
            }
        });

        btnUpdateHomeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHomeDialog();
            }
        });

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeProfileDialog();
            }
        });
    }


    private void showChangeProfileDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfileActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialog.setTitle("Change Profile Picture");

        LayoutInflater inflater = this.getLayoutInflater();
        View change_profile = inflater.inflate(R.layout.change_profile_dialog, null);
        btnSelect = change_profile.findViewById(R.id.btnSelect);
        btnUpload = change_profile.findViewById(R.id.btnUpload);

        //Event for button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //let users select image from gallery and save URL of this image
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //upload image
                uploadImage();
            }
        });

        alertDialog.setView(change_profile);
        alertDialog.setIcon(R.drawable.lock_b_icon);

        //set Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                //change profile
                user.child("images").setValue(newUser.getImages());
                Picasso.get().load(saveUri).into(profile_pic);

            }

        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alertDialog.show();

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
                if (TextUtils.isEmpty(street_no.getText())) {

                    Toast.makeText(UserProfileActivity.this, "Home Address is Empty!", Toast.LENGTH_SHORT).show();
                } else {

                    Common.currentUser.setAddress(street_no.getText().toString() + " , " + house_no.getText().toString() + " , " + area.getText().toString() + " , " + main_point.getText().toString());

                    FirebaseDatabase.getInstance().getReference("User")
                            .child(Common.currentUser.getPhone())
                            .setValue(Common.currentUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(UserProfileActivity.this, "Update Address Successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserProfileActivity.this, "Home Address Cannot Update!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void UpdateUserNameDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Update Your Name");
        alertDialog.setMessage("Please provide information about your current address");
        alertDialog.setIcon(R.drawable.add_adress);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.custom_update_username_dialog, null);
        alertDialog.setView(view);

        final MaterialEditText user_name = view.findViewById(R.id.user_name);


        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                if (TextUtils.isEmpty(user_name.getText())) {
                    Toast.makeText(UserProfileActivity.this, "Username is Empty!", Toast.LENGTH_SHORT).show();
                } else {

                    Common.currentUser.setName(user_name.getText().toString());

                    FirebaseDatabase.getInstance().getReference("User")
                            .child(Common.currentUser.getPhone())
                            .setValue(Common.currentUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                        Toast.makeText(UserProfileActivity.this, "Username was updated!", Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            });
                }
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }


    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), Common.PICK_IMAGE_REQUEST);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {

            saveUri = data.getData();
            btnSelect.setText("Image Selected!");
        }
    }

    private void uploadImage() {

        if (saveUri != null) {

            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mDialog.dismiss();
                    Toast.makeText(UserProfileActivity.this, "Uploaded!!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            //set value for newCategory if image upload and we can get download link
                            newUser = new User();
                            newUser.setImages(uri.toString());

                        }
                    });

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            mDialog.dismiss();
                            Toast.makeText(UserProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploading" + progress + " % ");
                        }
                    });
        }
    }

    public void loadProfile() {

        ValueEventListener imageListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String images = dataSnapshot.child("images").getValue(String.class);

                Picasso.get().load(images).into(profile_pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        profile_name = findViewById(R.id.profile_name);
        profile_phone = findViewById(R.id.profile_phone);
        profile_address = findViewById(R.id.profile_address);

        profile_name.setText(Common.currentUser.getName());
        profile_address.setText(Common.currentUser.getAddress());
        profile_phone.setText(Common.currentUser.getPhone());
        user.addValueEventListener(imageListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfile();
    }
}