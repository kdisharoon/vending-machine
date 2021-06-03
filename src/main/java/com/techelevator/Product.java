package com.techelevator;

import java.math.BigDecimal;

public class Product {

    private String name;
    private BigDecimal price;
    private String saleMessage;

    public Product(String productName, BigDecimal productPrice) {
        this.name = productName;
        this.price = productPrice;
        this.saleMessage = ", Yum!";
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSaleMessage() {
        return saleMessage;
    }
}
