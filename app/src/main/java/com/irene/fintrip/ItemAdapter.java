package com.irene.fintrip;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by ChaoJung on 2017/3/20.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>  {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView ivBought;
        public ImageView ivPaid;
        public ImageView ivProduct;
        public TextView tvOwner;
        public TextView tvPrice;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivBought = (ImageView) itemView.findViewById(R.id.ivBought);
            ivPaid = (ImageView) itemView.findViewById(R.id.ivPaid);
            ivProduct = (ImageView) itemView.findViewById(R.id.ivProduct);
            tvOwner = (TextView) itemView.findViewById(R.id.tvOwner);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);

        }
    }

    // Store a member variable for the contacts
    private List<Item> mItem;
    // Store the context for easy access
    private Context mContext;

    // Clean all elements of the recycler
    public void clear() {
        mItem.clear();
        notifyDataSetChanged();
    }

    // Pass in the contact array into the constructor
    public ItemAdapter(Context context, List<Item> items) {
        mItem = items;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_tobuy, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Item item = mItem.get(position);

//        viewHolder.getBinding().setVariable(com.codepath.apps.simpletweets.BR.tweet,tweet);
//        viewHolder.getBinding().executePendingBindings();

        // Set item views based on your views and data model
        final ImageView ivBought = viewHolder.ivBought;
        final ImageView ivPaid = viewHolder.ivPaid;

        ivBought.setColorFilter(Color.parseColor("#d4d3d3"), PorterDuff.Mode.MULTIPLY);
        ivPaid.setColorFilter(Color.parseColor("#d4d3d3"), PorterDuff.Mode.MULTIPLY);

        if(item.isBuy()){
            ivPaid.setVisibility(View.VISIBLE);
            ivBought.setColorFilter(Color.parseColor("#B2FF59"), PorterDuff.Mode.MULTIPLY);
        }
        else {
            ivPaid.setVisibility(View.INVISIBLE);
        }
        if(item.isPaid()){
            ivPaid.setColorFilter(Color.parseColor("#FFEB3B"), PorterDuff.Mode.MULTIPLY);
        }

        ivBought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!item.isBuy() && mContext instanceof HomeActivity){
                    ((HomeActivity)mContext).updateIsBuy(((HomeActivity)mContext).getTripID(),item.getItemId(),true);
                    ivBought.setColorFilter(Color.parseColor("#B2FF59"), PorterDuff.Mode.MULTIPLY);
                }
                else{
                    ((HomeActivity)mContext).updateIsBuy(((HomeActivity)mContext).getTripID(),item.getItemId(),false);
                    ivBought.setColorFilter(Color.parseColor("#d4d3d3"), PorterDuff.Mode.MULTIPLY);
                }
            }
        });

        ivPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!item.isPaid() && mContext instanceof HomeActivity){
                    ((HomeActivity)mContext).updateIsPaid(((HomeActivity)mContext).getTripID(),item.getItemId(),true);
                    ivPaid.setColorFilter(Color.parseColor("#FFEB3B"), PorterDuff.Mode.MULTIPLY);
                }
                else{
                    ((HomeActivity)mContext).updateIsPaid(((HomeActivity)mContext).getTripID(),item.getItemId(),false);
                    ivPaid.setColorFilter(Color.parseColor("#d4d3d3"), PorterDuff.Mode.MULTIPLY);
                }
            }
        });


        ImageView ivProduct = viewHolder.ivProduct;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivProduct.setClipToOutline(true);
        }

        ViewCompat.setTransitionName(viewHolder.ivProduct, item.getItemId());


        Glide.with(mContext)
                .load(item.getImageUrl()) // Uri of the picture
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivProduct);

        TextView tvOwner = viewHolder.tvOwner;
        tvOwner.setText(item.getOwner());

        TextView tvPrice = viewHolder.tvPrice;
        tvPrice.setText("Price: "+item.getPrice().toString());

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mItem.size();
    }


}
