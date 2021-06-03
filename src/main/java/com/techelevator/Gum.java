package com.techelevator;

import java.math.BigDecimal;

public class Gum extends Product {
    private String gumSlogan = "Chew Chew";

    public Gum(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public String getSaleMessage() {
        return gumSlogan + super.getSaleMessage();
    }
}
