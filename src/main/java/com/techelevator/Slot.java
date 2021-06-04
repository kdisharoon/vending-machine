package com.techelevator;

public class Slot {

    private String slotID;        //A7, B3, etc.
    private Product product;        //product in this particular slot
    private int quantity = 5;           //quantity on hand

    public Slot(String slotID, Product product) {
        this.slotID = slotID;
        this.product = product;
    }

    public String getSlotID() {
        return slotID;
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

    public void sellItem() {
        quantity -= 1;
    }

    @Override
    public String toString() {

        if (quantity == 0) {
            return "Slot " + slotID + ": SOLD OUT.";
        }

        return "Slot " + slotID + ": " + product.getName() + ", $" + product.getPrice() + ", " + quantity + " in stock";
    }
}
