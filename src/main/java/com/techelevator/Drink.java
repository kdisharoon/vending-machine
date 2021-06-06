package com.techelevator;

import java.math.BigDecimal;

public class Drink extends Product {

    private final String DRINK_SLOGAN = "Glug Glug";

    public Drink(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public String getSaleMessage() {
        return this.DRINK_SLOGAN + super.getSaleMessage();
    }

}
