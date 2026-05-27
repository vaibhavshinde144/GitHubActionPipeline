package com.saucedemo.ai.tests;

import utils.excel.ExcelReader;

import java.util.List;
import java.util.Map;

final class TestInputDataUtil {
    private TestInputDataUtil() {
    }

    static String username(String userType) {
        return requiredValue(rowByKey("Credentials", "userType", userType), "username", userType);
    }

    static String password(String userType) {
        return requiredValue(rowByKey("Credentials", "userType", userType), "password", userType);
    }

    static String productName(String dataSet) {
        return requiredValue(rowByKey("Products", "dataSet", dataSet), "productName", dataSet);
    }

    static List<String> productNames() {
        return ExcelReader.getInputSheetData("Products").stream()
                .map(row -> row.get("productName"))
                .filter(value -> value != null && !value.isBlank())
                .toList();
    }

    static CustomerData customer(String dataSet) {
        Map<String, String> row = rowByKey("Checkout", "dataSet", dataSet);
        return new CustomerData(
                requiredValue(row, "firstName", dataSet),
                requiredValue(row, "lastName", dataSet),
                requiredValue(row, "pincode", dataSet)
        );
    }

    private static Map<String, String> rowByKey(String sheetName, String keyColumn, String keyValue) {
        return ExcelReader.getInputSheetData(sheetName).stream()
                .filter(row -> keyValue.equalsIgnoreCase(row.getOrDefault(keyColumn, "")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing test input data: " + sheetName + " / " + keyValue));
    }

    private static String requiredValue(Map<String, String> row, String columnName, String dataSet) {
        String value = row.get(columnName);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing test input value for data set '" + dataSet + "', column '" + columnName + "'");
        }
        return value;
    }

    record CustomerData(String firstName, String lastName, String pincode) {
    }
}
