package com.irene.fintrip.model;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by reneewu on 3/17/2017.
 */

public class Rates {

    @SerializedName("AUD")
    @Expose
    private Double aUD;
    @SerializedName("BGN")
    @Expose
    private Double bGN;
    @SerializedName("BRL")
    @Expose
    private Double bRL;
    @SerializedName("CAD")
    @Expose
    private Double cAD;
    @SerializedName("CHF")
    @Expose
    private Double cHF;
    @SerializedName("CNY")
    @Expose
    private Double cNY;
    @SerializedName("CZK")
    @Expose
    private Double cZK;
    @SerializedName("DKK")
    @Expose
    private Double dKK;
    @SerializedName("GBP")
    @Expose
    private Double gBP;
    @SerializedName("HKD")
    @Expose
    private Double hKD;
    @SerializedName("HRK")
    @Expose
    private Double hRK;
    @SerializedName("HUF")
    @Expose
    private Double hUF;
    @SerializedName("IDR")
    @Expose
    private Integer iDR;
    @SerializedName("ILS")
    @Expose
    private Double iLS;
    @SerializedName("INR")
    @Expose
    private Double iNR;
    @SerializedName("JPY")
    @Expose
    private Double jPY;
    @SerializedName("KRW")
    @Expose
    private Double kRW;
    @SerializedName("MXN")
    @Expose
    private Double mXN;
    @SerializedName("MYR")
    @Expose
    private Double mYR;
    @SerializedName("NOK")
    @Expose
    private Double nOK;
    @SerializedName("NZD")
    @Expose
    private Double nZD;
    @SerializedName("PHP")
    @Expose
    private Double pHP;
    @SerializedName("PLN")
    @Expose
    private Double pLN;
    @SerializedName("RON")
    @Expose
    private Double rON;
    @SerializedName("RUB")
    @Expose
    private Double rUB;
    @SerializedName("SEK")
    @Expose
    private Double sEK;
    @SerializedName("SGD")
    @Expose
    private Double sGD;
    @SerializedName("THB")
    @Expose
    private Double tHB;
    @SerializedName("TRY")
    @Expose
    private Double tRY;
    @SerializedName("ZAR")
    @Expose
    private Double zAR;
    @SerializedName("EUR")
    @Expose
    private Double eUR;

    public Double getNTD() {
        return NTD;
    }

    public void setNTD(Double NTD) {
        this.NTD = NTD;
    }

