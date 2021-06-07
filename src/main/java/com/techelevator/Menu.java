package com.techelevator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final VendingMachine VM = new VendingMachine();
    private Scanner keyboard = new Scanner(System.in);
    private final List<String> initialMenu = VM.getInitialMenu();
    private final List<String> purchaseMenu = VM.getPurchaseMenu();
    private final List<Slot> inventory = VM.getInventory();
    private final NumberFormat nf = NumberFormat.getCurrencyInstance();
    private final Documenter documenter = new Documenter();

    /**
     *
     * @param relevantMenu A generic list to represent a series of options to the user.
     * @param <T> The type of the List stored in relevantMenu.
     * @return A string corresponding to the numerical choice (between 1 and 4) entered by the user.git com
     */
    public <T> String getUserChoice(List<T> relevantMenu) {
        if (relevantMenu == null) return null;

        String userChoice;
        boolean invalidChoice = false;

        do {
            if (invalidChoice) {
                System.out.println("That's not valid input, try again.");
                printMenu(relevantMenu);
            }
            userChoice = keyboard.nextLine();
            invalidChoice = true;
        }
        //regex ensures the input is an integer. next two OR checks make sure the choice is between 1 and the size of the menu
        while ( (!userChoice.matches("[\\d+]")) || Integer.parseInt(userChoice) <= 0 || Integer.parseInt(userChoice) > relevantMenu.size() );

        return userChoice;
    }

    /**
     *  Takes any List of type T and prints its elements.
     *
     * @param listToPrint
     * @param <T>
     */
    public <T> void printMenu(List<T> listToPrint) {
        if (listToPrint != null) {
            for (T t : listToPrint) {
                if (t instanceof String && t == "") continue;
                System.out.println(t);
            }
            System.out.println();
        }
    }

    public String selectProduct() {
        System.out.println("Here are the currently available products: \n");
        printMenu(inventory);
        System.out.println("Please enter the slot of the product you'd like to purchase (i.e. A2, B4): ");
        String userInput = keyboard.nextLine().toUpperCase();
        String productLogMessage = null;

        boolean found = false;

        //iterates through all the slots in the machine until we find the one the user selected
        for (Slot s : inventory) {

            if (s.getSlotID().equals(userInput)) {
                BigDecimal price = s.getProduct().getPrice();
                BigDecimal currentMoney = VM.getCurrentMoneyInMachine();
                found = true;

                //if the item the user wants isn't sold out, and they have enough money to purchase it
                if (s.getQuantity() > 0) {
                    if (price.compareTo(currentMoney) <= 0) {
                        BigDecimal balanceBeforePurchase = VM.getCurrentMoneyInMachine();

                        //actually dispenses the product in slot s
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

    //method to deal with what the user inputs on the Purchase Choice screen, including checking for invalid inputs
    public void processPurchaseChoice() {

        String purchaseChoice;

        do {
            System.out.println("\nWhat would you like to do?");
            printMenu(purchaseMenu);
            System.out.println("Current Money Provided: " + nf.format(VM.getCurrentMoneyInMachine()));
            purchaseChoice = getUserChoice(purchaseMenu);
            String logMessage;

            if (purchaseChoice != null) {
                if (purchaseChoice.equals("1")) {
                    logMessage = VM.feedMoney();
                } else if (purchaseChoice.equals("2")) {
                    logMessage = selectProduct();
                } else if (purchaseChoice.equals("3")) {
                    logMessage = VM.getChange();
                } else {
                    System.out.println("Invalid choice.");
                    continue;
                }

                documenter.writeLog(logMessage);
            }
        } while ((purchaseChoice.equals("1")) || (purchaseChoice.equals("2")));
    }

    //this serves as the main starting point for the program to get the user's input
    public void run() {
        String userChoice;

        System.out.println("Welcome to the Vendo-Matic 800 from Umbrella Corp!");

        do {
            System.out.println("\nWhat would you like to do?");

            //the initial menu printed out below has 4 values - the fourth one is an empty string to represent the
            //"secret" input of 4 to generate a sales report
            printMenu(initialMenu);
            userChoice = getUserChoice(VM.getInitialMenu());

            if (userChoice != null) {
                if (userChoice.equals("1")) {
                    printMenu(inventory);
                } else if (userChoice.equals("2")) {
                    processPurchaseChoice();
                //below is the "secret" input to generate a sales report and flush the current session's sales tracker
                } else if (userChoice.equals("4")) {
                    System.out.println("Generating Sales Report...");
                    documenter.generateSalesReport(VM.getAndFlushSalesTracker(true), VM.getTotalCurrentSales());
                }
            }
        //we'll continue with this main menu screen until the user selects 3 to quit the program
        } while (!userChoice.equals("3"));
    }

    public static void main(String[] args) {

        Menu vendingMachineMenu = new Menu();
        vendingMachineMenu.run();
    }
}
