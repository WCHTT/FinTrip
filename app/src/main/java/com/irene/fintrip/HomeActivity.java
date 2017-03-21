package com.irene.fintrip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.util.Log;
import android.view.View;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    String mCurrentPhotoPath;
    File photoFile;


    public static final int REQUEST_IMAGE_CAPTURE = 3;
    public static final int SELECT_PICTURE = 1;
    public static final int REQUEST_TAKE_PHOTO = 2;
    public static final int  MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public final static int PICK_PHOTO_CODE = 1046;

    ItemAdapter itemAdapter;
    RecyclerView rvToBuyItem;
    FloatingActionButton fabCreate;
    List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                startActivity(i);
            }
        });

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
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
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

            } catch (Exception e) {
                e.printStackTrace();
            }

            Item item = new Item(true,imageURI.toString(),"","");
            items.add(0,item);
            itemAdapter.notifyItemInserted(0);
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

}
