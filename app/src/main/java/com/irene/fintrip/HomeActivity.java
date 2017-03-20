package com.irene.fintrip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
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
    private void dispatchTakePictureIntent() {

        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

        String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra
                (
                        Intent.EXTRA_INITIAL_INTENTS,
                        new Intent[] { takePictureIntent }
                );

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooserIntent, SELECT_PICTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {

            Bitmap imageBitmap = null;
            try {

                if(data != null && data.getData() != null){
//                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
//                BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(inputStream, false);
//                imageBitmap = decoder.decodeRegion(new Rect(10, 10, 50, 50), null);
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                }
                else{
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.irene.fintrip.fileprovider", photoFile);
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");

            Item item = new Item(true,BitMapToString(imageBitmap),"","");
            items.add(0,item);
            itemAdapter.notifyItemInserted(0);
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
