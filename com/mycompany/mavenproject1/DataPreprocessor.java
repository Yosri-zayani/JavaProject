import java.util.*;
import java.util.stream.Collectors;

public class DataPreprocessor {

    // Handle missing values by replacing them with a default value
    public List<List<String>> handleMissingValues(List<List<String>> data, String defaultValue) {
        for (List<String> row : data) {
            for (int i = 0; i < row.size(); i++) {
                if (row.get(i) == null || row.get(i).isEmpty()) {
                    row.set(i, defaultValue);
                }
            }
        }
        return data;
    }

    // Remove rows with outliers based on a threshold
    public List<List<String>> removeOutliers(List<List<String>> data, int columnIndex, double threshold) {
        data.removeIf(row -> {
            try {
                double value = Double.parseDouble(row.get(columnIndex));
                return value > threshold;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        return data;
    }

    // Normalize numeric data in a column
    public List<List<String>> normalizeData(List<List<String>> data, int columnIndex) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        // Find min and max values
        for (List<String> row : data) {
            try {
                double value = Double.parseDouble(row.get(columnIndex));
                if (value < min) min = value;
                if (value > max) max = value;
            } catch (NumberFormatException e) {
                // Skip non-numeric values
            }
        }

        // Normalize values
        for (List<String> row : data) {
            try {
                double value = Double.parseDouble(row.get(columnIndex));
                double normalizedValue = (value - min) / (max - min);
                row.set(columnIndex, String.valueOf(normalizedValue));
            } catch (NumberFormatException e) {
                // Skip non-numeric values
            }
        }
        return data;
    }

    // Remove duplicate rows from the dataset
    public List<List<String>> removeDuplicates(List<List<String>> data) {
        Set<List<String>> uniqueRows = new HashSet<>(data);
        return new ArrayList<>(uniqueRows);
    }

    // Encode categorical data using one-hot encoding
    public List<List<String>> oneHotEncode(List<List<String>> data, int columnIndex) {
        // Collect all unique categories in the column
        Set<String> categories = data.stream()
            .map(row -> row.get(columnIndex))
            .collect(Collectors.toSet());

        // Create a map to store the one-hot encoded values
        Map<String, List<String>> encodingMap = new HashMap<>();
        for (String category : categories) {
            List<String> encodedValues = new ArrayList<>(Collections.nCopies(categories.size(), "0"));
            int index = new ArrayList<>(categories).indexOf(category);
            encodedValues.set(index, "1");
            encodingMap.put(category, encodedValues);
        }

        // Replace the categorical column with one-hot encoded columns
        List<List<String>> encodedData = new ArrayList<>();
        for (List<String> row : data) {
            List<String> newRow = new ArrayList<>(row);
            String category = newRow.remove(columnIndex);
            newRow.addAll(columnIndex, encodingMap.get(category));
            encodedData.add(newRow);
        }

        return encodedData;
    }

    // Split data into training and testing sets
    public Map<String, List<List<String>>> trainTestSplit(List<List<String>> data, double trainSize) {
        int trainSizeInt = (int) (data.size() * trainSize);
        List<List<String>> trainData = new ArrayList<>(data.subList(0, trainSizeInt));
        List<List<String>> testData = new ArrayList<>(data.subList(trainSizeInt, data.size()));

        Map<String, List<List<String>>> result = new HashMap<>();
        result.put("train", trainData);
        result.put("test", testData);

        return result;
    }

    // Shuffle the dataset
    public List<List<String>> shuffleData(List<List<String>> data) {
        List<List<String>> shuffledData = new ArrayList<>(data);
        Collections.shuffle(shuffledData);
        return shuffledData;
    }

    // Standardize numeric data in a column (mean = 0, std = 1)
    public List<List<String>> standardizeData(List<List<String>> data, int columnIndex) {
        double sum = 0.0;
        double sumSquared = 0.0;
        int count = 0;

        // Calculate mean and standard deviation
        for (List<String> row : data) {
            try {
                double value = Double.parseDouble(row.get(columnIndex));
                sum += value;
                sumSquared += value * value;
                count++;
            } catch (NumberFormatException e) {
                // Skip non-numeric values
            }
        }

        double mean = sum / count;
        double std = Math.sqrt((sumSquared / count) - (mean * mean));

        // Standardize values
        for (List<String> row : data) {
            try {
                double value = Double.parseDouble(row.get(columnIndex));
                double standardizedValue = (value - mean) / std;
                row.set(columnIndex, String.valueOf(standardizedValue));
            } catch (NumberFormatException e) {
                // Skip non-numeric values
            }
        }
        return data;
    }

    }
