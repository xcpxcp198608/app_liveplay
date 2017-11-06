package com.wiatec.bplay.pay;

/**
 * Created by patrick on 31/10/2017.
 * create time : 10:33 AM
 */
public class PayInfo{

    private float price;
    private String currency;
    private String description;

    public PayInfo() {
    }

    public PayInfo(float price, String currency, String description) {
        this.price = price;
        this.currency = currency;
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PayInfo{" +
                "price=" + price +
                ", currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}