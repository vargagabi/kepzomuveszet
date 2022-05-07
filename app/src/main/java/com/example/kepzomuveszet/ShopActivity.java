package com.example.kepzomuveszet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
    private int gridNumber = 2;
    private ItemAdapter itemAdapter;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);

        //firebase adattagok
        firebaseAuth=FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();



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

    public void onCartButton(View view) {


    }
}