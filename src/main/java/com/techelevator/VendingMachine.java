package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;

public class VendingMachine {
    private final String[] INITIAL_MENU_OPTIONS = {"(1) Display Vending Machine Items", "(2) Purchase", "(3) Exit", ""};
    private final String[] PURCHASE_MENU_OPTIONS = {"(1) Feed Money", "(2) Select Product", "(3) Finish Transaction"};
    private BigDecimal currentMoneyInMachine = BigDecimal.ZERO;
    private BigDecimal totalCurrentSales = BigDecimal.ZERO;
    private final List<String> INITIAL_MENU = new ArrayList<>(Arrays.asList(INITIAL_MENU_OPTIONS));
    private final List<String> PURCHASE_MENU = new ArrayList<>(Arrays.asList(PURCHASE_MENU_OPTIONS));
    private final List<Slot> inventory;
    private final NumberFormat nf = NumberFormat.getCurrencyInstance();
    private final Scanner keyboard = new Scanner(System.in);
    private final List<BigDecimal> VALID_MONEY_AMOUNTS = Arrays.asList(new BigDecimal("1.00"), new BigDecimal("2.00"),
            new BigDecimal("5.00"), new BigDecimal("10.00"), new BigDecimal("20.00"), new BigDecimal("50.00"),
            new BigDecimal("100.00"));
    private final Map<String, Integer> salesTracker = new HashMap<>();

    public VendingMachine() {
        inventory = stock("vendingmachine.csv");
    }

    public List<Slot> getInventory() {
        return inventory;
    }

    public List<String> getInitialMenu() {
        return INITIAL_MENU;
    }

    public List<String> getPurchaseMenu() {
        return PURCHASE_MENU;
    }

    //method to track all-time sales on the machine - will flush the input after writing to the log file
    protected Map<String, Integer> getAndFlushSalesTracker(boolean flush) {
        Map<String, Integer> salesTallyToReturn = new HashMap<>();

        //iterates through the list of products sold during this session and adds to the list to return
        for (Map.Entry<String, Integer> entry: salesTracker.entrySet()) {
            salesTallyToReturn.put(entry.getKey(), entry.getValue());
        }

        //if we should flush the sales tracker, do so
        if (flush) {
            for (Map.Entry<String, Integer> entry : salesTracker.entrySet()) {
                salesTracker.put(entry.getKey(), 0);
            }
        }

        return salesTallyToReturn;
    }

    // Stock the machine as soon as a VendingMachine object is instantiated
    protected List<Slot> stock(String path) {

        List<Slot> listOfSlots = new ArrayList<>();

        try (Scanner stockFile = new Scanner(new File(path))) {

            //reads through every line in the vendingmachine.csv file and parses it into an array
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
                        break;
                }

                Slot newSlotItem = new Slot(slotName, toAdd);

                //after parsing, adds the slot item (slotID, product name and price) to the list of current machine contents)
                listOfSlots.add(newSlotItem);

                //if the sales tracker doesn't already have this item in it, put it in and set initial quantity sold to 0
                if (!salesTracker.containsKey(newSlotItem.getProduct().getName())) {
                    salesTracker.put(newSlotItem.getProduct().getName(), 0);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Oh no, we can't find that file.");
        }

        return listOfSlots;
    }


    protected BigDecimal addMoney(BigDecimal amountToAdd) {
        currentMoneyInMachine = currentMoneyInMachine.add(amountToAdd);
        return currentMoneyInMachine;
    }

    protected BigDecimal subtractMoney(BigDecimal amountToSubtract) {
        currentMoneyInMachine = currentMoneyInMachine.subtract(amountToSubtract);
        return currentMoneyInMachine;
    }

    protected String feedMoney() {
        BigDecimal amount;
        String message = "FEED MONEY:";

        System.out.println("Input amount of money:");

        try {
            amount = keyboard.nextBigDecimal();
        }
        //catches inputs that aren't numerical
        catch (InputMismatchException e) {
            amount = BigDecimal.ZERO;
        }

        keyboard.nextLine();
        //rounds the amount down so that 5.00000000000000001 will be accepted as 5.00. This was causing exceptions
        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.FLOOR);

        //VALID_MONEY_AMOUNTS verifies that nonexistent currency like a $3 bill won't be accepted
        if (VALID_MONEY_AMOUNTS.contains(roundedAmount)) {
            addMoney(roundedAmount);
            System.out.println(nf.format(roundedAmount) + " added to your balance.\n\n");
            return message + " " + nf.format(amount) + " " + nf.format(getCurrentMoneyInMachine());
        } else {
            System.out.println("Invalid amount.");
            return null;
        }

    }

    protected String dispense(Slot s) {
        Product currentProduct = s.getProduct();
        String itemName = currentProduct.getName();
        BigDecimal itemPrice = currentProduct.getPrice();

        //make sure to subtract money from current balance before actually dispensing product
        subtractMoney(itemPrice);
        BigDecimal currentMoney = getCurrentMoneyInMachine();
        String itemSaleMessage = currentProduct.getSaleMessage();

        //reduces quantity in this slot by 1
        s.sellItem();

        //next two lines increment the current session sales tracker by 1 for the product we just sold
        int numCurrentProductSold = salesTracker.get(itemName);
        salesTracker.put(itemName, numCurrentProductSold + 1);

        //adds the amount just spent to the machine's total sales
        totalCurrentSales = totalCurrentSales.add(currentProduct.getPrice());

        return itemName + " costs " + nf.format(itemPrice) + ".\nYou have " + nf.format(currentMoney)
                + " remaining in the machine." + "\n" + itemSaleMessage + "\n\n";
    }

    protected BigDecimal getTotalCurrentSales() {
        return totalCurrentSales;
    }

    //getChange method calculates how many quarters, dimes, nickels are required to give change with the fewest coins
    protected String getChange() {
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

    protected BigDecimal getCurrentMoneyInMachine() {
        return currentMoneyInMachine;
    }

}