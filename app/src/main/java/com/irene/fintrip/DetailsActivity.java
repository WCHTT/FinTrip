package com.irene.fintrip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.irene.fintrip.API.FixerAPIEndpointInterface;
import com.irene.fintrip.Utils.DatabaseUtil;
import com.irene.fintrip.fragment.DisplayPictureFragment;
import com.irene.fintrip.fragment.EditItemFragment;
import com.irene.fintrip.model.CurrencyExchange;
import com.irene.fintrip.model.Rates;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.irene.fintrip.HomeActivity.SELECT_PICTURE;
import static com.irene.fintrip.R.id.location;

public class DetailsActivity extends AppCompatActivity  implements EditItemFragment.EditItemDialogListener {
    String mCurrentPhotoPath;
    File photoFile;
    public static final String BASE_URL = "http://api.fixer.io";
    private Spinner spinner;
    private Rates rates;
    private TextView tvTargetPrice;

    private Item item;
    private String tripID;
    private TextView owner;
    private TextView etPrice;
    private TextView tvLocation;
    private TextView priceCurrency;
    private ImageView ivItemImage;

    private ImageView locationMark;
    private ImageView detailsPic;
    private LinearLayout rlTargetPrice;
    private String baseCurrency;
    private String[] currency = {"JPY", "USD"};
    private Double itemPrice;
    private Address address;
    private LocationManager lms;
    private String gpsProvider;
    private String networkProvider;
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int TAG_CODE_PERMISSION_LOCATION = 111;


    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        mDatabase = DatabaseUtil.getDatabase().getReference();

        locationServiceInitial();

        //if (!canAccessLocation() ) {
        //    requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        //}

