package com.techelevator;

import java.math.BigDecimal;

public class Chip extends Product {

    private final String CHIP_SLOGAN = "Crunch Crunch";

    public Chip(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public String getSaleMessage() {
        return this.CHIP_SLOGAN + super.getSaleMessage();
    }
}
