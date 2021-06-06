package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;

public class GumTest {

    private Gum objectUnderTest;
    private String gumName;
    private BigDecimal gumPrice;

    @Before
    public void setObjectUnderTest() {
        objectUnderTest = new Gum(gumName, gumPrice);
    }

    @Test
    public void gum_sale_message_prints_correctly() {
        String saleMessage = "Chew Chew, Yum!";
        String output = objectUnderTest.getSaleMessage();
        Assert.assertEquals("The sale message is supposed to be \"Chew Chew, Yum!\"", saleMessage, output);
    }

}