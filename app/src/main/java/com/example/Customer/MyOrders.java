package com.example.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Model.Orders;
import com.example.Prevalent.Prevalent;
import com.example.RestaurantOwner.AdminUserProductsActivity;
import com.example.eatathome.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyOrders extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("UserView");


        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

        userID = Prevalent.currentOnlineUser.getPhone().trim();



       // ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("UserView");


    }

    @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions<Orders> options =
                new FirebaseRecyclerOptions.Builder<Orders>()
                        .setQuery(ordersRef.orderByChild("uphone").equalTo(userID), Orders.class)
                        .build();

        FirebaseRecyclerAdapter<Orders, CustomerOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Orders, CustomerOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CustomerOrderViewHolder holder, final int position, @NonNull final Orders model)
                    {
                        holder.userName.setText("State: " + model.getState());
                        holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                        holder.userTotalPrice.setText("Total Amount =  RS" + model.getTotalAmount());
                        holder.userDateTime.setText("Order at: " + model.getDate() + "  " + model.getTime());
                        holder.userShippingAddress.setText("Shipping Address: " + model.getAddress() + ", " + model.getCity());

                        holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                String uID = model.getUphone();

                                Intent intent = new Intent(MyOrders.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid", uID);
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Toast.makeText(MyOrders.this, "Item Clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CustomerOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_orders_layout, parent, false);
                        return new CustomerOrderViewHolder(view);
                    }
                };

        ordersList.setAdapter(adapter);
        adapter.startListening();
    }




    public static class CustomerOrderViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;
        public Button ShowOrdersBtn;


        public CustomerOrderViewHolder(View itemView)
        {
            super(itemView);


            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            ShowOrdersBtn = itemView.findViewById(R.id.show_all_products_btn);
        }
    }



}