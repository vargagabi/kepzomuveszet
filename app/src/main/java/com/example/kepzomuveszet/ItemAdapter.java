package com.example.kepzomuveszet;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private static final String LOG_TAG=ItemAdapter.class.getName()+"logcat";
    private ArrayList<MyItem> itemsData;
    private ArrayList<MyItem> itemsAll;
    private Context context;

    ItemAdapter(Context context, ArrayList<MyItem> itemsData){
        this.itemsData=itemsData;
        this.itemsAll=itemsData;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.card,parent,false));
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        MyItem currentItem = itemsData.get(position);

        holder.bindTo(currentItem);
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
            itemView.findViewById(R.id.cardCartButton).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Log.i(LOG_TAG,"Add cart button");
                }
            });
        }

        public void bindTo(MyItem currentItem) {
            Log.i(LOG_TAG,"BINDTO: "+currentItem.getName());
            nameText.setText(currentItem.getName());
            amountText.setText(Integer.toString(currentItem.getAmount()));
            priceText.setText(Integer.toString(currentItem.getPrice()));
            descriptionText.setText(currentItem.getDescription());

        }
    }
}

