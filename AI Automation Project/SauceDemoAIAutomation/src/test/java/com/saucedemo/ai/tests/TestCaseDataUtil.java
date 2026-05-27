package com.saucedemo.ai.tests;

import utils.excel.ExcelReader;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class TestCaseDataUtil {
    private static final String DETAILED_TEST_CASES_SHEET = "Detailed Test Cases";
    private static final String REGRESSION_PRIORITY_SHEET = "Regression Priority";
    private static final String TEST_CASE_ID = "Test Case ID";

    private TestCaseDataUtil() {
    }

    static List<Map<String, String>> getAllRegressionCases() {
        return ExcelReader.getSheetData(DETAILED_TEST_CASES_SHEET, TEST_CASE_ID);
    }

    static List<Map<String, String>> getPriorityRegressionCases() {
        Set<String> priorityIds = getPriorityIds();
        return getAllRegressionCases().stream()
                .filter(row -> priorityIds.contains(row.get(TEST_CASE_ID)))
                .toList();
    }

    static Object[][] toDataProvider(List<Map<String, String>> rows) {
        Object[][] data = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i);
        }
        return data;
    }

    static Object[][] regressionCasesForScenarios(String... scenarioIds) {
        return toDataProvider(filterByScenario(getAllRegressionCases(), scenarioIds));
    }

    static Object[][] priorityCasesForScenarios(String... scenarioIds) {
        return toDataProvider(filterByScenario(getPriorityRegressionCases(), scenarioIds));
    }

    static Object[][] regressionCasesForIds(String... testCaseIds) {
        return toDataProvider(filterByTestCaseId(getAllRegressionCases(), testCaseIds));
    }

    static Object[][] priorityCasesForIds(String... testCaseIds) {
        return toDataProvider(filterByTestCaseId(getPriorityRegressionCases(), testCaseIds));
    }

    private static List<Map<String, String>> filterByScenario(List<Map<String, String>> rows, String... scenarioIds) {
        Set<String> selected = Set.of(scenarioIds);
        return rows.stream()
                .filter(row -> selected.contains(row.get("Scenario ID")))
                .toList();
    }

    private static List<Map<String, String>> filterByTestCaseId(List<Map<String, String>> rows, String... testCaseIds) {
        Set<String> selected = Set.of(testCaseIds);
        return rows.stream()
                .filter(row -> selected.contains(row.get(TEST_CASE_ID)))
                .toList();
    }

    private static Set<String> getPriorityIds() {
        Set<String> priorityIds = new HashSet<>();
        for (Map<String, String> row : ExcelReader.getSheetData(REGRESSION_PRIORITY_SHEET, TEST_CASE_ID)) {
            String testCaseId = row.get(TEST_CASE_ID);
            if (testCaseId != null && !testCaseId.isBlank()) {
                priorityIds.add(testCaseId);
            }
        }
        return priorityIds;
    }
}
