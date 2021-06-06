package com.techelevator;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuTest {
    Menu menuTester;

    @Before
    public void setup() {
        menuTester = new Menu();

    }

    @Test
    public void getUserChoice_returns_null_for_null_input() {
        String expected = null;
        String actual = menuTester.getUserChoice(null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void printMenu_does_not_throw_exception_with_null_input() {
        menuTester.printMenu(null);
    }

    // https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
    // https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
    @Test
    public void getUserChoice_does_not_allow_nonnumeric_user_input() {

    }
}
