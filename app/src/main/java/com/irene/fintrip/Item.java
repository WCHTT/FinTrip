package com.irene.fintrip;

import org.parceler.Parcel;

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


    boolean isBuy;
    String imageUrl;
    String owner;
    Double price;
}
