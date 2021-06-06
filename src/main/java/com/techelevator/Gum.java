package com.techelevator;

import java.math.BigDecimal;

public class Gum extends Product {

    private final String GUM_SLOGAN = "Chew Chew";

    public Gum(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public String getSaleMessage() {
        return this.GUM_SLOGAN + super.getSaleMessage();
    }
}
