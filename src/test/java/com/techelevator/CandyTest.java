package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;

public class CandyTest {

    private Candy objectUnderTest;
    private String candyName;
    private BigDecimal candyPrice;

    @Before
    public void setObjectUnderTest() {
        objectUnderTest = new Candy(candyName, candyPrice);
    }

    @Test
    public void candy_sale_message_prints_correctly() {
        String saleMessage = "Munch Munch, Yum!";
        String output = objectUnderTest.getSaleMessage();
        Assert.assertEquals("The sale message is supposed to be \"Munch Munch, Yum!\"", saleMessage, output);
    }

}
