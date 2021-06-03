package com.techelevator;

public class Slot {

    private String slotName;        //A7, B3, etc.
    private Product product;        //product in this particular slot
    private int quantity = 5;           //quantity on hand

    public Slot(String slotName, Product product) {
        this.slotName = slotName;
        this.product = product;
    }

    public String getSlotName() {
        return slotName;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Slot " + slotName + ": " + product.getName() + ", $" + product.getPrice() + ", " + quantity + " in stock";
    }
}
