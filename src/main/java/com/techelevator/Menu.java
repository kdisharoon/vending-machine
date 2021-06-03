package com.techelevator;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Menu {
    // Instantiate a vending machine
    // Present a series of options
    // According to the
    private List<String> initialOptions;
    VendingMachine vm = new VendingMachine();
    Scanner keyboard = new Scanner(System.in);

    public Menu() {

    }

    public void displayInitialMenu() {
        System.out.println("Welcome to the Vendo-Matic 800 from Umbrella Corp!");
        System.out.println("Would you like to:");
        List<String> menuOptions = vm.getMenuOptions();
        for (String s : menuOptions) {
            System.out.println(s);
        }
    }

    public void displayInventory() {
        List<Slot> slots = vm.getListOfSlots();
        for (Slot s : slots) {
            System.out.println(s);
        }
    }

    public String getUserChoice() {
        String userChoice = "";

        userChoice = keyboard.nextLine();

        if (! (userChoice.equals("1") || userChoice.equals("2") || userChoice.equals("3")) ) {
            System.out.println("That's not valid input, try again.");
        }

        return userChoice;
    }

    public static void main(String[] args) {

        Menu vendingMachineMenu = new Menu();
        String userChoice;

        do {
            vendingMachineMenu.displayInitialMenu();
            userChoice = vendingMachineMenu.getUserChoice();

            if (userChoice.equals("1")) {
                vendingMachineMenu.displayInventory();
            }

        } while (!userChoice.equals("3"));





    }


}
