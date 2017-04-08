package com.irene.fintrip;

/**
 * Created by ChaoJung on 2017/4/2.
 */

public class Receipe {

    public Receipe(String owner, Double totalPrice, String targetCurrency) {
        this.owner = owner;
        this.totalPrice = totalPrice;
        this.targetCurrency = targetCurrency;
    }

    public Receipe(String owner, Double totalPrice) {
        this.owner = owner;
        this.totalPrice = totalPrice;
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

    String owner;
    Double totalPrice;
    String targetCurrency;
}
