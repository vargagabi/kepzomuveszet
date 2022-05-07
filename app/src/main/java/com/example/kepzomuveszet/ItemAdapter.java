package com.example.kepzomuveszet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private int lastPosition = -1;
    private NotificationHandler notificationHandler;

    ItemAdapter(Context context, ArrayList<MyItem> itemsData){
        this.itemsData=itemsData;
        this.context=context;
        firestore = FirebaseFirestore.getInstance();
        notificationHandler = new NotificationHandler(context);
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

        if(holder.getAbsoluteAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_left);
            holder.itemView.startAnimation(animation);
            lastPosition=holder.getAbsoluteAdapterPosition();
        }

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
                        Log.i(LOG_TAG,"ITEM DELETED" + id);

                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_to_right);
                        itemView.startAnimation(animation);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                notifyItemRemoved(position);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                deleteItem(id);
                                itemsData.remove(position);
                                notificationHandler.send("Item out of stock: " + currentItem.getName());
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
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

