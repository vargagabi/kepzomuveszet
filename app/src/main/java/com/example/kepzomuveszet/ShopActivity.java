package com.example.kepzomuveszet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShopActivity extends AppCompatActivity {
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //firebase adattagok
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){

        }else{
            finish();
        }
    }

    public void onCartButton(View view) {
    }
}