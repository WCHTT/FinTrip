package com.irene.fintrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.tripToolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        writeNewBuyList(user.getUid(), "Irene", sdf.format(new Date()),"America");
        allTrips = new ArrayList<Trip>();
        getNewBuyList("pB57jmHfCAhDy5lSpdW6mOWMOFg1");
//        deleteBuyList("pB57jmHfCAhDy5lSpdW6mOWMOFg1","-KfggbFdKY_Ll2vF7vRp");



        lvTrips = (ListView) findViewById(R.id.lvTrips);
        tripsAdapter = new TripListAdapter(this, allTrips);
        lvTrips.setAdapter(tripsAdapter);

        lvTrips.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        Intent i = new Intent(TripListActivity.this, HomeActivity.class);
                        i.putExtra("tripName",tripsAdapter.getItem(pos).getListName());
                        startActivity(i);
                    }
                }
        );//Home

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trips, menu);
        return true;
    }

    //write
    private void writeNewBuyList(String authorId, String authorName, String createdTime, String listName) {
        // Create new item at /user-buylists/$userid/$buylistid and at
        String key = mDatabase.child("user-buylists").child(authorId).push().getKey();
        Trip trip = new Trip(key, authorId, authorName, createdTime, listName);

        Map<String, Object> tripValues = trip.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/buylists/" + key, postValues);
        childUpdates.put("/user-buylists/" + authorId + "/" + key, tripValues);

        mDatabase.updateChildren(childUpdates);
    }

    //read
    private void getNewBuyList(String authorId) {

        //final ArrayList<Trip> trips = new ArrayList<Trip>();

        Query myTopPostsQuery = mDatabase.child("user-buylists").child(authorId).limitToFirst(100);
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Map<String, String> tripValues = (Map<String, String>) postSnapshot.getValue();
                    Trip trip = new Trip(tripValues.get("buyListId"),tripValues.get("authorId"),tripValues.get("authorName"),tripValues.get("createdTime"), tripValues.get("listName"));
                    Log.e("DEBUG", String.valueOf(trip.getAuthorId()));
                    allTrips.add(trip);

//                    allTrips.get(0).setListName("America");
//
//                    modifyBuyList("pB57jmHfCAhDy5lSpdW6mOWMOFg1","-Kf_zIeT765Znw4D8TBu",allTrips.get(0));
                }
                tripsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("DEBUG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }

    //update
    private void modifyBuyList(String authorId, String tripId, Trip modifyTrip) {
        Map<String, Object> tripValues = modifyTrip.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/user-buylists/" + authorId + "/" + tripId, tripValues);

        mDatabase.updateChildren(childUpdates);
    }

    //delete
    private void deleteBuyList(String authorId, String tripId) {
        mDatabase.child("user-buylists").child(authorId).child(tripId).removeValue();
    }

}
