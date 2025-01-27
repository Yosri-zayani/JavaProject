import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataImporter {

    // Import data from a CSV file
    public static List<String[]> importFromCSV(String filePath) throws IOException, CsvValidationException {
        List<String[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
            }
        }
        return data;
    }

    // Import data from an Excel file
    public static List<String[]> importFromExcel(String filePath) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            rowData.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                        default:
                            rowData.add("");
                    }
                }
                data.add(rowData.toArray(new String[0]));
            }
        }
        return data;
    }

    // Import data from a database
    public static List<String[]> importFromDatabase(String url, String username, String password, String query) throws SQLException {
        List<String[]> data = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getString(i);
                }
                data.add(row);
            }
        }
        return data;
    }
}