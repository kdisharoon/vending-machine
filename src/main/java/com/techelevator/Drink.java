package com.techelevator;

import java.math.BigDecimal;

public class Drink extends Product {
    private String drinkSlogan = "Glug Glug";

    public Drink(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public String getSaleMessage() {
        return drinkSlogan + super.getSaleMessage();
    }

    public static void main(String[] args) {
        Drink cola = new Drink("Coke", new BigDecimal(5));
        System.out.println(cola.getSaleMessage());
    }
}
