package com.techelevator;

import java.math.BigDecimal;

public class Candy extends Product{
    private String candySlogan = "Munch Munch";

    public Candy(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public String getSaleMessage() {
        return this.candySlogan + super.getSaleMessage();
    }
}
