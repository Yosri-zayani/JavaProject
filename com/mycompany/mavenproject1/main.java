package com.mycompany.mavenproject1;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVRecord;

public class main {

    public static void main(String[] args) {
        try {
            // Step 1: Use a file chooser to get the file path
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();

                // Step 1: Ingest data
                DataIngestion dataIngestion = new DataIngestion();
                List<CSVRecord> csvData = dataIngestion.readCSV(filePath);

                // Step 2: Preprocess data
                DataPreprocessor preprocessor = new DataPreprocessor();
                List<List<String>> cleanedData = convertCSVToList(csvData);
                cleanedData = preprocessor.handleMissingValues(cleanedData, "0");
                cleanedData = preprocessor.removeOutliers(cleanedData, 3, 1000.0);
                cleanedData = preprocessor.normalizeData(cleanedData, 3);

                // Step 3: Store data in a CSV file
                DataStorage dataStorage = new DataStorage();
                dataStorage.storeInCSV("cleaned_data.csv", cleanedData);

            } else {
                System.out.println("No file selected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Improved convertCSVToList method
    private static List<List<String>> convertCSVToList(List<CSVRecord> csvRecords) {
        List<List<String>> data = new ArrayList<>();
        for (CSVRecord record : csvRecords) {
            List<String> row = new ArrayList<>(record.size()); //Added for efficiency
            for (int i = 0; i < record.size(); i++) {
                row.add(record.get(i));
            }
            data.add(row);
        }
        return data;
    }
}