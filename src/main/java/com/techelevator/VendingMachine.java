package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;

public class VendingMachine {
    private final String[] initialMenuOptions = {"(1) Display Vending Machine Items", "(2) Purchase", "(3) Exit"};
    private final String[] purchaseMenuOptions = {"(1) Feed Money", "(2) Select Product", "(3) Finish Transaction"};
    private BigDecimal currentMoneyInMachine = BigDecimal.ZERO;
    private BigDecimal totalCurrentSales = BigDecimal.ZERO;
    private final List<Slot> listOfSlots = new ArrayList<>();
    private final List<String> INITIAL_MENU = new ArrayList<>(Arrays.asList(initialMenuOptions));
    private final List<String> purchaseMenu = new ArrayList<>(Arrays.asList(purchaseMenuOptions));
    private final NumberFormat nf = NumberFormat.getCurrencyInstance();
    private final Scanner keyboard = new Scanner(System.in);
    private final List<BigDecimal> validAmounts = Arrays.asList(new BigDecimal("1.00"), new BigDecimal("2.00"), new BigDecimal("5.00"),
            new BigDecimal("10.00"), new BigDecimal("20.00"), new BigDecimal("50.00"),
            new BigDecimal("100.00"));
    private final Map<String, Integer> salesTracker = new HashMap<>();

    public VendingMachine() {
        stock();
        fillSalesTracker();
    }

    public void fillSalesTracker() {
        for (Slot slot: listOfSlots) {
            salesTracker.put(slot.getProduct().getName(), 0);
        }
    }

    public List<Slot> getListOfSlots() {
        return listOfSlots;
    }

    public List<String> getInitialMenu() {
        return INITIAL_MENU;
    }

    public List<String> getPurchaseMenu() {
        return purchaseMenu;
    }

    public Map<String, Integer> getSalesTracker() {
        return salesTracker;
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

                Slot newSlotItem = new Slot(slotName, toAdd);
                listOfSlots.add(newSlotItem);

                if (!salesTracker.containsKey(newSlotItem.getProduct().getName())) {
                    salesTracker.put(newSlotItem.getProduct().getName(), 0);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Oh no, we can't find that file.");
        }
    }

    public void addMoney(BigDecimal amountToAdd) {
        currentMoneyInMachine = currentMoneyInMachine.add(amountToAdd);
    }

    public void subtractMoney(BigDecimal amountToSubtract) {
        currentMoneyInMachine = currentMoneyInMachine.subtract(amountToSubtract);
    }

    public String feedMoney() {
        BigDecimal amount;
        String message = "FEED MONEY:";

        System.out.println("Input amount of money:");

        try {
            amount = keyboard.nextBigDecimal();
        }
        catch (InputMismatchException e) {
            amount = BigDecimal.ZERO;
        }

        keyboard.nextLine();
        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.FLOOR);

        if (validAmounts.contains(roundedAmount)) {
            addMoney(roundedAmount);
            System.out.println(nf.format(roundedAmount) + " added to your balance.\n\n");
            return message + " " + nf.format(amount) + " " + nf.format(getCurrentMoneyInMachine());
        } else {
            System.out.println("Invalid amount.");
            return null;
        }

    }

    public String dispense(Slot s) {
        Product currentProduct = s.getProduct();
        String itemName = currentProduct.getName();
        BigDecimal itemPrice = currentProduct.getPrice();
        subtractMoney(itemPrice);
        BigDecimal currentMoney = getCurrentMoneyInMachine();
        String itemSaleMessage = currentProduct.getSaleMessage();

        s.sellItem();       //reduces quantity in this slot by 1
        int numCurrentProductSold = salesTracker.get(itemName);
        salesTracker.put(itemName, numCurrentProductSold + 1);
        //?
        totalCurrentSales = totalCurrentSales.add(currentProduct.getPrice());

        return itemName + " costs " + nf.format(itemPrice) + ".\nYou have " + nf.format(currentMoney)
                + " remaining in the machine." + "\n" + itemSaleMessage + "\n\n";
    }

    public BigDecimal getTotalCurrentSales() {
        return totalCurrentSales;
    }

    public String getChange() {
        BigDecimal initialBalance = getCurrentMoneyInMachine();

        if (initialBalance.equals(BigDecimal.ZERO)) return null;

        BigDecimal quarter = new BigDecimal("0.25");
        BigDecimal dime = new BigDecimal("0.10");
        BigDecimal nickel = new BigDecimal("0.05");
        int numberOfQuarters;
        int numberOfDimes;
        int numberOfNickels;
        String message = "GIVE CHANGE:";

        numberOfQuarters = getCurrentMoneyInMachine().divide(quarter).intValue();
        subtractMoney(quarter.multiply(new BigDecimal(numberOfQuarters)));

        numberOfDimes = getCurrentMoneyInMachine().divide(dime).intValue();
        subtractMoney(dime.multiply(new BigDecimal(numberOfDimes)));

        numberOfNickels = getCurrentMoneyInMachine().divide(nickel).intValue();
        subtractMoney(nickel.multiply(new BigDecimal(numberOfNickels)));

        System.out.println("Your change is " + numberOfQuarters + " quarters, "
                + numberOfDimes + " dimes, " + numberOfNickels + " nickels");

        return message + " " + nf.format(initialBalance) + " " + nf.format(getCurrentMoneyInMachine());
    }

    public BigDecimal getCurrentMoneyInMachine() {
        return currentMoneyInMachine;
    }

    public static void main(String[] args) {

    }
}
