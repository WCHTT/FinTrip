package com.irene.fintrip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.irene.fintrip.Utils.DatabaseUtil;

import java.util.Map;

public class ChargeActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String tripID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        mDatabase = DatabaseUtil.getDatabase().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.homeToolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        tripID = extras.getString("tripID");
        getItemList(tripID);
    }

    //read
    private void getItemList(String tripId) {

        Query myTopItemsQuery = mDatabase.child("buylist-items").child(tripId).orderByChild("createdTimeStampOrder").limitToFirst(100);
        myTopItemsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Map<String, Object> itemValues = (Map<String, Object>) itemSnapshot.getValue();
                    Log.e("DEBUG", String.valueOf(itemValues.get("price")));
                    Log.e("DEBUG:owner", String.valueOf(itemValues.get("owner")));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("DEBUG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }
}
