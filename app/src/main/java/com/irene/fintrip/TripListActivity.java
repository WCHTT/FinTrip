package com.irene.fintrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.irene.fintrip.Utils.DatabaseUtil;
import com.irene.fintrip.adapters.TripListAdapter;
import com.irene.fintrip.fragment.TripItemFragment;
import com.irene.fintrip.models.Trip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TripListActivity extends AppCompatActivity implements TripItemFragment.TripItemDialogListener{

    private DatabaseReference mDatabase;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    private ArrayList<Trip> allTrips;
    private TripListAdapter tripsAdapter;

    private FirebaseUser user;

    private ListView lvTrips;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triplist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tripToolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);



        mDatabase = DatabaseUtil.getDatabase().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
//        writeNewBuyList(user.getUid(), "Irene", sdf.format(new Date()),"America");
        allTrips = new ArrayList<Trip>();
        getNewBuyList(user.getUid());
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
                        i.putExtra("tripId", tripsAdapter.getItem(pos).getBuyListId());
                        startActivity(i);
                    }
                }
        );//Home
        lvTrips.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos,long id) {
                        deleteBuyList(allTrips.get(pos).getAuthorId(), allTrips.get(pos).getBuyListId());
                        return true;
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trips, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.miTrip) {
            openTripFragment(null);
        }
//        else if (id==R.id.action_other)
//        {
//            Toast toast=Toast.makeText(this, "Other Clicked.", Toast.LENGTH_LONG);
//            toast.show();
//        }
        return super.onOptionsItemSelected(item);
    }

    public void openTripFragment(Trip trip) {
        FragmentManager fm = getSupportFragmentManager();
        TripItemFragment tripitemDialogFragment = TripItemFragment.newInstance(trip);
        tripitemDialogFragment.show(fm, "fragment_trip_item");
    }

    //write
    private void writeNewBuyList(String authorId, String authorName, String createdTime, Long createdTimeStampOrder, String listName) {
        // Create new item at /user-buylists/$userid/$buylistid and at
        String key = mDatabase.child("user-buylists").child(authorId).push().getKey();
        Trip trip = new Trip(key, authorId, authorName, createdTime, createdTimeStampOrder, listName);

        Map<String, Object> tripValues = trip.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/buylists/" + key, postValues);
        childUpdates.put("/user-buylists/" + authorId + "/" + key, tripValues);

        mDatabase.updateChildren(childUpdates);
    }

    //read
    private void getNewBuyList(String authorId) {

        //final ArrayList<Trip> trips = new ArrayList<Trip>();

        Query myTopTripsQuery = mDatabase.child("user-buylists").child(authorId).orderByChild("createdTimeStampOrder").limitToFirst(100);
        myTopTripsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allTrips.clear();
                for (DataSnapshot tripSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Map<String, Object> tripValues = (Map<String, Object>) tripSnapshot.getValue();
                    Trip trip = new Trip((String)tripValues.get("buyListId"),(String)tripValues.get("authorId"),(String)tripValues.get("authorName"),(String)tripValues.get("createdTime"), (Long)tripValues.get("createdTimeStampOrder"),(String)tripValues.get("listName"));
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
        mDatabase.child("buylist-items").child(tripId).removeValue();
    }

    @Override
    public void onFinishEditDialog(Trip trip, String condition) {
        Log.e("DEBUG",condition+","+trip.getListName());
        if(condition.equals("new")) {
            Date now = new Date();
            writeNewBuyList(user.getUid(), user.getDisplayName(), sdf.format(now), (-1)* now.getTime(),trip.getListName());
        }
    }

}
