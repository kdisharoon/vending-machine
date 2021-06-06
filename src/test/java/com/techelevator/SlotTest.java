package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class SlotTest {

    private Slot objectUnderTest;

    @Before
    public void setObjectUnderTest() {
        BigDecimal testPrice = new BigDecimal("4.00");
        Product testProduct = new Product("Cheese", testPrice);
        objectUnderTest = new Slot("B1", testProduct);
    }

    @Test
    public void sellitem_reduces_quantity_by_1_when_quantity_is_positive() {

        //Arrange - first 2 lines set the initial quantity in the test object and get that quantity
        objectUnderTest.setQuantity(45);
        int inputQuantity = objectUnderTest.getQuantity();

        //Act - next 2 lines run the sellItem method and get the new quantity
        objectUnderTest.sellItem();
        int outputQuantity = objectUnderTest.getQuantity();

        //Assert - check whether the output is equal to what we expected
        Assert.assertEquals("The output quantity should equal 44", 44, outputQuantity);

    }

    @Test
    public void sellitem_does_not_reduce_quantity_when_quantity_is_zero() {

        //Arrange - first 2 lines set the initial quantity in the test object and get that quantity
        objectUnderTest.setQuantity(0);
        int inputQuantity = objectUnderTest.getQuantity();

        //Act - next 2 lines run the sellItem method and get the new quantity, which should still be zero
        objectUnderTest.sellItem();
        int outputQuantity = objectUnderTest.getQuantity();

        //Assert - check whether the output is equal to what we expected
        Assert.assertEquals("The output quantity should equal 0", 0, outputQuantity);

    }

    @Test
    public void toString_returns_SOLD_OUT_message_when_quantity_is_zero() {

        //Arrange - set quantity to zero
        objectUnderTest.setQuantity(0);

        //Act - next 2 lines set the expected output and run the toString method to get the actual output
        String expected = "Slot B1: SOLD OUT.";
        String output = objectUnderTest.toString();

        //Assert - check whether the output is equal to what we expected
        Assert.assertEquals("The sale message should say \"Slot B1: SOLD OUT.\"", expected, output);

    }

    @Test
    public void toString_returns_correct_message_when_quantity_is_greater_than_zero() {

        //Arrange - set quantity to 45
        objectUnderTest.setQuantity(45);

        //Act - next 2 lines run the sellItem method and get the new quantity, which should still be zero
        String expected = "Slot B1: Cheese, $4.00, 45 in stock";
        String output = objectUnderTest.toString();

        //Assert - check whether the output is equal to what we expected
        Assert.assertEquals("The sale message should say \"Slot B1: Cheese, $4.00, 45 in stock.\"", expected, output);

    }


}
