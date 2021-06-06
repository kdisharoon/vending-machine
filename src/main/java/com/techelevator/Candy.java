package com.techelevator;

import java.math.BigDecimal;

public class Candy extends Product {

    private final String CANDY_SLOGAN = "Munch Munch";

    public Candy(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public String getSaleMessage() {
        return this.CANDY_SLOGAN + super.getSaleMessage();
    }
}
