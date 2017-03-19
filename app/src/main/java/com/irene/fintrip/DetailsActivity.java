package com.irene.fintrip;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.irene.fintrip.API.FixerAPIEndpointInterface;
import com.irene.fintrip.fragment.EditItemFragment;
import com.irene.fintrip.model.CurrencyExchange;
import com.irene.fintrip.model.Rates;

import java.io.IOException;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity  implements EditItemFragment.EditItemDialogListener {

    public static final String BASE_URL = "http://api.fixer.io";
    private Spinner spinner;
    private Rates rates;
    private TextView tvTargetPrice;

    private TextView owner;
    private TextView etPrice;
    private TextView tvLocation;
    private TextView priceCurrency;

    private String baseCurrency;
    private Double itemPrice;
    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int TAG_CODE_PERMISSION_LOCATION = 111;


    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

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

        final String[] currency = {"JPY", "KRW", "CNY"};
        ArrayAdapter<String> currencyList = new ArrayAdapter<>(DetailsActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                currency);
        spinner.setAdapter(currencyList);

        tvLocation = (TextView) findViewById(R.id.location);
        owner = (TextView) findViewById(R.id.buyerName);
        tvTargetPrice = (TextView) findViewById(R.id.tPrice);
        etPrice = (TextView) findViewById(R.id.price);
        priceCurrency = (TextView) findViewById(R.id.priceCurrency);

        //etPrice = (EditText) findViewById(R.id.price);
        itemPrice = 100.0;
        // Load data from DB for this item

        // Load item's picture


        // show target currency options if price is input
        // use USD as default price currency first
        baseCurrency = "USD";

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
                rates = response.body().getRates();

                // TODO: 3/19/2017
                //final String[] currency = {"JPY", "KRW", "CNY"};
                //ArrayAdapter<String> currencyList = new ArrayAdapter<>(DetailsActivity.this,
                //        android.R.layout.simple_spinner_dropdown_item,
                //        currency);
                //spinner.setAdapter(currencyList);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(DetailsActivity.this, "你選的是" + currency[position], Toast.LENGTH_SHORT).show();

                        // update target price
                        Double tPrice = itemPrice * rates.get(currency[position]);
                        tvTargetPrice.setText(tPrice.toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<CurrencyExchange> call, Throwable t) {
                // Log error here since request failed
            }
        });


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


    private void updateForCurrentLocation() {
        final Location location = locationServiceInitial();
        final float latitude = (float) location.getLatitude();
        final float longitude = (float) location.getLongitude();
        // Helper.log(TAG, "latitude: " +latitude);
        List<Address> addresses = null;
        Geocoder gcd = new Geocoder(getBaseContext());

        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);

            Locale locale = addresses.get(0).getLocale();

            // update view
            priceCurrency.setText(displayCurrencyInfoForLocale(locale));
            tvLocation.setText(addresses.get(0).getLocality());
        } catch (IOException e) {
            e.printStackTrace();
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

    private Location locationServiceInitial() {
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
        Criteria criteria = new Criteria();  //資訊提供者選取標準
        bestProvider = lms.getBestProvider(criteria, true);    //選擇精準度最高的提供者
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location location = lms.getLastKnownLocation(bestProvider);
        return location;
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
        EditItemFragment editNameDialogFragment = EditItemFragment.newInstance();
        editNameDialogFragment.show(fm, "fragment_edit_item");

    }

    @Override
    public void onFinishEditDialog(String ownerName, String price) {
        // TODO: update view
        owner.setText(ownerName);
        etPrice.setText(price);
        itemPrice = Double.parseDouble(price);

        // TODO: upload location
        updateForCurrentLocation();

        /*Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
        if (addresses.size() > 0)
        {
            System.out.println(addresses.get(0).getLocality());
        }
        else
        {
            // do your staff
        }

        // location.setText
        priceCurrency.setText(getCurrencyForCurrentLocation());*/
    }
}