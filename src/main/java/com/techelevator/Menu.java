package com.techelevator;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Menu <T> {
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



    public <T> void printMenu(List<T> listToPrint) {
        for (T t : listToPrint) {
            System.out.println(t);
        }
        System.out.println();
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
            vm.addMoney(roundedAmount);
            System.out.println(nf.format(roundedAmount) + " added to your balance.\n\n");
            return message + " " + nf.format(amount) + " " + nf.format(vm.getCurrentMoneyInMachine());
        } else {
            System.out.println("Invalid amount.");
            return null;
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

        System.out.println(itemName + " costs " + nf.format(itemPrice) + ".\nYou have " + nf.format(currentMoney)
                            + " remaining in the machine." + "\n" + itemSaleMessage + "\n\n");
    }

    public String selectProduct() {
        System.out.println("Here are the currently available products: \n");
        printMenu(inventory);
        System.out.println("Please enter the slot of the product you'd like to purchase (i.e. A2, B4): ");
        String userInput = keyboard.nextLine().toUpperCase();
        String message = null;

        boolean found = false;
        for (Slot s : inventory) {
            if (s.getSlotID().equals(userInput)) {
                BigDecimal price = s.getProduct().getPrice();
                BigDecimal currentMoney = vm.getCurrentMoneyInMachine();
                found = true;

                if (s.getQuantity() > 0) {
                    if (price.compareTo(currentMoney) <= 0) {
                        BigDecimal balanceBeforePurchase = vm.getCurrentMoneyInMachine();
                        dispense(s);
                        message = s.getProduct().getName() + " " + s.getSlotID() + " "
                                + nf.format(balanceBeforePurchase) + " " + nf.format(vm.getCurrentMoneyInMachine());
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

        return message;
    }

    /**
     *
     */
    public void processPurchaseChoice() {

        String purchaseChoice = "";
        String logFileName = "Log.txt";

        try (FileWriter fw = new FileWriter(logFileName, true);
             PrintWriter auditLogWriter = new PrintWriter(fw, true)) {

            do {
                System.out.println("\nWhat would you like to do?");
                printMenu(purchaseMenu);
                System.out.println("Current Money Provided: " + nf.format(vm.getCurrentMoneyInMachine()));
                purchaseChoice = getUserChoice(purchaseMenu);
                String msgForLog;

                if (purchaseChoice.equals("1")) {
                    msgForLog = feedMoney();
                } else if (purchaseChoice.equals("2")) {
                    msgForLog = selectProduct();
                } else {
                    msgForLog = getChange();
                }

                String currentDateTime = getCurrentDateAndTime();

                if (msgForLog != null) {
                    msgForLog = currentDateTime + " " + msgForLog;
                    auditLogWriter.println(msgForLog);
                }

    //            auditLogWriter.flush();

            } while ((purchaseChoice.equals("1")) || (purchaseChoice.equals("2")));

        } catch (IOException e) {
            System.out.println("Unable to find or create " + logFileName + ".");
        }

    }

    public String getChange() {
        BigDecimal initialBalance = vm.getCurrentMoneyInMachine();

        if (initialBalance.equals(BigDecimal.ZERO)) return null;

        BigDecimal quarter = new BigDecimal("0.25");
        BigDecimal dime = new BigDecimal("0.10");
        BigDecimal nickel = new BigDecimal("0.05");
        int numberOfQuarters;
        int numberOfDimes;
        int numberOfNickels;
        String message = "GIVE CHANGE:";

        numberOfQuarters = vm.getCurrentMoneyInMachine().divide(quarter).intValue();
        vm.subtractMoney(quarter.multiply(new BigDecimal(numberOfQuarters)));

        numberOfDimes = vm.getCurrentMoneyInMachine().divide(dime).intValue();
        vm.subtractMoney(dime.multiply(new BigDecimal(numberOfDimes)));

        numberOfNickels = vm.getCurrentMoneyInMachine().divide(nickel).intValue();
        vm.subtractMoney(nickel.multiply(new BigDecimal(numberOfNickels)));

        System.out.println("Your change is " + numberOfQuarters + " quarters, "
                            + numberOfDimes + " dimes, " + numberOfNickels + " nickels");

        return message + " " + nf.format(initialBalance) + " " + nf.format(vm.getCurrentMoneyInMachine());
    }

    public String getCurrentDateAndTime() {
        LocalDateTime now = LocalDateTime.now();

        return now.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + " " +
                now.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM));

    }

    public void run() {
        String userChoice;

        System.out.println("Welcome to the Vendo-Matic 800 from Umbrella Corp!");

        do {
            System.out.println("\nWhat would you like to do?");
            printMenu(initialMenu);
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
