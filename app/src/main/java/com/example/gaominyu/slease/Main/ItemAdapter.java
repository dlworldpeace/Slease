package com.example.gaominyu.slease.Main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gaominyu.slease.Model.ItemPreview;
import com.example.gaominyu.slease.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ItemPreview itemPreview);
    }

    private Context context;
    private final List<ItemPreview> itemPreviewList;
    private final OnItemClickListener listener;

    public ItemAdapter(Context context, List<ItemPreview> itemPreviewList, OnItemClickListener listener) {
        this.context = context;
        this.itemPreviewList = itemPreviewList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.browse_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ItemPreview itemPreview = itemPreviewList.get(position);
        holder.name.setText(itemPreview.title);
        holder.deposit.setText("deposit: $" + itemPreview.deposit);
        holder.price.setText("rate：$" + itemPreview.rate + " per " + itemPreview.interval);

        byte[] decodedString = Base64.decode(itemPreview.imageBase64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Glide.with(context)
                .load(bitmap)
                .into(holder.thumbnail);

        // set onclick action on each item in RecyclerView
        holder.bindClickAction(itemPreview, listener);
    }

    @Override
    public int getItemCount() {
        return itemPreviewList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, deposit;
        public ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);
            deposit = view.findViewById(R.id.deposit);
            thumbnail = view.findViewById(R.id.thumbnail);
        }

        void bindClickAction(final ItemPreview itemPreview, final OnItemClickListener listener) {
            //name.setText(itemPreview.name);
            //Picasso.with(itemView.getContext()).load(item.imageUrl).into(image);
            thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        listener.onItemClick(itemPreview);
                    }
            });
        }
    }
}