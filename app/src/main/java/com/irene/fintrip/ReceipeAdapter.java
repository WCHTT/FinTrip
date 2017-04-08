package com.irene.fintrip;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ChaoJung on 2017/4/2.
 */

public class ReceipeAdapter extends RecyclerView.Adapter<ReceipeAdapter.ViewHolder>   {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        public TextView tvOwner;
        public TextView tvTotalPrice;
        public ImageView ivTotalPaid;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvOwner = (TextView) itemView.findViewById(R.id.tvOwner);
            tvTotalPrice = (TextView) itemView.findViewById(R.id.tvTotalPrice);
            ivTotalPaid = (ImageView) itemView.findViewById(R.id.ivTotalPaid);

        }
    }

    // Store a member variable for the contacts
    private List<Receipe> mItem;
    // Store the context for easy access
    private Context mContext;

    // Clean all elements of the recycler
    public void clear() {
        mItem.clear();
        notifyDataSetChanged();
    }

    // Pass in the contact array into the constructor
    public ReceipeAdapter(Context context, List<Receipe> items) {
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
        View contactView = inflater.inflate(R.layout.item_receipe_byowner, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Receipe item = mItem.get(position);

        TextView tvOwner = viewHolder.tvOwner;
        tvOwner.setText(item.getOwner());

        TextView tvTotalPrice = viewHolder.tvTotalPrice;
        tvTotalPrice.setText(item.getTargetCurrency()+" "+item.getTotalPrice().toString());

        final ImageView ivTotalPaid = viewHolder.ivTotalPaid;

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mItem.size();
    }

}
