package com.irene.fintrip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.irene.fintrip.Utils.DatabaseUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChargeActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String tripID;

    ReceipeAdapter receipeAdapter;
    RecyclerView rvOwnerReceipe;
    TextView tvNoItem;
    List<Receipe> receipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        mDatabase = DatabaseUtil.getDatabase().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.homeToolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvToolbar = (TextView) findViewById(R.id.tvToolbar);
        tvToolbar.setText("- Receipe");


        Bundle extras = getIntent().getExtras();
        tripID = extras.getString("tripID");

        setupView();
    }

    private void setupView() {
        rvOwnerReceipe = (RecyclerView) findViewById(R.id.rvCharge);
        tvNoItem = (TextView) findViewById(R.id.tvNoItem);
        // Initialize contacts
        receipes = new ArrayList<>();

        getItemList(tripID);

        // Create adapter passing in the sample user data
        receipeAdapter = new ReceipeAdapter(this,receipes);
        // Attach the adapter to the recyclerview to populate items
        rvOwnerReceipe.setAdapter(receipeAdapter);
        // Set layout manager to position the items
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvOwnerReceipe.setLayoutManager(linearLayoutManager);
        rvOwnerReceipe.setHasFixedSize(true);
    }


    //read
    public void getItemList(String tripId) {

        Query myTopItemsQuery = mDatabase.child("buylist-items").child(tripId).orderByChild("createdTimeStampOrder").limitToFirst(100);
        myTopItemsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, HashMap<String, Receipe>> ownerMap = new HashMap<String, HashMap<String,Receipe>>();
//                HashMap<String, HashMap<String, Double>> ownerMap = new HashMap<String, HashMap<String,Double>>();

                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Map<String, Object> itemValues = (Map<String, Object>) itemSnapshot.getValue();
                    Log.e("DEBUG", String.valueOf(itemValues.get("price")));
                    Log.e("DEBUG:owner", String.valueOf(itemValues.get("owner")));
                    Log.e("DEBUG:isPaid", String.valueOf(itemValues.get("isPaid")));

//                    if((Boolean)itemValues.get("isBuy") && !(Boolean)itemValues.get("isPaid")){
//                        if(ownerMap.get(itemValues.get("owner")) == null){
//                            HashMap<String, Double> currencyMap = new HashMap<String, Double>();
//                            currencyMap.put((String)itemValues.get("targetCurrency"),((Number)itemValues.get("price")).doubleValue());
//                            ownerMap.put((String)itemValues.get("owner"),currencyMap);
//                        }
//                        else {
//                            if (ownerMap.get(itemValues.get("owner")).get(itemValues.get("targetCurrency")) == null) {
//                                ownerMap.get(itemValues.get("owner")).put((String) itemValues.get("targetCurrency"), ((Number) itemValues.get("price")).doubleValue());
//                            } else {
//                                ownerMap.get(itemValues.get("owner")).put((String) itemValues.get("targetCurrency"), ownerMap.get(itemValues.get("owner")).get(itemValues.get("targetCurrency")) + ((Number) itemValues.get("price")).doubleValue());
//                            }
//                        }
//                    }


                    if((Boolean)itemValues.get("isBuy") && !(Boolean)itemValues.get("isPaid") ){

                        String owner = (String)itemValues.get("owner");
                        owner = owner.toUpperCase();

                        if(ownerMap.get(owner) == null){

                            HashMap<String, Receipe> currencyMap = new HashMap<String, Receipe>();
                            List<String> ListItemID = new ArrayList<String>();
                            ListItemID.add((String) itemValues.get("itemId"));
                            Receipe receipe = new Receipe(((Number)itemValues.get("targetPrice")).doubleValue(),ListItemID);

                            currencyMap.put((String)itemValues.get("targetCurrency"),receipe);
                            ownerMap.put(owner,currencyMap);
                        }
                        else {
                            if (ownerMap.get(owner).get(itemValues.get("targetCurrency")) == null) {
                                List<String> ListItemID = new ArrayList<String>();
                                ListItemID.add((String) itemValues.get("itemId"));
                                Receipe receipe = new Receipe(((Number)itemValues.get("targetPrice")).doubleValue(),ListItemID);
                                ownerMap.get(owner).put((String) itemValues.get("targetCurrency"), receipe);
                            }
                            else {
                                Double priceUpdate;
                                List<String> ListItemID = new ArrayList<String>();
                                priceUpdate = ((Number)itemValues.get("targetPrice")).doubleValue() + ownerMap.get(itemValues.get("owner")).get(itemValues.get("targetCurrency")).getTotalPrice();
                                ListItemID = ownerMap.get(owner).get(itemValues.get("targetCurrency")).getItemID();
                                ListItemID.add((String)itemValues.get("itemId"));
                                Receipe receipe = new Receipe(priceUpdate,ListItemID);
                                ownerMap.get(owner).put((String) itemValues.get("targetCurrency"),receipe);
                            }
                        }
                    }

                }
//                for(String key : ownerMap.keySet() ){
//                    for(String innerkey: ownerMap.get(key).keySet()){
//                        Receipe receipe = new Receipe (key,ownerMap.get(key).get(innerkey),innerkey);
//                        receipes.add(receipe);
//                    }
//                }

                receipes.clear();
                for(String key : ownerMap.keySet() ){
                    for(String innerkey: ownerMap.get(key).keySet()){
                        Receipe receipe = new Receipe (key,ownerMap.get(key).get(innerkey).getTotalPrice(),innerkey,ownerMap.get(key).get(innerkey).getItemID());
                        Log.d("DEBUG:innerKEY",innerkey);
                        receipes.add(receipe);
                    }
                }
                receipeAdapter.notifyDataSetChanged();

                Log.d("DEBUG:receipe",String.valueOf(receipes.size()));

                if(receipes.size() == 0){
                    tvNoItem.setText("NO UNPAID ITEM");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("DEBUG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }

    //update
    public void updateIsPaid(String tripId, String itemId, boolean isPaid) {
        mDatabase.child("buylist-items").child(tripId).child(itemId).child("isPaid").setValue(isPaid);
    }

    public String getTripID(){
        return tripID;
    }

}
