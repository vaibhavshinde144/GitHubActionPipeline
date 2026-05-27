package com.saucedemo.ai.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

public class LoginRegressionTest extends SauceDemoTestBase {
    @DataProvider(name = "loginRegressionCases")
    public Object[][] loginRegressionCases() {
        return TestCaseDataUtil.regressionCasesForScenarios("SC_001");
    }

    @DataProvider(name = "loginPriorityCases")
    public Object[][] loginPriorityCases() {
        return TestCaseDataUtil.priorityCasesForScenarios("SC_001");
    }

    @Test(dataProvider = "loginRegressionCases", groups = {"regression"})
    public void verifyLoginValidation(Map<String, String> testCase) {
        executeTestCase(testCase);
    }

    @Test(dataProvider = "loginPriorityCases", groups = {"priority"})
    public void verifyPriorityLoginValidation(Map<String, String> testCase) {
        executeTestCase(testCase);
    }
}
