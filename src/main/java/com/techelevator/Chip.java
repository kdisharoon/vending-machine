package com.techelevator;

public class Chip extends Product {

    private String chipSlogan = "Crunch Crunch";

    public Chip(String productName, double productPrice) {
        super(productName, productPrice);

    }

    @Override
    public String getSaleMessage() {
        return this.chipSlogan + super.getSaleMessage();
    }
}
