package org.aiautomationproject.qa.tests;

import org.aiautomationproject.qa.pages.HeaderPage;
import org.aiautomationproject.qa.pages.HomePage;
import org.aiautomationproject.qa.pages.LoginPage;
import org.aiautomationproject.qa.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthenticationRegressionTest extends RegressionTestBase {
    @Test(priority = 2, groups = {"high", "authentication"}, description = "TC_LOGIN_002 - Verify login with invalid password")
    public void verifyLoginWithInvalidPassword() {
        LoginPage loginPage = new HeaderPage(driver).openLogin();

        loginPage.attemptLogin(suiteEmail(), "Wrong@123");

        Assert.assertTrue(loginPage.loginErrorText().contains("incorrect"));
    }

    @Test(priority = 3, groups = {"medium", "authentication"}, description = "TC_LOGIN_003 - Verify login with unregistered email")
    public void verifyLoginWithUnregisteredEmail() {
        LoginPage loginPage = new HeaderPage(driver).openLogin();

        loginPage.attemptLogin("unregistered." + System.currentTimeMillis() + "@test.com", "Password@123");

        Assert.assertTrue(loginPage.loginErrorText().contains("incorrect"));
    }

    @Test(priority = 4, groups = {"medium", "authentication"}, description = "TC_LOGIN_004 - Verify login with blank email and blank password")
    public void verifyLoginWithBlankEmailAndPassword() {
        LoginPage loginPage = new HeaderPage(driver).openLogin();

        Assert.assertFalse(loginPage.loginEmailValidationMessage().isBlank());
    }

    @Test(priority = 5, groups = {"medium", "authentication"}, description = "TC_LOGIN_005 - Verify login with blank email and valid password")
    public void verifyLoginWithBlankEmailAndValidPassword() {
        LoginPage loginPage = new HeaderPage(driver).openLogin();

        Assert.assertFalse(loginPage.loginPasswordValidationMessage("").isBlank());
    }

    @Test(priority = 1, groups = {"critical", "authentication", "checkout"}, description = "TC_LOGOUT_001 - Verify logout after order placement")
    public void verifyLogoutAfterOrderPlacement() {
        Assert.assertTrue(placeOrderWithValidCard().isOrderConfirmed());

        new HeaderPage(driver).logout();

        Assert.assertTrue(pageContains("Login to your account"));
    }
}
