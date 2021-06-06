package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VendingMachineTest {

    private VendingMachine objectUnderTest;

    @Before
    public void setObjectUnderTest() {
        objectUnderTest = new VendingMachine();
    }

    //The below test fails because it's comparing two different objects. Overriding the equals method did not fix it.
    //Still need to figure out how to correctly compare them
    @Test
    public void valid_input_file_produces_inventory_list() {
        //Arrange
        List<Slot> expected = new ArrayList<>();
        String[] testSlotNames = new String[] {"A1", "B2", "C3", "D4"};
        String[] testProductNames = new String[] {"Moldy Cheese", "Brackish Water", "Rubber Pieces", "Dirt Balls"};
        BigDecimal[] testPrices = new BigDecimal[] {new BigDecimal("3.05"), new BigDecimal("1.45"),
                                  new BigDecimal("2.75"), new BigDecimal("8.30")};

        Product testProduct0 = new Chip(testProductNames[0], testPrices[0]);
        Slot testListItem0 = new Slot(testSlotNames[0], testProduct0);
        expected.add(testListItem0);

        Product testProduct1 = new Drink(testProductNames[1], testPrices[1]);
        Slot testListItem1 = new Slot(testSlotNames[1], testProduct1);
        expected.add(testListItem1);

        Product testProduct2 = new Gum(testProductNames[2], testPrices[2]);
        Slot testListItem2 = new Slot(testSlotNames[2], testProduct2);
        expected.add(testListItem2);

        Product testProduct3 = new Candy(testProductNames[3], testPrices[3]);
        Slot testListItem3 = new Slot(testSlotNames[3], testProduct3);
        expected.add(testListItem3);

        //Act
        List<Slot> output = objectUnderTest.stock("vendingmachinetest.csv");

        //Assert
        Assert.assertEquals("The lists are not equal", expected, output);
    }

//    @Test



}
