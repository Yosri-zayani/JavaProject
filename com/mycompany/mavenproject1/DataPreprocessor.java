import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataPreprocessor {

    public static void main(String[] args) {
        String inputFilePath = "data/input.csv";
        String outputFilePath = "data/output.csv";

        try {
            // Step 1: Read the CSV file
            List<String[]> data = readCSV(inputFilePath);

            // Step 2: Preprocess the data
            List<String[]> preprocessedData = preprocessData(data);

            // Step 3: Write the preprocessed data to a new CSV file
            writeCSV(outputFilePath, preprocessedData);

            System.out.println("Data preprocessing completed successfully!");

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private static List<String[]> readCSV(String filePath) throws IOException, CsvValidationException {
        List<String[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
            }
        }
        return data;
    }

    private static List<String[]> preprocessData(List<String[]> data) {
        List<String[]> preprocessedData = new ArrayList<>();

        // Assuming the first row is the header
        preprocessedData.add(data.get(0));

        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);

            // Example preprocessing steps:
            // 1. Handle missing values (replace with a default value)
            for (int j = 0; j < row.length; j++) {
                if (row[j] == null || row[j].isEmpty()) {
                    row[j] = "0"; // Replace missing values with "0"
                }
            }

            // 2. Normalize numerical data (example: divide by 100)
            try {
                double value = Double.parseDouble(row[1]); // Assuming the second column is numerical
                row[1] = String.valueOf(value / 100.0);
            } catch (NumberFormatException e) {
                // Handle the case where the value is not a number
                System.err.println("Non-numeric value found in numerical column: " + row[1]);
            }

            // 3. Encode categorical variables (example: one-hot encoding)
            // This is a simplified example; in practice, you'd need a more robust solution
            if ("CategoryA".equals(row[2])) {
                row[2] = "1,0,0";
            } else if ("CategoryB".equals(row[2])) {
                row[2] = "0,1,0";
            } else if ("CategoryC".equals(row[2])) {
                row[2] = "0,0,1";
            }

            preprocessedData.add(row);
        }

        return preprocessedData;
    }

    private static void writeCSV(String filePath, List<String[]> data) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeAll(data);
        }
    }
}