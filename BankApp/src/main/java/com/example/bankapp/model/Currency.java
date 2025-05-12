package com.example.bankapp.model;

public class Currency {
    private String currencyCode;
    private String currencyName;
    private double pricePerRub;

    public Currency() {}
    public Currency(String code, String name, double price) {
        this.currencyCode = code;
        this.currencyName = name;
        this.pricePerRub = price;
    }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getCurrencyName() { return currencyName; }
    public void setCurrencyName(String currencyName) { this.currencyName = currencyName; }
    public double getPricePerRub() { return pricePerRub; }
    public void setPricePerRub(double pricePerRub) { this.pricePerRub = pricePerRub; }
    @Override
    public String toString() { return currencyCode; }
}