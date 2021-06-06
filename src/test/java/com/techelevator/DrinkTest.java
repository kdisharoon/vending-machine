package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;

public class DrinkTest {

    private Drink objectUnderTest;
    private String drinkName;
    private BigDecimal drinkPrice;

    @Before
    public void setObjectUnderTest() {
        objectUnderTest = new Drink(drinkName, drinkPrice);
    }

    @Test
    public void drink_sale_message_prints_correctly() {
        String saleMessage = "Glug Glug, Yum!";
        String output = objectUnderTest.getSaleMessage();
        Assert.assertEquals("The sale message is supposed to be \"Glug Glug, Yum!\"", saleMessage, output);
    }

}
