package org.aiautomationproject.qa.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TestData {
    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

    private TestData() {
    }

    public static String get(String key) {
        return CACHE.computeIfAbsent(key, value ->
                ExcelTestDataReader.getValueFromResource(ConfigReader.get("testDataWorkbook"), "TestData", value));
    }
}