        Locale defaultLocale = Locale.getDefault();
        displayCurrencyInfoForLocale(defaultLocale);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            // do nothing
        } else {
            Toast.makeText(this, "no permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
        }

        spinner = (Spinner) findViewById(R.id.tCurrencySpinner);


        ArrayAdapter<String> currencyList = new ArrayAdapter<>(DetailsActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                currency);
        spinner.setAdapter(currencyList);

        tvLocation = (TextView) findViewById(location);
        owner = (TextView) findViewById(R.id.buyerName);
        tvTargetPrice = (TextView) findViewById(R.id.tPrice);
        etPrice = (TextView) findViewById(R.id.price);
        priceCurrency = (TextView) findViewById(R.id.priceCurrency);
        ivItemImage = (ImageView) findViewById(R.id.ivItemImage);
        locationMark  = (ImageView) findViewById(R.id.locationMark);
        detailsPic  = (ImageView) findViewById(R.id.detailsPic);
        rlTargetPrice  = (LinearLayout) findViewById(R.id.rlTargetPrice);

        item =  (Item) Parcels.unwrap(getIntent().getParcelableExtra("item"));
        tripID = getIntent().getExtras().getString("tripId");

        // Required item
        if(item.getImageUrl()!= null && !item.getImageUrl().equals("")){
            Glide.with(getBaseContext())
                    .load(item.getImageUrl())
                    //.load("http://pic.pimg.tw/omifind/1468387801-1461333924.jpg")
                    .centerCrop()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivItemImage);
        }
        // optional
        if(item.getOwner()!=null && !item.getOwner().equals(""))
            owner.setText(item.getOwner());
        else
            owner.setVisibility(View.GONE);

        if(item.getPrice()!=0.0){
            itemPrice = item.getPrice();
            etPrice.setText(item.getPrice().toString());
            priceCurrency.setText(item.getPriceCurrency());
            tvLocation.setText(item.getLocation());

            prepareCurrencyExchangeSection(item.getPriceCurrency());
        }
        else{
            // hide price editText, location and section below
            etPrice.setVisibility(View.INVISIBLE);
            priceCurrency.setVisibility(View.INVISIBLE);
            tvLocation.setVisibility(View.INVISIBLE);
            locationMark.setVisibility(View.INVISIBLE);
            rlTargetPrice.setVisibility(View.INVISIBLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            detailsPic.setClipToOutline(true);
        }

        if(item.getPriceTagImageUrl()!=null && !item.getPriceTagImageUrl().equals(""))
        {
            detailsPic.setVisibility(View.VISIBLE);
            Glide.with(getBaseContext())
                    .load(item.getPriceTagImageUrl())
                    //.load("http://pic.pimg.tw/omifind/1468387801-1461333924.jpg")
                    .centerCrop()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(detailsPic);
        }
        else{
            detailsPic.setVisibility(View.GONE);
        }

        if(item.getTargetCurrency()!=null && !item.getTargetCurrency().equals("")) {
            setSpinnerToValue(spinner, item.getTargetCurrency());
        }

        ImageView priceTagImageCamera = (ImageView) findViewById(R.id.pic);
        //
        priceTagImageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

    }

    public void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }

    private void prepareCurrencyExchangeSection(String baseCurrency){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FixerAPIEndpointInterface apiService = retrofit.create(FixerAPIEndpointInterface.class);

        Call<CurrencyExchange> call = apiService.getLatest(baseCurrency);
        call.enqueue(new Callback<CurrencyExchange>() {
            @Override
            public void onResponse(Call<CurrencyExchange> call, Response<CurrencyExchange> response) {

                int statusCode = response.code();
                Log.d("DEBUG:statusCode",String.valueOf(statusCode));
                if(statusCode == 422){
                    rates = new Rates();
                    //rates.setNTD(30.0);
                    rates.setJPY(3.58);
                    rates.setUSD(0.0324);
                }
                else{
                    rates = response.body().getRates();
                }

                // TODO:get supported currency exchange rate for dropdown from api
                //final String[] currency = {"JPY", "KRW", "CNY"};
                //ArrayAdapter<String> currencyList = new ArrayAdapter<>(DetailsActivity.this,
                //        android.R.layout.simple_spinner_dropdown_item,
                //        currency);
                //spinner.setAdapter(currencyList);

                updateTargetCurrency();

                updateCurrency(rates);
            }

            @Override
            public void onFailure(Call<CurrencyExchange> call, Throwable t) {
                Query myTopItemsQuery = mDatabase.child("rates");
                myTopItemsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot != null)
                        {
                            rates= new Rates();

                                Map<String, Object> itemValues = (Map<String, Object>) dataSnapshot.getValue();
                                Log.e("DEBUG", String.valueOf(itemValues.get("price")));
                                rates = new Rates((Double) itemValues.get("JPY"),(Double)itemValues.get("USD"));

                        }

                        updateTargetCurrency();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w("DEBUG", "loadPost:onCancelled", databaseError.toException());

                    }
                });
            }
        });
    }
    private void updateTargetCurrency(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // update target price
                Double tPrice = item.getPrice() * rates.get(currency[position]);
                DecimalFormat df = new DecimalFormat("##.00");
                tPrice = Double.parseDouble(df.format(tPrice));
                tvTargetPrice.setText(tPrice.toString());

                item.setTargetCurrency(currency[position]);
                updateItemInDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(item.getTargetCurrency()!=null && !item.getTargetCurrency().equals("")) {
            Double tPrice = item.getPrice() * rates.get(item.getTargetCurrency());
            DecimalFormat df = new DecimalFormat("##.00");
            tPrice = Double.parseDouble(df.format(tPrice));
            tvTargetPrice.setText(tPrice.toString());
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch(requestCode) {
            case TAG_CODE_PERMISSION_LOCATION: {
                // Load location

               Locale defaultLocale = Locale.getDefault();
                displayCurrencyInfoForLocale(defaultLocale);

                if(canAccessLocation()){
                   // getCurrencyForCurrentLocation(locationServiceInitial());

                }

                break;
            }
        }
    }

    public static String displayCurrencyInfoForLocale(Locale locale) {
        Log.d("Locale: ", locale.getDisplayName());
        Currency currency = Currency.getInstance(locale);
        Log.d("Currency Code: ", currency.getCurrencyCode());
        Log.d("Symbol: ", currency.getSymbol());

        return currency.getCurrencyCode();
        //Log.d("Default Fraction Digits: ", currency.getDefaultFractionDigits());
    }

    private Address updateForCurrentLocation(Location location) {

        if(location==null)
            return null;

        final float latitude = (float) location.getLatitude();
        final float longitude = (float) location.getLongitude();
        Log.d("DEBUG:location",String.valueOf(latitude) + "," + String.valueOf(longitude));
        // Helper.log(TAG, "latitude: " +latitude);
        List<Address> addresses = null;
        Geocoder gcd = new Geocoder(getBaseContext());

        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);

            if(addresses.size()>0){
                return addresses.get(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkAccessLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            // do nothing
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
        }
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
        }

        return false;
    }

    private void locationServiceInitial() {
        lms = (LocationManager) getSystemService(LOCATION_SERVICE); //取得系統定位服務
        /*
         //做法一,由程式判斷用GPS_provider
         if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
               location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);  //使用GPS定位座標
         }
         else if ( lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
         { location = lms.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //使用GPS定位座標
         }
         else
           {
               location=null;
           }
           */

        // 做法二,由Criteria物件判斷提供最準確的資訊
//        Criteria criteria = new Criteria();  //資訊提供者選取標準
//        bestProvider = lms.getBestProvider(criteria, true);    //選擇精準度最高的提供者
        gpsProvider = LocationManager.GPS_PROVIDER;
        networkProvider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            return null;
        }


