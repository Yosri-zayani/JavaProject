import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataIngestion {

    // Read data from a CSV file
    public List<CSVRecord> readCSV(String filePath) throws IOException {
        FileReader reader = new FileReader(new File(filePath));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        return csvParser.getRecords();
    }

    // Read data from an Excel file
    public List<List<String>> readExcel(String filePath) throws IOException, InvalidFormatException {
        List<List<String>> data = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(new File(filePath));
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            List<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
                rowData.add(cell.toString());
            }
            data.add(rowData);
        }
        workbook.close();
        return data;
    }

    // Read data from a database
    public List<List<String>> readDatabase(String url, String user, String password, String query) throws Exception {
        List<List<String>> data = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            int columnCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                List<String> rowData = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(resultSet.getString(i));
                }
                data.add(rowData);
            }
        }
        return data;
    }
}