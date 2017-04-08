package com.irene.fintrip;

import java.util.List;

/**
 * Created by ChaoJung on 2017/4/2.
 */

public class Receipe {

    public Receipe(String owner, Double totalPrice, String targetCurrency, List<String> itemID) {
        this.owner = owner;
        this.totalPrice = totalPrice;
        this.targetCurrency = targetCurrency;
        this.itemID = itemID;
    }

    public Receipe(String owner, Double totalPrice, String targetCurrency) {
        this.owner = owner;
        this.totalPrice = totalPrice;
        this.targetCurrency = targetCurrency;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public List<String> getItemID() {
        return itemID;
    }

    public void setItemID(List<String> itemID) {
        this.itemID = itemID;
    }

    String owner;
    Double totalPrice;
    String targetCurrency;
    List<String> itemID;
}
