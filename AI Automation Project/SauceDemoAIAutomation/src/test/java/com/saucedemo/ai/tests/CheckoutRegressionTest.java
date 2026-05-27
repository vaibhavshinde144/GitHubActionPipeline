package com.saucedemo.ai.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

public class CheckoutRegressionTest extends SauceDemoTestBase {
    @DataProvider(name = "checkoutRegressionCases")
    public Object[][] checkoutRegressionCases() {
        return TestCaseDataUtil.regressionCasesForScenarios("SC_004", "SC_005");
    }

    @DataProvider(name = "checkoutPriorityCases")
    public Object[][] checkoutPriorityCases() {
        return TestCaseDataUtil.priorityCasesForScenarios("SC_004", "SC_005");
    }

    @Test(dataProvider = "checkoutRegressionCases", groups = {"regression"})
    public void verifyCheckoutAndOrderFlow(Map<String, String> testCase) {
        executeTestCase(testCase);
    }

    @Test(dataProvider = "checkoutPriorityCases", groups = {"priority"})
    public void verifyPriorityCheckoutAndOrderFlow(Map<String, String> testCase) {
        executeTestCase(testCase);
    }
}
