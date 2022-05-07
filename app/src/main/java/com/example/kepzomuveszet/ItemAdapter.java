package com.example.kepzomuveszet;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private static final String LOG_TAG=ItemAdapter.class.getName()+"logcat";
    private ArrayList<MyItem> itemsData;
    private Context context;
    private FirebaseFirestore firestore;

    ItemAdapter(Context context, ArrayList<MyItem> itemsData){
        this.itemsData=itemsData;
        this.context=context;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.card,parent,false));
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        MyItem currentItem = itemsData.get(position);

        holder.bindTo(currentItem,position);
    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView nameText;
        private TextView priceText;
        private TextView amountText;
        private TextView descriptionText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.cardItemNameTextView);
            amountText = itemView.findViewById(R.id.cardItemAmountTextView);
            priceText = itemView.findViewById(R.id.cardItemPriceTextView);
            descriptionText = itemView.findViewById(R.id.cardItemDescriptionTextView);

        }

        public void bindTo(MyItem currentItem, int position) {
            Log.i(LOG_TAG,"BINDTO: "+currentItem.getName());
            nameText.setTag(currentItem.getId());
            nameText.setText(currentItem.getName());
            amountText.setText(Integer.toString(currentItem.getAmount()));
            priceText.setText(Integer.toString(currentItem.getPrice()));
            descriptionText.setText(currentItem.getDescription());
            itemView.findViewById(R.id.cardCartButton).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Log.i(LOG_TAG,"Add cart button");
                    String id = (String)nameText.getTag();
                    int currentAmount = Integer.parseInt(amountText.getText().toString());
                    if(currentAmount>1){
                        currentAmount-=1;
//                        firestore.collection("Items").document(id).update("amount",currentAmount);
                        currentItem.setAmount(currentAmount);

                        updateItem(id,currentItem);
                        Log.i(LOG_TAG,"ITEM UPDATED: " + id + ", " + currentAmount);
                        notifyItemChanged(position);
                    }else{
                        deleteItem(id);
                        Log.i(LOG_TAG,"ITEM DELETED" + id);
                        itemsData.remove(position);
                        notifyItemRemoved(position);
                    }

                }
            });

        }
    }
    //CRUD: Update
    public void updateItem(String id,MyItem newItem){
        DocumentReference ref = firestore.collection("Items").document(id);
        ref.update("name",newItem.getName());
        ref.update("description",newItem.getDescription());
        ref.update("price",newItem.getPrice());
        ref.update("amount",newItem.getAmount()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i(LOG_TAG,"SUCCESSFULLY DECREASED AMOUNT");
                }else{
                    Log.i(LOG_TAG,task.getException().getMessage());
                }
            }
        });
    }

    //CRUD: Delete
    public void deleteItem(String id){
        firestore.collection("Items").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i(LOG_TAG,"SUCCESSFULLY DELETED: " + id);
                }else{
                    Log.i(LOG_TAG,"FAILED TO DELETE: " + id);
                }
            }
        });
    }
}

