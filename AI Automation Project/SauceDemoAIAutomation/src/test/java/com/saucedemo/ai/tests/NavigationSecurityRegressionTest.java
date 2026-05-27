package com.saucedemo.ai.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

public class NavigationSecurityRegressionTest extends SauceDemoTestBase {
    @DataProvider(name = "navigationSecurityRegressionCases")
    public Object[][] navigationSecurityRegressionCases() {
        return TestCaseDataUtil.regressionCasesForScenarios("SC_009", "SC_010", "SC_011");
    }

    @DataProvider(name = "navigationSecurityPriorityCases")
    public Object[][] navigationSecurityPriorityCases() {
        return TestCaseDataUtil.priorityCasesForScenarios("SC_009", "SC_010", "SC_011");
    }

    @Test(dataProvider = "navigationSecurityRegressionCases", groups = {"regression"})
    public void verifyNavigationSessionAndSecurity(Map<String, String> testCase) {
        executeTestCase(testCase);
    }

    @Test(dataProvider = "navigationSecurityPriorityCases", groups = {"priority"})
    public void verifyPriorityNavigationSessionAndSecurity(Map<String, String> testCase) {
        executeTestCase(testCase);
    }
}
