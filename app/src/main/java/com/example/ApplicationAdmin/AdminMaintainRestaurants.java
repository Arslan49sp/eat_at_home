package com.example.ApplicationAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.RestaurantOwner.AdminMaintainProductsActivity;
import com.example.RestaurantOwner.RestOwnerPannel;
import com.example.eatathome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainRestaurants extends AppCompatActivity {

    private Button applyChangesBtn, deleteBtn;
    private EditText name, phone, address;
    private ImageView imageView;

    private String productID = "" , restID;
    private DatabaseReference restRef;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_restaurants);


        restID = getIntent().getStringExtra("rid");

        restRef = FirebaseDatabase.getInstance().getReference().child("Restaurants").child(restID);



        applyChangesBtn = findViewById(R.id.apply_changes_btn);
        name = findViewById(R.id.product_name_maintain);
        phone = findViewById(R.id.product_price_maintain);
        address = findViewById(R.id.product_description_maintain);
        imageView = findViewById(R.id.product_image_maintain);
        deleteBtn = findViewById(R.id.delete_product_btn);


        displayCurrentRestaurant();



        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                applyChanges();
            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                deleteThisProduct();
            }
        });
    }





    private void deleteThisProduct()
    {
        restRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Intent intent = new Intent(AdminMaintainRestaurants.this, ApplicationAdminPannel.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminMaintainRestaurants.this, "The Product Is deleted successfully.", Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void applyChanges() {
        String pName = name.getText().toString();
        String pPhone = phone.getText().toString();
        String pAddress = address.getText().toString();

        if (pName.equals("")) {
            Toast.makeText(this, "Write down Name.", Toast.LENGTH_SHORT).show();
        } else if (pPhone.equals("")) {
            Toast.makeText(this, "Write down Phone.", Toast.LENGTH_SHORT).show();
        } else if (pAddress.equals("")) {
            Toast.makeText(this, "Write down Address", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("address", pAddress);
            productMap.put("phone", pPhone);
            productMap.put("name", pName);

            restRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminMaintainRestaurants.this, "Changes applied successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminMaintainRestaurants.this, ApplicationAdminPannel.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void displayCurrentRestaurant()
    {
        restRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String pName = dataSnapshot.child("name").getValue().toString();
                    String pPhone = dataSnapshot.child("phone").getValue().toString();
                    String pAddress = dataSnapshot.child("address").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();


                    name.setText(pName);
                    phone.setText(pPhone);
                    address.setText(pAddress);
                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}