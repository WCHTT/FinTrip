package com.irene.fintrip.API;

import com.irene.fintrip.model.CurrencyExchange;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by reneewu on 3/17/2017.
 */

public interface FixerAPIEndpointInterface {
    @GET("latest")
    Call<CurrencyExchange> getLatest();

}
