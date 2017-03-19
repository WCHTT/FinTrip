package com.irene.fintrip.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by reneewu on 3/17/2017.
 */

public class CurrencyExchange {
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("rates")
    @Expose
    private Rates rates;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Rates getRates() {
        return rates;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
