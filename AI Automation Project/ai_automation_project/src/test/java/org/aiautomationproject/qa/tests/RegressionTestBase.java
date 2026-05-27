package org.aiautomationproject.qa.tests;

import org.aiautomationproject.qa.base.BaseTest;
import org.aiautomationproject.qa.pages.CartPage;
import org.aiautomationproject.qa.pages.CheckoutPage;
import org.aiautomationproject.qa.pages.HeaderPage;
import org.aiautomationproject.qa.pages.HomePage;
import org.aiautomationproject.qa.pages.LoginPage;
import org.aiautomationproject.qa.pages.OrderConfirmationPage;
import org.aiautomationproject.qa.pages.PaymentPage;
import org.aiautomationproject.qa.pages.ProductsPage;
import org.aiautomationproject.qa.utils.ExtentReportManager;
import org.aiautomationproject.qa.utils.TestData;
import org.testng.Assert;

public abstract class RegressionTestBase extends BaseTest {
    protected HomePage createLoggedInUser() {
        return loginAsSuiteUser();
    }

    protected HomePage createLoggedInUser(String email) {
        LoginPage loginPage = new HeaderPage(driver).openLogin();
        return loginPage.startSignup(TestData.get("testUserName"), email)
                .createAccount(TestData.get("testUserPassword"));
    }

    protected HomePage createUserThenLogin() {
        return loginAsSuiteUser();
    }

    protected HomePage loginAsSuiteUser() {
        if (suiteSetupFailure() != null) {
            Assert.fail("Suite signup failed before login. " + suiteSetupFailure());
        }
        LoginPage loginPage = new HeaderPage(driver).openLogin();
        return loginPage.login(suiteEmail(), TestData.get("testUserPassword"));
    }

    protected CartPage addDefaultProductToCart() {
        HomePage homePage = loginAsSuiteUser();
        ProductsPage productsPage = homePage.header().openProducts();
        return productsPage.addProductToCartAndViewCart(TestData.get("productName"));
    }

    protected CheckoutPage openCheckoutWithDefaultProduct() {
        return addDefaultProductToCart().proceedToCheckout();
    }

    protected PaymentPage openPaymentWithDefaultProduct() {
        return openCheckoutWithDefaultProduct().enterCommentAndPlaceOrder(TestData.get("orderComment"));
    }

    protected OrderConfirmationPage placeOrderWithValidCard() {
        OrderConfirmationPage confirmationPage = openPaymentWithDefaultProduct().pay(
                TestData.get("paymentName"),
                TestData.get("cardNumber"),
                TestData.get("cvc"),
                TestData.get("expiryMonth"),
                TestData.get("expiryYear"));
        if (confirmationPage.isOrderConfirmed()) {
            ExtentReportManager.logInfo("Order confirmed successfully with valid card data.");
            attachScreenshot("successful_order_confirmation");
        }
        return confirmationPage;
    }

    protected void deleteAccountIfPossible() {
        new HeaderPage(driver).deleteAccountIfVisible();
    }
}
