package com.saucedemo.ai.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

public class UserInterfaceRegressionTest extends SauceDemoTestBase {
    @DataProvider(name = "userInterfaceRegressionCases")
    public Object[][] userInterfaceRegressionCases() {
        return TestCaseDataUtil.regressionCasesForScenarios("SC_012");
    }

    @DataProvider(name = "userInterfacePriorityCases")
    public Object[][] userInterfacePriorityCases() {
        return TestCaseDataUtil.priorityCasesForScenarios("SC_012");
    }

    @Test(dataProvider = "userInterfaceRegressionCases", groups = {"regression"})
    public void verifyUserInterfaceValidation(Map<String, String> testCase) {
        executeTestCase(testCase);
    }

    @Test(dataProvider = "userInterfacePriorityCases", groups = {"priority"})
    public void verifyPriorityUserInterfaceValidation(Map<String, String> testCase) {
        executeTestCase(testCase);
    }
}
