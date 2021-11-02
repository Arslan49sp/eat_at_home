package com.example.eatathome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AllAppUsers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_app_users);
    }

    public void client(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("userType", "Users");
        startActivity(intent);

    }

    public void restOwner(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("userType", "Restaurants");
        startActivity(intent);
    }

    public void delBoy(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("userType", "Delivery Boy");
        startActivity(intent);
    }

    public void appAdmin(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("userType", "Application Admins");
        startActivity(intent);
    }
}