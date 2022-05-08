package com.example.kepzomuveszet;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName() + "logcat";

    Button loginBTN;
    Button registerBTN;
    Button logoutBTN;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBTN = findViewById(R.id.mainLoginButton);
        registerBTN = findViewById(R.id.mainRegisterButton);
        logoutBTN = findViewById(R.id.mainLogoutButton);
        //firebase adattagok
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //ha az user be van jelentkezve ne legyen a login es regisztracio gombok lathatoak
        isUserLoggedIn();
        Log.i(LOG_TAG,"onCreate");

    }
    private void isUserLoggedIn(){
        if(user ==null){
            Log.i(LOG_TAG,"NO USER");
            loginBTN.setVisibility(View.VISIBLE);
            registerBTN.setVisibility(View.VISIBLE);
            logoutBTN.setVisibility(View.GONE);

        }else{
            Log.i(LOG_TAG,"USER IS LOGGED IN");
            loginBTN.setVisibility(View.GONE);
            registerBTN.setVisibility(View.GONE);
            logoutBTN.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(user != null){
            user.reload();
        }
        isUserLoggedIn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(user != null){
            user.reload();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(user != null){
            user.reload();
        }
        isUserLoggedIn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();

    }

    public void onRegisterButton(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    public void onLoginButton(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivityForResult(intent,2);
        isUserLoggedIn();
    }


    //ha sikeres a bejelentkezes reloadoljuk a main-t ha visszalep az user
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2){
            finish();
            overridePendingTransition(0, 0);
            startActivity(new Intent(this,MainActivity.class));
            overridePendingTransition(0, 0);
        }
    }

    public void onShopButton(View view) {
        startActivity(new Intent(this,ShopActivity.class));
    }

    public void onLogoutButton(View view) {
        firebaseAuth.signOut();
        Log.i(LOG_TAG,"logout");
        finish();
        overridePendingTransition(0, 0);
        startActivity(new Intent(this,MainActivity.class));
        overridePendingTransition(0, 0);
    }



}