package com.saucedemo.ai.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

public class CartRegressionTest extends SauceDemoTestBase {
    @DataProvider(name = "cartRegressionCases")
    public Object[][] cartRegressionCases() {
        return TestCaseDataUtil.regressionCasesForScenarios("SC_003", "SC_007");
    }

    @DataProvider(name = "cartPriorityCases")
    public Object[][] cartPriorityCases() {
        return TestCaseDataUtil.priorityCasesForScenarios("SC_003", "SC_007");
    }

    @Test(dataProvider = "cartRegressionCases", groups = {"regression"})
    public void verifyCartFunctionality(Map<String, String> testCase) {
        executeTestCase(testCase);
    }

    @Test(dataProvider = "cartPriorityCases", groups = {"priority"})
    public void verifyPriorityCartFunctionality(Map<String, String> testCase) {
        executeTestCase(testCase);
    }
}
