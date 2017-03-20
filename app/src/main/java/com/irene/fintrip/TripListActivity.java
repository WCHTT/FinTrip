package com.irene.fintrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.irene.fintrip.adapters.TripListAdapter;
import com.irene.fintrip.models.Trip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TripListActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    private ArrayList<Trip> allTrips;
    private TripListAdapter tripsAdapter;
    ListView lvTrips;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triplist);
        //writeNewBuyList("pB57jmHfCAhDy5lSpdW6mOWMOFg1", "Irene", sdf.format(new Date()),"Okinawa");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.e("DEBUG", user.getDisplayName());
        allTrips = new ArrayList<Trip>();
        getNewBuyList("pB57jmHfCAhDy5lSpdW6mOWMOFg1");

        lvTrips = (ListView) findViewById(R.id.lvTrips);
        tripsAdapter = new TripListAdapter(this, allTrips);
        lvTrips.setAdapter(tripsAdapter);

        lvTrips.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        Intent i = new Intent(TripListActivity.this, HomeActivity.class);
                        startActivity(i);
                    }
                }
        );//Home

    }


    private void writeNewBuyList(String authorId, String authorName, String createdTime, String listName) {
        // Create new item at /user-buylists/$userid/$buylistid and at
        // /buylists/$buylistid simultaneously
        String key = mDatabase.child("buylists").push().getKey();
        Trip trip = new Trip(key, authorId, authorName, createdTime, listName);
        Map<String, Object> postValues = trip.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/buylists/" + key, postValues);
        childUpdates.put("/user-buylists/" + authorId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    private void getNewBuyList(String authorId) {

        //final ArrayList<Trip> trips = new ArrayList<Trip>();

        Query myTopPostsQuery = mDatabase.child("user-buylists").child(authorId).limitToFirst(100);
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Map<String, String> postValues = (Map<String, String>) postSnapshot.getValue();
                    Trip trip = new Trip(postValues.get("buyListId"),postValues.get("authorId"),postValues.get("authorName"),postValues.get("createdTime"), postValues.get("listName"));
                    Log.e("DEBUG", String.valueOf(trip.getAuthorId()));
                    allTrips.add(trip);
                    tripsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("DEBUG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }

    private void modifyBuyList() {

    }

    private void deleteBuyList() {

    }

}
