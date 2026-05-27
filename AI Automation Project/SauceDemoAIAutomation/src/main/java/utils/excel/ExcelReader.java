package utils.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelReader {
    private static final String TEST_DATA_PATH = "src/test/resources/TestData.xlsx";
    private static final String TEST_INPUT_DATA_PATH = "src/test/resources/TestInputData.xlsx";

    public static List<Map<String, String>> getSheetData(String sheetName) {
        return getSheetData(sheetName, null);
    }

    public static List<Map<String, String>> getSheetData(String sheetName, String headerColumnName) {
        return getSheetData(TEST_DATA_PATH, sheetName, headerColumnName);
    }

    public static List<Map<String, String>> getInputSheetData(String sheetName) {
        return getSheetData(TEST_INPUT_DATA_PATH, sheetName, null);
    }

    public static List<Map<String, String>> getSheetData(String filePath, String sheetName, String headerColumnName) {
        List<Map<String, String>> rows = new ArrayList<>();
        try {
            try (InputStream inputStream = openWorkbook(filePath)) {
                if (inputStream == null) {
                return rows;
            }
                try (Workbook workbook = WorkbookFactory.create(inputStream)) {
                    Sheet sheet = workbook.getSheet(sheetName);
                    if (sheet == null) {
                        return rows;
                    }
                    Iterator<Row> rowIterator = sheet.rowIterator();
                    Row headerRow = findHeaderRow(rowIterator, headerColumnName);
                    if (headerRow == null) {
                        return rows;
                    }
                    List<String> headers = new ArrayList<>();
                    headerRow.forEach(cell -> headers.add(getCellValue(cell)));
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        Map<String, String> rowData = new LinkedHashMap<>();
                        for (int col = 0; col < headers.size(); col++) {
                            Cell cell = row.getCell(col);
                            rowData.put(headers.get(col), getCellValue(cell));
                        }
                        if (rowData.values().stream().anyMatch(value -> value != null && !value.isBlank())) {
                            rows.add(rowData);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

    private static InputStream openWorkbook(String filePath) throws IOException {
        String fileName = Paths.get(filePath).getFileName().toString();
        Path directPath = Paths.get(filePath);
        if (directPath.toFile().exists()) {
            return new FileInputStream(directPath.toFile());
        }

        Path userDirPath = Paths.get(System.getProperty("user.dir"), filePath);
        if (userDirPath.toFile().exists()) {
            return new FileInputStream(userDirPath.toFile());
        }

        Path upwardPath = findUpwardResource(fileName);
        if (upwardPath != null) {
            return new FileInputStream(upwardPath.toFile());
        }

        String resourceName = fileName;
        InputStream resourceStream = ExcelReader.class.getClassLoader().getResourceAsStream(resourceName);
        if (resourceStream != null) {
            return resourceStream;
        }

        URL resourceUrl = ExcelReader.class.getClassLoader().getResource(resourceName);
        if (resourceUrl != null) {
            try {
                return new FileInputStream(Paths.get(resourceUrl.toURI()).toFile());
            } catch (URISyntaxException ignored) {
                return null;
            }
        }
        return null;
    }

    private static Path findUpwardResource(String fileName) {
        Path current = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        while (current != null) {
            Path candidate = current.resolve(Paths.get("src", "test", "resources", fileName));
            if (candidate.toFile().exists()) {
                return candidate;
            }
            current = current.getParent();
        }
        return null;
    }

    private static Row findHeaderRow(Iterator<Row> rowIterator, String headerColumnName) {
        if (headerColumnName == null || headerColumnName.isBlank()) {
            return rowIterator.hasNext() ? rowIterator.next() : null;
        }
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            for (Cell cell : row) {
                if (headerColumnName.equalsIgnoreCase(getCellValue(cell))) {
                    return row;
                }
            }
        }
        return null;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
