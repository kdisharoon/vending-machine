package com.techelevator;

import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Documenter {
    private final NumberFormat nf = NumberFormat.getCurrencyInstance();
    private BigDecimal totalAllTimeSales = BigDecimal.ZERO;

    /**
     * Writes provided contents to log file.
     *
     * @param logMessage - Message to write to Log.txt. Provided by Menu.java.
     */
    protected void writeLog(String logMessage) {
        String logFileName = "Log.txt";

        try (FileWriter fw = new FileWriter(logFileName, true);
             PrintWriter auditLogWriter = new PrintWriter(fw, true)) {

            if (logMessage != null) {
                String currentDateTime = getCurrentDateAndTime();
                logMessage = currentDateTime + " " + logMessage;
                auditLogWriter.println(logMessage);
            }

        } catch (IOException e) {
            System.out.println("Error: Could not write to that file.");
        }
    }

    /**
     * Method publicly available to accept Map of current sales and total amount of current sales
     * as a BigDecimal. Calls various helper methods, ultimately writing to a file.
     *
     * @param currentSales A map with item names as keys and number sold since instantiation.
     * @param totalCurrentSales Total amount of sales since instantiation.
     */
    protected void generateSalesReport(Map<String, Integer> currentSales, BigDecimal totalCurrentSales) {
        Map<String, Integer> cumulativeSales = getCumulativeSales();

        if (cumulativeSales != null) {
            for (Map.Entry<String, Integer> entry : cumulativeSales.entrySet()) {
                String key = entry.getKey();
                Integer val = entry.getValue();
                Integer toAdd = currentSales.get(key);
                Integer newValue = val + toAdd;

                currentSales.put(key, newValue);
            }
        }

        this.totalAllTimeSales = this.totalAllTimeSales.add(totalCurrentSales);
        writeToSalesFile(currentSales);
    }

    /**
     * Calls method to get most recently updated sales report file. If found, iterates
     * through that file and populates Map<String, Integer> cumulativeSales with
     * product names and number of items sold. If not found, returns null.
     *
     * @return Map<String, Integer> cumulativeSales - A Map that contains product names
     * and sales numbers, or null if call to getMostRecentSalesFile() returns null.
     */
    private Map<String, Integer> getCumulativeSales() {
        File salesFile = getMostRecentSalesFile();
        Map<String, Integer> cumulativeSales = new HashMap<>();

        if (salesFile != null && salesFile.exists()) {
            try (Scanner salesFileStream = new Scanner(salesFile)) {
                while (salesFileStream.hasNext()) {
                    String line = salesFileStream.nextLine();

                    if (line.contains("|")) {
                        String[] keyAndVal = line.split("\\|");
                        cumulativeSales.put(keyAndVal[0], Integer.parseInt(keyAndVal[1]));
                    } else if (line.contains("$")) {
                        String[] totalCumulativeSales = line.split("\\$");
                        this.totalAllTimeSales = new BigDecimal(totalCumulativeSales[1]);
                    }
                }

                return cumulativeSales;

            } catch (FileNotFoundException e) {
                System.out.println("Error: could not find that file.");
            }
        }

        return null;
    }

    /**
     * Helper method to get the most recently edited file that starts with "salesreport"
     * in current directory.
     *
     * @return File object representing the most recent salesreport file, or null if not found.
     */
    private File getMostRecentSalesFile() {
        File currentDirectory = new File(".");
        File[] files = currentDirectory.listFiles();
        File mostRecent = null;
        int fileCount = 0;

        for (File file: files) {
            if (file.getName().contains("totalsales")) {
                if (fileCount == 0) {
                    mostRecent = file;
                } else if (mostRecent != null
                        && file.lastModified() > mostRecent.lastModified()) {
                        mostRecent = file;
                }

                fileCount++;
            }
        }

        return mostRecent;
    }

    /**
     * Takes a Map of sales and writes them to a file in pipe delimited fashion.
     * File's title pattern is prefix of "salesreport" with suffix indicating
     * date and time that file was generated.
     *
     * @param salesToWrite Map of product names and sales
     */
    private void writeToSalesFile(Map<String, Integer> salesToWrite) {
        String fileBase = "totalsales ";
        String ext = ".txt";
        String fileDateAndTime = getCurrentDateAndTime().replace("/", "-");
        String todayFile = fileBase + fileDateAndTime + ext;

        try (FileWriter todaysReportFW = new FileWriter(todayFile);
             PrintWriter todaysReportPW = new PrintWriter(todaysReportFW)) {

            for (String item: salesToWrite.keySet()) {
                todaysReportPW.println(item + "|" + salesToWrite.get(item));
            }

            todaysReportPW.println("\n**TOTAL SALES**\n  " + nf.format(totalAllTimeSales));
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Error: Unable to write to that file.");
        }
    }

    /**
     *
     * @return String representing current date and time, formatted.
     */
    private String getCurrentDateAndTime() {
        LocalDateTime now = LocalDateTime.now();

        return now.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + " " +
                now.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM));
    }

}