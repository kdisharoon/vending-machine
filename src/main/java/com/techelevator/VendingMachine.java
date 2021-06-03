package com.techelevator;

import java.math.BigDecimal;

public class VendingMachine {

    private BigDecimal currentMoneyInMachine;

    public VendingMachine() {

    }
    
    public void purchase(Slot slot) {

    }

    public void dispense(Product product) {

    }

    public void addMoney(BigDecimal amountToAdd) {
        currentMoneyInMachine.add(amountToAdd);
    }


}
