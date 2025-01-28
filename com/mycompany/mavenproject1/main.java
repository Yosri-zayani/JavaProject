import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVRecord;

public class MainApplication {
    public static void main(String[] args) {
        try {
            // Step 1: Ingest data
            
            DataIngestion dataIngestion = new DataIngestion();
            String filePath = "C:\\Users\\Zayen\\Downloads\\testjava - Sheet1.csv"; // Full path to your CSV file
            List<CSVRecord> csvData = dataIngestion.readCSV(filePath);

            // Step 2: Preprocess data
            DataPreprocessor preprocessor = new DataPreprocessor();
            List<List<String>> cleanedData = convertCSVToList(csvData); // Convert CSVRecord to List<List<String>>
            cleanedData = preprocessor.handleMissingValues(cleanedData, "0"); // Handle missing values
            cleanedData = preprocessor.removeOutliers(cleanedData, 4, 1000.0); // Remove outliers in column 4
            cleanedData = preprocessor.normalizeData(cleanedData, 4); // Normalize column 4

            // Step 3: Store data in a CSV file
            DataStorage dataStorage = new DataStorage();
            dataStorage.storeInCSV("cleaned_data.csv", cleanedData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to convert CSVRecord to List<List<String>>
    private static List<List<String>> convertCSVToList(List<CSVRecord> csvRecords) {
        List<List<String>> data = new ArrayList<>();
        for (CSVRecord record : csvRecords) {
            List<String> row = new ArrayList<>();
            for (String value : record) {
                row.add(value);
            }
            data.add(row);
        }
        return data;
    }
}