//        Location location = lms.getLastKnownLocation(bestProvider);
        lms.requestLocationUpdates(networkProvider, 1000, 1000 ,locationListener);
        lms.requestLocationUpdates(gpsProvider, 1000, 1000 ,locationListener);

//        return location;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    public void onEditAction(MenuItem mi) {
        // open dialog
        FragmentManager fm = getSupportFragmentManager();
        EditItemFragment editNameDialogFragment = EditItemFragment.newInstance(item.getOwner(),item.getPrice());
        editNameDialogFragment.show(fm, "fragment_edit_item");
    }

    public void onClickDetailsPic(View v) {
        // open dialog
        FragmentManager fm = getSupportFragmentManager();
        DisplayPictureFragment picFragment = DisplayPictureFragment.newInstance(item.getPriceTagImageUrl());
        picFragment.show(fm, "fragment_picture");
    }

    @Override
    public void onFinishEditDialog(String ownerName, String price) {
        owner.setText(ownerName);
        item.setOwner(ownerName);

        etPrice.setText(price);
        itemPrice = Double.parseDouble(price);
        item.setPrice(itemPrice);
        if(address!=null){
            Locale locale = address.getLocale();

            // update view
            String priceCurrencyInfo = displayCurrencyInfoForLocale(locale);
            priceCurrency.setText(priceCurrencyInfo);
            item.setPriceCurrency(priceCurrencyInfo);

            priceCurrency.setVisibility(View.VISIBLE);

            String location = address.getLocality();
            tvLocation.setText(location);
            item.setLocation(location);

            prepareCurrencyExchangeSection(displayCurrencyInfoForLocale(locale));

            rlTargetPrice.setVisibility(View.VISIBLE);
        }

        updateItemInDB();

        etPrice.setVisibility(View.VISIBLE);
        tvLocation.setVisibility(View.VISIBLE);
        locationMark.setVisibility(View.VISIBLE);
        owner.setVisibility(View.VISIBLE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {

            Uri imageURI = null;
            File f = new File(mCurrentPhotoPath);
            try {

                if(data != null && data.getData() != null){
                    f.delete();
                    imageURI = data.getData();
                } else {
                    imageURI = Uri.fromFile(f);
                    // galleryAddPic(imageURI);
                }
                Log.d("imageURI",imageURI.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

            item.setPriceTagImageUrl(imageURI.toString());

            //display detailsPic

            detailsPic.setVisibility(View.VISIBLE);
            Glide.with(getBaseContext())
                    .load(imageURI.toString())
                    //.load("http://pic.pimg.tw/omifind/1468387801-1461333924.jpg")
                    .centerCrop()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(detailsPic);

            updateItemInDB();
        }
    }

    private void updateItemInDB(){
        // save item back to db
        updateItemList(tripID,item);
    }

    //update
    private void updateItemList(String tripId, Item modifyItem) {
        Map<String, Object> itemValues = modifyItem.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/buylist-items/" + tripId+"/"+modifyItem.getItemId(), itemValues);

        mDatabase.updateChildren(childUpdates);
    }

    private void updateCurrency(Rates rates) {
        Map<String, Object> rateValues = rates.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/rates", rateValues);

        mDatabase.updateChildren(childUpdates);

    }

    public LocationListener locationListener = new LocationListener()
    {

        @Override
        public void onLocationChanged(Location location)
        {
            if(location != null)
                address = updateForCurrentLocation(location);
        }
        @Override
        public void onProviderDisabled(String provider){

        }
        @Override
        public void onProviderEnabled(String provider){

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){

        }
    };

//    public LocationListener networklocationListener = new LocationListener()
//    {
//
//        @Override
//        public void onLocationChanged(Location location)
//        {
//            if(location != null)
//                address = updateForCurrentLocation(location);
//        }
//        @Override
//        public void onProviderDisabled(String provider){
//
//        }
//        @Override
//        public void onProviderEnabled(String provider){
//
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras){
//
//        }
//    };



}
