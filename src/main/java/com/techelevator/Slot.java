package com.techelevator;

public class Slot {

    private String slotName;        //A7, B3, etc.
    private Product product;        //product in this particular slot
    private int quantity;           //quantity on hand

    public Slot(String slotName) {
        this.slotName = slotName;
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
}
