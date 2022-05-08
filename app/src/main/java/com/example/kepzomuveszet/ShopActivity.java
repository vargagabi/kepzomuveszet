package com.example.kepzomuveszet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private int gridNumber=1;
    private ItemAdapter itemAdapter;
    private FirebaseFirestore firestore;

    //add item adattagok
    EditText itemName;
    EditText itemPrice;
    EditText itemDescription;
    EditText itemAmount;
    LinearLayout shopAddItemLayout;
    Button switchButton;


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
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.i(LOG_TAG,"LANDSCAPE");
            gridNumber=2;
        }else{
            Log.i(LOG_TAG,"PORTRAIT");
            gridNumber=1;
        }
        recyclerView = findViewById(R.id.shopRecycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        itemsData = new ArrayList<>();
        itemAdapter = new ItemAdapter(this,itemsData);


        firestore = FirebaseFirestore.getInstance();

        readAll();
    }

    //lekerdezi azokat az itemeket amelyekbol max 10 db-van es ezert ritkak
    private void getRareItems(){
        firestore.collection("Items").whereLessThanOrEqualTo("amount",10).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
//                                Log.i(LOG_TAG, document.getId() + "=>" + document.getData());

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
                        }else{
                            Log.i(LOG_TAG,"Nem sikerült letölteni az adatokat: " +task.getException().getMessage());

                        }
                    }
                });
    }

    //CRUD: READ
    private void readAll() {
        firestore.collection("Items").orderBy("name").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
//                                Log.i(LOG_TAG, document.getId() + "=>" + document.getData());

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

    //item feltoltese a shop-ba
    public void onShopAdd(View view) {
        String name = itemName.getText().toString();
        String desc = itemDescription.getText().toString();
        String price = itemPrice.getText().toString();
        String amount = itemAmount.getText().toString();
        if(!name.isEmpty() && !desc.isEmpty() && !price.isEmpty() && !amount.isEmpty()){
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
    //valtas az itemek listazasa es item hozzaadasaa kozott
    public void onSwitchButton(View view) {
        String text = switchButton.getText().toString();
        if(text.equals("Hozzáad")){
            recyclerView.setVisibility(View.GONE);
            shopAddItemLayout.setVisibility(View.VISIBLE);
            switchButton.setText(R.string.keszlet);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            shopAddItemLayout.setVisibility(View.GONE);
            switchButton.setText(R.string.hozzaad);
        }
    }

}