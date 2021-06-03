package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

public class VendingMachine {
    private final String OPTION1 = "(1) Display Vending Machine Items";
    private final String OPTION2 = "(2) Purchase";
    private final String OPTION3 = "(3) Exit";
    private final String[] initialMenuOptions = {OPTION1, OPTION2, OPTION3};
    private final String[] purchaseFlowMenuOptions = {"(1) Feed Money", "(2) Select Product", "(3) Finish Transaction"};
    private BigDecimal currentMoneyInMachine = BigDecimal.ZERO;
    private List<Slot> listOfSlots = new ArrayList<>();
    private List<String> menuOptions = new ArrayList<>(Arrays.asList(initialMenuOptions));
    private List<String> purchaseFlowMenu = new ArrayList<>(Arrays.asList(purchaseFlowMenuOptions));

    public VendingMachine() {
        stock();
    }

    public List<Slot> getListOfSlots() {
        return listOfSlots;
    }

    public List<String> getInitialMenuOptions() {
        return menuOptions;
    }

    public List<String> getPurchaseFlowMenu() {
        return purchaseFlowMenu;
    }

    // Stock the machine
    public void stock() {
        String filename = "vendingmachine.csv";

        try (Scanner stockFile = new Scanner(new File(filename))) {

            while (stockFile.hasNext()) {
                String line = stockFile.nextLine();
                String[] lineElements = line.split("\\|");

                String slotName = lineElements[0];
                String itemName = lineElements[1];
                BigDecimal price = new BigDecimal(lineElements[2]);
                String type = lineElements[3].toLowerCase(Locale.ROOT);
                Product toAdd = null;

                switch (type) {
                    case "chip":
                        toAdd = new Chip(itemName, price);
                        break;
                    case "candy":
                        toAdd = new Candy(itemName, price);
                        break;
                    case "gum":
                        toAdd = new Gum(itemName, price);
                        break;
                    case "drink":
                        toAdd = new Drink(itemName, price);
                }

                listOfSlots.add(new Slot(slotName, toAdd));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Oh no, we can't find that file.");
        }
    }


    public void purchase(Slot slot) {

    }

    public void dispense(Product product) {

    }

    public void addMoney(BigDecimal amountToAdd) {
        currentMoneyInMachine = currentMoneyInMachine.add(amountToAdd);
    }

    public BigDecimal getCurrentMoneyInMachine() {
        return currentMoneyInMachine;
    }
}
