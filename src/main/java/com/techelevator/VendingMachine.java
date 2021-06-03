package com.techelevator;

import java.math.BigDecimal;
import java.util.Set;
import java.util.HashSet;

public class VendingMachine {
    private BigDecimal currentMoneyInMachine;
    private Set<Slot> listOfSlots = new HashSet<>();

    // Stock the machine

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
