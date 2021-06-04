package com.techelevator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

public class Menu <T> {
    // Instantiate a vending machine
    // Present a series of options
    // According to the
    VendingMachine vm = new VendingMachine();
    Scanner keyboard = new Scanner(System.in);
    List<String> initialMenu = vm.getInitialMenuOptions();
    List<String> purchaseMenu = vm.getPurchaseFlowMenu();
    List<Slot> inventory = vm.getListOfSlots();
    NumberFormat nf = NumberFormat.getCurrencyInstance();
    List<BigDecimal> validAmounts = Arrays.asList(new BigDecimal("1.00"), new BigDecimal("2.00"), new BigDecimal("5.00"),
            new BigDecimal("10.00"), new BigDecimal("20.00"), new BigDecimal("50.00"),
            new BigDecimal("100.00"));

    public <T> String getUserChoice(List<T> relevantMenu) {
        String userChoice = "";

        userChoice = keyboard.nextLine();

        while (! (userChoice.equals("1") || userChoice.equals("2") || userChoice.equals("3")) ) {
            System.out.println("That's not valid input, try again.");
            printMenu(relevantMenu);
            userChoice = keyboard.nextLine();
        }

        return userChoice;
    }

    public void displayInitialMenu() {
        System.out.println("Welcome to the Vendo-Matic 800 from Umbrella Corp!");
        System.out.println("Would you like to:");
        printMenu(initialMenu);
    }

    public <T> void printMenu(List<T> listToPrint) {
        for (T t : listToPrint) {
            System.out.println(t);
        }
        System.out.println();
    }

    public void feedMoney() {
        BigDecimal amount;

        System.out.println("Input amount of money");

        try {
            amount = keyboard.nextBigDecimal();
        }
        catch (InputMismatchException e) {
            amount = BigDecimal.ZERO;
        }

        keyboard.nextLine();

        if (validAmounts.contains(amount)) {
            vm.addMoney(amount);
        } else {
            System.out.println("Invalid amount.");
        }
    }

    public void dispense(Slot s) {
        Product currentProduct = s.getProduct();
        String itemName = currentProduct.getName();
        BigDecimal itemPrice = currentProduct.getPrice();
        vm.subtractMoney(itemPrice);
        BigDecimal currentMoney = vm.getCurrentMoneyInMachine();
        String itemSaleMessage = currentProduct.getSaleMessage();

        s.sellItem();       //reduces quantity in this slot by 1

        System.out.println(itemName + " costs " + itemPrice + ".\nYou have " + currentMoney
                            + " remaining in the machine." + "\n" + itemSaleMessage);
    }

    public void selectProduct() {
        System.out.println("Here are the currently available products: \n");
        printMenu(inventory);
        System.out.println("Please enter the slot of the product you'd like to purchase (i.e. A2, B4): ");
        String userInput = keyboard.nextLine().toUpperCase();

        boolean found = false;
        for (Slot s : inventory) {
            if (s.getSlotID().equals(userInput)) {
                BigDecimal price = s.getProduct().getPrice();
                BigDecimal currentMoney = vm.getCurrentMoneyInMachine();
                found = true;

                if (s.getQuantity() > 0) {
                    if (price.compareTo(currentMoney) <= 0) {
                        dispense(s);
                    } else {
                        System.out.println("You don't have enough money for that product.");
                    }
                }
                else {
                    System.out.println("Sorry, that product is sold out.");
                }

            }

        }

        if (!found) {
            System.out.println("Sorry, that's not a valid slot.\n");
        }

    }

    public void processPurchaseChoice() {

        String purchaseChoice;

        do {
            printMenu(purchaseMenu);
            System.out.println("\nCurrent Money Provided: " + nf.format(vm.getCurrentMoneyInMachine()));
            purchaseChoice = getUserChoice(purchaseMenu);

            if (purchaseChoice.equals("1")) {
                feedMoney();
            }
            else if (purchaseChoice.equals("2")) {
                selectProduct();
            }
            else {
                //finish transaction, receive change in coins, update balance to 0, return to main menu
                getChange();
                vm.subtractMoney(vm.getCurrentMoneyInMachine());
            }

        } while ( (purchaseChoice.equals("1")) || (purchaseChoice.equals("2")) );

    }

    public void getChange() {
        BigDecimal balance = vm.getCurrentMoneyInMachine();
        BigDecimal quarter = new BigDecimal("0.25");
        BigDecimal dime = new BigDecimal("0.10");
        BigDecimal nickel = new BigDecimal("0.05");
        int numberOfQuarters;
        int numberOfDimes;
        int numberOfNickels;

        numberOfQuarters = balance.divide(quarter).intValue();
        balance = balance.remainder(quarter);
        numberOfDimes = balance.divide(dime).intValue();
        balance = balance.remainder(dime);
        numberOfNickels = balance.divide(nickel).intValue();

        System.out.println("Your change is " + numberOfQuarters + " quarters, "
                            + numberOfDimes + " dimes, " + numberOfNickels + " nickels");
    }

    public void run() {
        String userChoice;

        do {
            displayInitialMenu();
            userChoice = getUserChoice(vm.getInitialMenuOptions());

            if (userChoice.equals("1")) {
                printMenu(inventory);
            } else if (userChoice.equals("2")) {
                processPurchaseChoice();
            }

        } while (!userChoice.equals("3"));

    }

    public static void main(String[] args) {

        Menu vendingMachineMenu = new Menu();
        vendingMachineMenu.run();
    }


}
