package com.example.kepzomuveszet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {
    private static final String LOG_TAG=ShopActivity.class.getName()+"logcat";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    RecyclerView recyclerView;
    private ArrayList<MyItem> items;
    private int gridNumber = 1;
    private ItemAdapter itemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //firebase adattagok
        firebaseAuth=FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            finish();
        }

        recyclerView = findViewById(R.id.shopRecycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        items = new ArrayList<>();
        itemAdapter = new ItemAdapter(this,items);

        recyclerView.setAdapter(itemAdapter);
        
        initItems();
    }

    private void initItems() {
        String[] itemNames;
        String[] itemPrices;
        String[] itemAmounts;
        String[] itemDescriptions;

    }

    public void onCartButton(View view) {


    }
}