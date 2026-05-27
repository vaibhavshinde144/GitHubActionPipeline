package org.aiautomationproject.qa.utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ExcelTestDataReader {
    private ExcelTestDataReader() {
    }

    public static Map<String, String> getTestCaseById(Path workbookPath, String testCaseId) {
        DataFormatter formatter = new DataFormatter();
        try (InputStream inputStream = Files.newInputStream(workbookPath);
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row header = sheet.getRow(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                String currentId = formatter.formatCellValue(row.getCell(0));
                if (testCaseId.equalsIgnoreCase(currentId)) {
                    Map<String, String> values = new LinkedHashMap<>();
                    for (int col = 0; col < header.getLastCellNum(); col++) {
                        values.put(formatter.formatCellValue(header.getCell(col)), formatter.formatCellValue(row.getCell(col)));
                    }
                    return values;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read workbook: " + workbookPath, e);
        }
        throw new IllegalArgumentException("Test case id not found in workbook: " + testCaseId);
    }

    public static Map<String, String> getRowByKeyFromResource(String resourceName, String sheetName, String keyColumn, String keyValue) {
        DataFormatter formatter = new DataFormatter();
        try (InputStream inputStream = ExcelTestDataReader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IllegalStateException("Workbook was not found in resources: " + resourceName);
            }
            try (Workbook workbook = new XSSFWorkbook(inputStream)) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new IllegalArgumentException("Sheet not found: " + sheetName);
                }
                Row header = sheet.getRow(0);
                int keyIndex = findColumnIndex(header, keyColumn, formatter);
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    if (keyValue.equalsIgnoreCase(formatter.formatCellValue(row.getCell(keyIndex)))) {
                        Map<String, String> values = new LinkedHashMap<>();
                        for (int col = 0; col < header.getLastCellNum(); col++) {
                            values.put(formatter.formatCellValue(header.getCell(col)), formatter.formatCellValue(row.getCell(col)));
                        }
                        return values;
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read workbook resource: " + resourceName, e);
        }
        throw new IllegalArgumentException("Key not found in workbook: " + keyValue);
    }

    public static String getValueFromResource(String resourceName, String sheetName, String key) {
        return getRowByKeyFromResource(resourceName, sheetName, "Key", key).getOrDefault("Value", "");
    }

    private static int findColumnIndex(Row header, String columnName, DataFormatter formatter) {
        for (int col = 0; col < header.getLastCellNum(); col++) {
            if (columnName.equalsIgnoreCase(formatter.formatCellValue(header.getCell(col)))) {
                return col;
            }
        }
        throw new IllegalArgumentException("Column not found: " + columnName);
    }
}
