package com.techelevator;

public class Product {

    private String name;
    private double price;
    private String saleMessage;

    public Product(String productName, double productPrice) {
        this.name = productName;
        this.price = productPrice;
        this.saleMessage = ", Yum!";
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSaleMessage() {
        return saleMessage;
    }
}
