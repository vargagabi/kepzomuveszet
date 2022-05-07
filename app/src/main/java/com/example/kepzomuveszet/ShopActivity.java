package com.example.kepzomuveszet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {
    private static final String LOG_TAG=ShopActivity.class.getName()+"logcat";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    RecyclerView recyclerView;
    private ArrayList<MyItem> itemsData;
    private int gridNumber = 1;
    private ItemAdapter itemAdapter;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;

    //add item adattagok
    EditText itemName;
    EditText itemPrice;
    EditText itemDescription;
    EditText itemAmount;
    LinearLayout shopAddItemLayout;
    Button switchButton;

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridNumber=3;
        }else{
            gridNumber=1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);

        //firebase adattagok
        firebaseAuth=FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        itemName=findViewById(R.id.shopItemNameEditText);
        itemPrice=findViewById(R.id.shopItemPriceEditText);
        itemDescription=findViewById(R.id.shopItemDescriptionEditText);
        itemAmount = findViewById(R.id.shopItemAmountEditText);
        shopAddItemLayout = findViewById(R.id.shopAddItemLayout);
        switchButton = findViewById(R.id.shopSwitchButton);

        shopAddItemLayout.setVisibility(View.GONE);
//        if(user==null){
//            finish();
//        }

        recyclerView = findViewById(R.id.shopRecycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        itemsData = new ArrayList<>();
        itemAdapter = new ItemAdapter(this,itemsData);


        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("Items");


        readAll();
    }


    //CRUD: READ
    private void readAll() {
        firestore.collection("Items").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Log.i(LOG_TAG, document.getId() + "=>" + document.getData());

                                itemsData.add(new MyItem(
                                        document.getId(),
                                        document.getData().get("name").toString(),
                                        document.getData().get("description").toString(),
                                        Integer.parseInt(document.getData().get("price").toString()),
                                        Integer.parseInt(document.getData().get("amount").toString())
                                        )
                                );
                            }

                            recyclerView.setAdapter(itemAdapter);
                            for(MyItem item : itemsData){
                                Log.i(LOG_TAG, item.toString());
                            }
                        }else{
                            Log.i(LOG_TAG,"Error downloading items: " +task.getException().getMessage());

                        }
                    }
                });
    }


    //CRUD: CREATE
    private void createItem(MyItem item) {
        Log.i(LOG_TAG,"inititems");
        firestore.collection("Items").add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.i(LOG_TAG,"item added: " +documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(LOG_TAG,"failed to add item: " +e);
            }
        });

    }
    //CRUD: Update
    public void updateItem(MyItem oldItem,MyItem newItem){
        DocumentReference ref = firestore.collection("Item").document(oldItem.getId());
        ref.update("name",newItem.getName());
        ref.update("description",newItem.getDescription());
        ref.update("price",newItem.getPrice());
        ref.update("amount",newItem.getAmount());

    }

    public void onCartButton(View view) {

    }

    public void onShopAdd(View view) {
        String name = itemName.getText().toString();
        String desc = itemDescription.getText().toString();
        String price = itemPrice.getText().toString();
        String amount = itemAmount.getText().toString();
        if(!name.isEmpty() && !desc.isEmpty() && !price.isEmpty() && !amount.isEmpty()){
            Log.i(LOG_TAG,"ADDED ITEM TEST");
            Log.i(LOG_TAG,"price: " + price + "amount: " + amount);
            int priceInt = 0;
            int amountInt = 0;
            try {
                priceInt = Integer.parseInt(price);
                amountInt = Integer.parseInt(amount);

            }catch (Exception e){
                return;
            }
            MyItem item = new MyItem(name,desc,priceInt,amountInt);
            createItem(item);
            //reload activity es valtas az itemek kiirasara
            finish();
            overridePendingTransition(0, 0);
            startActivity(new Intent(this,ShopActivity.class));
            overridePendingTransition(0, 0);
            switchButton.setText("Add Item");
        }


    }

    public void onSwitchButton(View view) {
        String text = switchButton.getText().toString();
        if(text.equals("Add item")){
            recyclerView.setVisibility(View.GONE);
            shopAddItemLayout.setVisibility(View.VISIBLE);
            switchButton.setText("Show items");
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            shopAddItemLayout.setVisibility(View.GONE);
            switchButton.setText("Add item");
        }
    }
}