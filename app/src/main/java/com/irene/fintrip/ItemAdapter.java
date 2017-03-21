package com.irene.fintrip;

import android.content.Context;
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
        public ImageView ivStar;
        public ImageView ivProduct;
        public TextView tvOwner;
        public TextView tvPrice;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivStar = (ImageView) itemView.findViewById(R.id.ivStar);
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
    public ItemAdapter(Context context, List<Item> tweets) {
        mItem = tweets;
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
        Item item = mItem.get(position);

//        viewHolder.getBinding().setVariable(com.codepath.apps.simpletweets.BR.tweet,tweet);
//        viewHolder.getBinding().executePendingBindings();

        // Set item views based on your views and data model
        ImageView ivStar = viewHolder.ivStar;

        ImageView ivProduct = viewHolder.ivProduct;
        Glide.with(mContext)
                .load(item.getImageUrl()) // Uri of the picture
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivProduct);

        TextView tvOwner = viewHolder.tvOwner;
        tvOwner.setText(item.getOwner());

        TextView tvPrice = viewHolder.tvPrice;
        tvPrice.setText(item.getPrice());

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mItem.size();
    }


}
