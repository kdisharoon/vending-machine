package com.techelevator;

import java.math.BigDecimal;

public class Chip extends Product {

    private String chipSlogan = "Crunch Crunch";

    public Chip(String productName, BigDecimal productPrice) {
        super(productName, productPrice);

    }

    @Override
    public String getSaleMessage() {
        return this.chipSlogan + super.getSaleMessage();
    }
}
