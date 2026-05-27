package org.aiautomationproject.qa.tests;

import org.aiautomationproject.qa.pages.CheckoutPage;
import org.aiautomationproject.qa.pages.HeaderPage;
import org.aiautomationproject.qa.pages.LoginPage;
import org.aiautomationproject.qa.pages.OrderConfirmationPage;
import org.aiautomationproject.qa.pages.ProductsPage;
import org.aiautomationproject.qa.utils.ConfigReader;
import org.aiautomationproject.qa.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SecurityRegressionTest extends RegressionTestBase {
    @Test(priority = 1, groups = {"critical", "security"}, description = "TC_SEC_001 - Verify direct checkout URL access without login is restricted")
    public void verifyDirectCheckoutUrlAccessWithoutLoginIsRestricted() {
        driver.get(ConfigReader.get("baseUrl") + "/checkout");

        Assert.assertTrue(pageContains("Signup") || pageContains("Login") || pageContains("Cart is empty"));
    }

    @Test(priority = 2, groups = {"critical", "security"}, description = "TC_SEC_002 - Verify SQL injection attempt on login fields")
    public void verifySqlInjectionAttemptOnLoginFields() {
        LoginPage loginPage = new HeaderPage(driver).openLogin();

        loginPage.attemptLogin(TestData.get("sqlInjectionEmail"), TestData.get("sqlInjectionPassword"));

        Assert.assertTrue(loginPage.loginWasBlockedOrRejected());
    }

    @Test(priority = 3, groups = {"critical", "security"}, description = "TC_SEC_003 - Verify XSS payload in product search field")
    public void verifyXssPayloadInProductSearchField() {
        ProductsPage productsPage = new HeaderPage(driver).openProducts();

        productsPage.search(TestData.get("xssPayload"));

        Assert.assertTrue(productsPage.noScriptAlertIsOpen());
        Assert.assertTrue(productsPage.visibleProductCount() >= 0);
    }

    @Test(priority = 4, groups = {"critical", "security"}, description = "TC_SEC_004 - Verify XSS payload in checkout comment field")
    public void verifyXssPayloadInCheckoutCommentField() {
        CheckoutPage checkoutPage = openCheckoutWithDefaultProduct();

        checkoutPage.enterCommentAndPlaceOrder(TestData.get("xssPayload"));

        Assert.assertTrue(checkoutPage.commentPayloadIsNotRendered(TestData.get("xssPayload")));
    }

    @Test(priority = 5, groups = {"critical", "security"}, description = "TC_SEC_005 - Verify protected page cannot be accessed after logout")
    public void verifyProtectedPageCannotBeAccessedAfterLogout() {
        createLoggedInUser();
        new HeaderPage(driver).logout();

        driver.get(ConfigReader.get("baseUrl") + "/checkout");

        Assert.assertTrue(pageContains("Signup") || pageContains("Login") || pageContains("Cart is empty"));
    }

    @Test(priority = 6, groups = {"critical", "security", "payment"}, description = "TC_SEC_006 - Verify card details are masked or not exposed after payment")
    public void verifyCardDetailsAreNotExposedAfterPayment() {
        OrderConfirmationPage confirmationPage = placeOrderWithValidCard();

        Assert.assertTrue(confirmationPage.hidesSensitiveCardData(TestData.get("cardNumber"), TestData.get("cvc")));
    }
}
