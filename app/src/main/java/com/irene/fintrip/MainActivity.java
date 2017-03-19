package com.irene.fintrip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.irene.fintrip.API.FixerAPIEndpointInterface;
import com.irene.fintrip.model.CurrencyExchange;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://api.fixer.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FixerAPIEndpointInterface apiService = retrofit.create(FixerAPIEndpointInterface.class);

        Call<CurrencyExchange> call = apiService.getLatest();
        call.enqueue(new Callback<CurrencyExchange>() {
            @Override
            public void onResponse(Call<CurrencyExchange> call, Response<CurrencyExchange> response) {
                int statusCode = response.code();
                CurrencyExchange res = response.body();
                Log.d("return", res.getBase());
            }

            @Override
            public void onFailure(Call<CurrencyExchange> call, Throwable t) {
                // Log error here since request failed
            }
        });
    }
}
