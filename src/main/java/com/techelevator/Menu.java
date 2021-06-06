package com.techelevator;

import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final VendingMachine VM = new VendingMachine();
    private final Scanner keyboard = new Scanner(System.in);
    private final List<String> initialMenu = VM.getInitialMenu();
    private final List<String> purchaseMenu = VM.getPurchaseMenu();
    private final List<Slot> inventory = VM.getListOfSlots();
    private final NumberFormat nf = NumberFormat.getCurrencyInstance();

    public <T> String getUserChoice(List<T> relevantMenu) {
        String userChoice;

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

    public String selectProduct() {
        System.out.println("Here are the currently available products: \n");
        printMenu(inventory);
        System.out.println("Please enter the slot of the product you'd like to purchase (i.e. A2, B4): ");
        String userInput = keyboard.nextLine().toUpperCase();
        String productLogMessage = null;

        boolean found = false;
        for (Slot s : inventory) {
            if (s.getSlotID().equals(userInput)) {
                BigDecimal price = s.getProduct().getPrice();
                BigDecimal currentMoney = VM.getCurrentMoneyInMachine();
                found = true;

                if (s.getQuantity() > 0) {
                    if (price.compareTo(currentMoney) <= 0) {
                        BigDecimal balanceBeforePurchase = VM.getCurrentMoneyInMachine();
                        String dispenseMsg = VM.dispense(s);

                        System.out.println(dispenseMsg);

                        productLogMessage = s.getProduct().getName() + " " + s.getSlotID() + " "
                                + nf.format(balanceBeforePurchase) + " " + nf.format(VM.getCurrentMoneyInMachine());
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

        return productLogMessage;
    }

    /**
     *
     */
    public void processPurchaseChoice() {

        String purchaseChoice;
        String logFileName = "Log.txt";

        try (FileWriter fw = new FileWriter(logFileName, true);
             PrintWriter auditLogWriter = new PrintWriter(fw, true)) {

            do {
                System.out.println("\nWhat would you like to do?");
                printMenu(purchaseMenu);
                System.out.println("Current Money Provided: " + nf.format(VM.getCurrentMoneyInMachine()));
                purchaseChoice = getUserChoice(purchaseMenu);
                String msgForLog;

                if (purchaseChoice.equals("1")) {
                    msgForLog = VM.feedMoney();
                } else if (purchaseChoice.equals("2")) {
                    msgForLog = selectProduct();
                } else {
                    msgForLog = VM.getChange();
                }

                String currentDateTime = getCurrentDateAndTime();

                if (msgForLog != null) {
                    msgForLog = currentDateTime + " " + msgForLog;
                    auditLogWriter.println(msgForLog);
                }
            } while ((purchaseChoice.equals("1")) || (purchaseChoice.equals("2")));

        } catch (IOException e) {
            System.out.println("Unable to find or create " + logFileName + ".");
        }

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
            userChoice = getUserChoice(VM.getInitialMenu());

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
