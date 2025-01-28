package com.mycompany.mavenproject1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class DataStorage {

    /**
     * Stores the cleaned data into a CSV file.
     *
     * @param filePath The path to the output CSV file.
     * @param data The cleaned data to be stored.
     */
    public void storeInCSV(String filePath, List<List<String>> data) {
        try (FileWriter writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            // Write each row of data to the CSV file
            for (List<String> row : data) {
                csvPrinter.printRecord(row);
            }
            csvPrinter.flush();
            System.out.println("Data successfully written to CSV file: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   
}