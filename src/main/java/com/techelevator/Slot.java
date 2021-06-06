package com.techelevator;

import java.util.Objects;

public class Slot {

    private final String SLOT_ID;          //A7, B3, etc.
    private Product product;        //product in this particular slot
    private int quantity = 5;       //quantity on hand always starts at 5 when vending machine is initialized

    public Slot(String slotID, Product product) {
        this.SLOT_ID = slotID;
        this.product = product;
    }

    public String getSlotID() {
        return SLOT_ID;
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

    public void setQuantity(int newQuantity) {
        if (newQuantity >= 0) {
            this.quantity = newQuantity;
        }
    }

    //when an item is sold, reduces current quantity in this slot by 1 IF quantity is greater than zero
    public void sellItem() {
        if (this.quantity > 0) {
            this.quantity -= 1;
        }
    }

    //prints out relevant info about what is in this particular slot
    @Override
    public String toString() {

        //if the product is sold out, specify that nothing is in this slot
        if (quantity == 0) {
            return "Slot " + SLOT_ID + ": SOLD OUT.";
        }

        //if the product is not sold out, return the slot ID, product name, product price and quantity in stock
        return "Slot " + SLOT_ID + ": " + product.getName() + ", $" + product.getPrice() + ", " + quantity + " in stock";
    }

}
