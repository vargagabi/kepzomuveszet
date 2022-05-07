package com.example.kepzomuveszet;


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
        if(user != null){
            Log.i(LOG_TAG,"USER IS LOGGED IN");
            loginBTN.setVisibility(View.GONE);
            registerBTN.setVisibility(View.GONE);
            logoutBTN.setVisibility(View.VISIBLE);
        }else{
            Log.i(LOG_TAG,"NO USER");
            loginBTN.setVisibility(View.VISIBLE);
            registerBTN.setVisibility(View.VISIBLE);
            logoutBTN.setVisibility(View.GONE);
        }
    }




    public void onRegisterButton(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    public void onLoginButton(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        isUserLoggedIn();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        user.reload();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"onResume");
        isUserLoggedIn();
    }
}