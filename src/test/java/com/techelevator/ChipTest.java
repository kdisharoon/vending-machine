package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class ChipTest {
        
        private Chip objectUnderTest;
        private String chipName;
        private BigDecimal chipPrice;

        @Before
        public void setObjectUnderTest() {
            objectUnderTest = new Chip(chipName, chipPrice);
        }

        @Test
        public void chip_sale_message_prints_correctly() {
            String saleMessage = "Crunch Crunch, Yum!";
            String output = objectUnderTest.getSaleMessage();
            Assert.assertEquals("The sale message is supposed to be \"Crunch Crunch, Yum!\"", saleMessage, output);
        }
        
}
