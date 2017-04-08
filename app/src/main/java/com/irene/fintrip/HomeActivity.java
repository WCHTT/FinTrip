package com.irene.fintrip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.irene.fintrip.Utils.DatabaseUtil;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    String mCurrentPhotoPath;
    File photoFile;

    public static final int REQUEST_IMAGE_CAPTURE = 3;
    public static final int SELECT_PICTURE = 1;
    public static final int REQUEST_TAKE_PHOTO = 2;
    public static final int  MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public final static int PICK_PHOTO_CODE = 1046;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");


    ItemAdapter itemAdapter;
    RecyclerView rvToBuyItem;
    FloatingActionButton fabCreate;
    List<Item> items;
    String tripID;
    String key;  //itemID for new item

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDatabase = DatabaseUtil.getDatabase().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.homeToolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        TextView tvToolbar = (TextView) findViewById(R.id.tvToolbar);
        Bundle extras = getIntent().getExtras();
        tvToolbar.setText("- "+extras.getString("tripName"));
        tripID = extras.getString("tripId");

        Log.d("DEBUG:tripID",tripID);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                    Manifest.permission.CAMERA)) {

            } else {

                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

        setupView();
        createItem();
    }

    private void setupView() {

        rvToBuyItem = (RecyclerView) findViewById(R.id.rvRequest);
        // Initialize contacts
        items = new ArrayList<>();

        getItemList(tripID);
//        items = Item.createItemList(1);
        // Create adapter passing in the sample user data
        itemAdapter = new ItemAdapter(this,items);
        // Attach the adapter to the recyclerview to populate items
        rvToBuyItem.setAdapter(itemAdapter);
        // Set layout manager to position the items
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvToBuyItem.setLayoutManager(linearLayoutManager);
        rvToBuyItem.setHasFixedSize(true);



        ItemClickSupport.addTo(rvToBuyItem).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // do it
                Intent i = new Intent(getApplicationContext(),DetailsActivity.class);
                i.putExtra("item", Parcels.wrap(items.get(position)));
                i.putExtra("tripId", tripID);
                startActivity(i);
            }
        });

        ItemClickSupport.addTo(rvToBuyItem).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                deleteItem(tripID,items.get(position).getItemId());
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void onChargeAction(MenuItem mi) {
        Intent i = new Intent(getApplicationContext(),ChargeActivity.class);
        i.putExtra("tripID",tripID);
        startActivity(i);
    }

    private void createItem() {
        fabCreate = (FloatingActionButton) findViewById(R.id.fabCreate);
        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                else {
                    finish();
                }
                return;
            }
        }
    }

    private void galleryAddPic(Uri contentUri) {

        Intent mediaScanIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        }
        else {
            mediaScanIntent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
        }
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

    }

    private void dispatchTakePictureIntent() {

        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra
                (
                        Intent.EXTRA_INITIAL_INTENTS,
                        new Intent[] { takePictureIntent }
                );
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("CameraTest",ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.irene.fintrip.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
        }
        startActivityForResult(chooserIntent, SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {

            Uri imageURI = null;
            File f = new File(mCurrentPhotoPath);
            try {

                if(data != null && data.getData() != null){
                    f.delete();
                    imageURI = data.getData();
                } else {
                    imageURI = Uri.fromFile(f);
                    galleryAddPic(imageURI);
                }
                Log.d("imageURI",imageURI.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

            Date createTime = new Date();

            writeItemList(tripID,false,imageURI.toString(),"",0.0,"","","","",false,sdf.format(createTime),(-1)* createTime.getTime());
            Item item = new Item(key,false,imageURI.toString(),"",0.0);

//            items.add(0,item);
//            itemAdapter.notifyItemInserted(0);

            Intent i = new Intent(getApplicationContext(),DetailsActivity.class);
            i.putExtra("item", Parcels.wrap(item));
            i.putExtra("tripId", tripID);
            startActivity(i);

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        final String appDirectoryName = "FinTrip";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),appDirectoryName);
        if (!storageDir.exists()){
            if (!storageDir.mkdirs()){
                Log.d("CameraTest", "failed to create directory");
            }
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //write
    private void writeItemList(String tripId, boolean isBuy, String imageUrl, String owner, Double price, String location, String priceTagImageUrl, String targetCurrency, String priceCurrency, boolean isPaid, String createdTime, Long createdTimeStampOrder) {
        // Create new item at /user-buylists/$userid/$buylistid and at
        //String key = mDatabase.child("buylist-items").child(tripId).push().getKey();
        key = mDatabase.child("buylist-items").child(tripId).push().getKey();
        Log.e("DEBUG",tripId + " " + key);
        Item item = new Item(key, isBuy,imageUrl,owner,price,location,priceTagImageUrl,targetCurrency,priceCurrency,isPaid, createdTime,createdTimeStampOrder);

        Map<String, Object> itemValues = item.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/buylist-items/" + tripId + "/" + key, itemValues);

        mDatabase.updateChildren(childUpdates);
    }

    //read
    private void getItemList(String tripId) {

        Query myTopItemsQuery = mDatabase.child("buylist-items").child(tripId).orderByChild("createdTimeStampOrder").limitToFirst(100);
        myTopItemsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot != null)
                {
                    Log.e("DEBUG", "not null");
                    items.clear();
                    for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                        // TODO: handle the post
                        Map<String, Object> itemValues = (Map<String, Object>) itemSnapshot.getValue();
                        Log.e("DEBUG", String.valueOf(itemValues.get("price")));
                        Item item = new Item((String)itemValues.get("itemId"),(boolean)itemValues.get("isBuy"),(String)itemValues.get("imageUrl"),(String)itemValues.get("owner"),((Number)itemValues.get("price")).doubleValue(),(String)itemValues.get("location"),(String)itemValues.get("priceTagImageUrl"),(String)itemValues.get("targetCurrency"),(String)itemValues.get("priceCurrency"),(boolean)itemValues.get("isPaid"),(String)itemValues.get("createdTime"), (Long)itemValues.get("createdTimeStampOrder"));
                        Log.e("DEBUG", String.valueOf(item.getOwner()));
                        items.add(item);

                    }
                    itemAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("DEBUG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }

    //delete
    private void deleteItem(String tripId, String itemId) {
        mDatabase.child("buylist-items").child(tripId).child(itemId).removeValue();
    }

    //update
    public void updateIsBuy(String tripId, String itemId, boolean isBuy) {
        mDatabase.child("buylist-items").child(tripId).child(itemId).child("isBuy").setValue(isBuy);
    }

    //update
    public void updateIsPaid(String tripId, String itemId, boolean isPaid) {
        mDatabase.child("buylist-items").child(tripId).child(itemId).child("isPaid").setValue(isPaid);
    }

    public String getTripID(){
        return tripID;
    }

}
