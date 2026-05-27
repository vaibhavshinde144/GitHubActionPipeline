package com.saucedemo.ai.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

public class ProductCatalogRegressionTest extends SauceDemoTestBase {
    @DataProvider(name = "productCatalogRegressionCases")
    public Object[][] productCatalogRegressionCases() {
        return TestCaseDataUtil.regressionCasesForScenarios("SC_002", "SC_006", "SC_008");
    }

    @DataProvider(name = "productCatalogPriorityCases")
    public Object[][] productCatalogPriorityCases() {
        return TestCaseDataUtil.priorityCasesForScenarios("SC_002", "SC_006", "SC_008");
    }

    @Test(dataProvider = "productCatalogRegressionCases", groups = {"regression"})
    public void verifyProductCatalogFeatures(Map<String, String> testCase) {
        executeTestCase(testCase);
    }

    @Test(dataProvider = "productCatalogPriorityCases", groups = {"priority"})
    public void verifyPriorityProductCatalogFeatures(Map<String, String> testCase) {
        executeTestCase(testCase);
    }
}