    private Double NTD;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("AUD", aUD);
        result.put("BGN", bGN);
        result.put("BRL", bRL);
        result.put("CAD", cAD);
        result.put("CHF", cHF);
        result.put("CNY", cNY);
        result.put("CZK", cZK);
        result.put("DKK", dKK);
        result.put("GBP", gBP);
        result.put("HKD", hKD);
        result.put("HRK", hRK);
        result.put("HUF", hUF);
        result.put("IDR", iDR);
        result.put("ILS", iLS);
        result.put("INR", iNR);
        result.put("JPY", jPY);
        result.put("KRW", kRW);
        result.put("MXN", mXN);
        result.put("MYR", mYR);
        result.put("NOK", nOK);
        result.put("NZD", nZD);
        result.put("PHP", pHP);
        result.put("PLN", pLN);
        result.put("RON", rON);
        result.put("RUB", rUB);
        result.put("SEK", sEK);
        result.put("SGD", sGD);
        result.put("THB", tHB);
        result.put("TRY", tRY);
        result.put("ZAR", zAR);
        return result;
    }

    public Double getAUD() {
        return aUD;
    }

    public void setAUD(Double aUD) {
        this.aUD = aUD;
    }

    public Double getBGN() {
        return bGN;
    }

    public void setBGN(Double bGN) {
        this.bGN = bGN;
    }

    public Double getBRL() {
        return bRL;
    }

    public void setBRL(Double bRL) {
        this.bRL = bRL;
    }

    public Double getCAD() {
        return cAD;
    }

    public void setCAD(Double cAD) {
        this.cAD = cAD;
    }

    public Double getCHF() {
        return cHF;
    }

    public void setCHF(Double cHF) {
        this.cHF = cHF;
    }

    public Double getCNY() {
        return cNY;
    }

    public void setCNY(Double cNY) {
        this.cNY = cNY;
    }

    public Double getCZK() {
        return cZK;
    }

    public void setCZK(Double cZK) {
        this.cZK = cZK;
    }

    public Double getDKK() {
        return dKK;
    }

    public void setDKK(Double dKK) {
        this.dKK = dKK;
    }

    public Double getGBP() {
        return gBP;
    }

    public void setGBP(Double gBP) {
        this.gBP = gBP;
    }

    public Double getHKD() {
        return hKD;
    }

    public void setHKD(Double hKD) {
        this.hKD = hKD;
    }

    public Double getHRK() {
        return hRK;
    }

    public void setHRK(Double hRK) {
        this.hRK = hRK;
    }

    public Double getHUF() {
        return hUF;
    }

    public void setHUF(Double hUF) {
        this.hUF = hUF;
    }

    public Integer getIDR() {
        return iDR;
    }

    public void setIDR(Integer iDR) {
        this.iDR = iDR;
    }

    public Double getILS() {
        return iLS;
    }

    public void setILS(Double iLS) {
        this.iLS = iLS;
    }

    public Double getINR() {
        return iNR;
    }

    public void setINR(Double iNR) {
        this.iNR = iNR;
    }

    public Double getJPY() {
        return jPY;
    }

    public void setJPY(Double jPY) {
        this.jPY = jPY;
    }

    public Double getKRW() {
        return kRW;
    }

    public void setKRW(Double kRW) {
        this.kRW = kRW;
    }

    public Double getMXN() {
        return mXN;
    }

    public void setMXN(Double mXN) {
        this.mXN = mXN;
    }

    public Double getMYR() {
        return mYR;
    }

    public void setMYR(Double mYR) {
        this.mYR = mYR;
    }

    public Double getNOK() {
        return nOK;
    }

    public void setNOK(Double nOK) {
        this.nOK = nOK;
    }

    public Double getNZD() {
        return nZD;
    }

    public void setNZD(Double nZD) {
        this.nZD = nZD;
    }

    public Double getPHP() {
        return pHP;
    }

    public void setPHP(Double pHP) {
        this.pHP = pHP;
    }

    public Double getPLN() {
        return pLN;
    }

    public void setPLN(Double pLN) {
        this.pLN = pLN;
    }

    public Double getRON() {
        return rON;
    }

    public void setRON(Double rON) {
        this.rON = rON;
    }

    public Double getRUB() {
        return rUB;
    }

    public void setRUB(Double rUB) {
        this.rUB = rUB;
    }

    public Double getSEK() {
        return sEK;
    }

    public void setSEK(Double sEK) {
        this.sEK = sEK;
    }

    public Double getSGD() {
        return sGD;
    }

    public void setSGD(Double sGD) {
        this.sGD = sGD;
    }

    public Double getTHB() {
        return tHB;
    }

    public void setTHB(Double tHB) {
        this.tHB = tHB;
    }

    public Double getTRY() {
        return tRY;
    }

    public void setTRY(Double tRY) {
        this.tRY = tRY;
    }

    /*
    public Double getUSD() {
        return uSD;
    }

    public void setUSD(Double uSD) {
        this.uSD = uSD;
    }
    */
    public Double getZAR() {
        return zAR;
    }

    public void setZAR(Double zAR) {
        this.zAR = zAR;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    // TODO: 3/19/2017
    public Double get(String currency){
        switch (currency){
            case "JPY":
                return getJPY();
            case "KRW":
                return getKRW();
            case "CNY":
                return getCNY();
            default:
                return getJPY();
        }
    }

}