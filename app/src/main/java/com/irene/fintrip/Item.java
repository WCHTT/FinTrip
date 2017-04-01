package com.irene.fintrip;

import com.google.firebase.database.Exclude;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChaoJung on 2017/3/20.
 */

@Parcel
public class Item {

    public Item() {
    }

    public Item(boolean isBuy, String imageUrl, String owner, Double price) {
        this.isBuy = isBuy;
        this.imageUrl = imageUrl;
        this.owner = owner;
        this.price = price;
    }

    public Item(boolean isBuy, String imageUrl, String owner, Double price, String location, String priceTagImageUrl, String targetCurrency, String priceCurrency, boolean isPaid, String createdTime, Long createdTimeStampOrder) {
        this.isBuy = isBuy;
        this.imageUrl = imageUrl;
        this.owner = owner;
        this.price = price;
        this.location = location;
        this.priceTagImageUrl = priceTagImageUrl;
        this.targetCurrency = targetCurrency;
        this.priceCurrency = priceCurrency;
        this.isPaid = isPaid;
        this.createdTime = createdTime;
        this.createdTimeStampOrder = createdTimeStampOrder;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("isBuy", isBuy);
        result.put("imageUrl", imageUrl);
        result.put("owner", owner);
        result.put("price",price);
        result.put("location", location);
        result.put("priceTagImageUrl", priceTagImageUrl);
        result.put("targetCurrency", targetCurrency);
        result.put("priceCurrency", priceCurrency);
        result.put("isPaid",isPaid);
        result.put("createdTime",createdTime);
        result.put("createdTimeStampOrder",createdTimeStampOrder);

        return result;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPriceTagImageUrl(String priceTagImageUrl) {
        this.priceTagImageUrl = priceTagImageUrl;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOwner() {
        return owner;
    }

    public Double getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String getPriceTagImageUrl() {
        return priceTagImageUrl;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }


    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isPaid() {

        return isPaid;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Long getCreatedTimeStampOrder() {
        return createdTimeStampOrder;
    }

    public void setCreatedTimeStampOrder(Long createdTimeStampOrder) {
        this.createdTimeStampOrder = createdTimeStampOrder;
    }

    boolean isBuy;
    String imageUrl;
    String owner;
    Double price;
    String location;
    String priceTagImageUrl;
    String targetCurrency;
    String priceCurrency; /*local: travel location*/
    boolean isPaid; /*is owner paid for this item or not*/
    String createdTime;
    Long createdTimeStampOrder;
}
