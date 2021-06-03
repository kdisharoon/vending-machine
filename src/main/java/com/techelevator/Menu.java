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

    public void processPurchaseChoice() {
        List<BigDecimal> validAmounts = Arrays.asList(new BigDecimal[] {
                new BigDecimal(1.00), new BigDecimal(2.00), new BigDecimal(5.00),
                new BigDecimal(10.00), new BigDecimal(20.00), new BigDecimal(50.00),
                new BigDecimal(100.00)});
        String purchaseChoice;

        do {
            printMenu(purchaseMenu);
            System.out.println("\nCurrent Money Provided: " + nf.format(vm.getCurrentMoneyInMachine()));
            purchaseChoice = getUserChoice(purchaseMenu);
            BigDecimal amount;

            System.out.println("Input amount of money");

            amount = keyboard.nextBigDecimal();
            keyboard.nextLine();

            if (validAmounts.contains(amount)) {
                vm.addMoney(amount);
            } else {
                System.out.println("Invalid amount.");
            }
        } while (purchaseChoice.equals("1"));

        if (purchaseChoice.equals("2")) {
            System.out.println("select product");
        } else if (purchaseChoice.equals("3")) {
            System.out.println("finish transaction");
        }
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
