package com.example.eatathome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ApplicationAdmin.ApplicationAdminPannel;
import com.example.Customer.HomeActivity;
import com.example.DeliveryBoy.DeliveryBoyPannel;
import com.example.Model.Users;
import com.example.Prevalent.Prevalent;
import com.example.RestaurantOwner.RestOwnerPannel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    // fields for user inputs
    private EditText inputPhoneNumber, inputPassword;
    private Button loginButton;
    private ProgressDialog loadingBar;

    private String parentDbName = "abc";
    private CheckBox chkBoxRememberMe;


    //these are used for Navigation Drawer.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_btn);
        inputPassword = findViewById(R.id.login_password_input);
        inputPhoneNumber = findViewById(R.id.login_phone_number_input);
        loadingBar = new ProgressDialog(this);


        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            parentDbName = getIntent().getExtras().get("userType").toString();
            Toast.makeText(this, parentDbName, Toast.LENGTH_SHORT).show();
        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginUser();
            }
        });

    }

    private void loginUser() {
        String phone = inputPhoneNumber.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            inputPhoneNumber.setError("Please enter your phone number");
        }
        else if (TextUtils.isEmpty(password))
        {
            inputPassword.setError("Please enter your password");
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            allowAccessToAccount(phone, password);
        }
    }

    private void allowAccessToAccount( String phone,  String password) {
        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }


        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();


        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            switch (parentDbName)
                            {
                                case "Application Admins":
                                    Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    Intent intent = new Intent(LoginActivity.this, ApplicationAdminPannel.class);
                                    Prevalent.currentOnlineUser = usersData;
                                    startActivity(intent);
                                    break;
                                case "Users":
                                    Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    Prevalent.currentOnlineUser = usersData;
                                    startActivity(intent);
                                    break;

                                case "Delivery Boy":

                                    Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    intent = new Intent(LoginActivity.this, DeliveryBoyPannel.class);
                                    Prevalent.currentOnlineUser = usersData;
                                    startActivity(intent);
                                    break;

                                case "Restaurants":
                                    Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    intent = new Intent(LoginActivity.this, RestOwnerPannel.class);
                                    Prevalent.currentOnlineUser = usersData;
                                    startActivity(intent);
                                    break;

                            }

                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}