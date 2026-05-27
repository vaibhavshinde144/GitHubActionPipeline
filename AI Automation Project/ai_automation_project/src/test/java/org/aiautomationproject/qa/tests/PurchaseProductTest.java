package org.aiautomationproject.qa.tests;

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
import org.testng.annotations.Test;

public class PurchaseProductTest extends RegressionTestBase {
    @Test(priority = 1, groups = {"critical", "e2e", "checkout", "payment"}, description = "TC_CHECKOUT_001, TC_CHECKOUT_004, TC_PAYMENT_001, TC_LOGOUT_001 - Purchase a Product Successfully")
    public void customerCanPurchaseProductSuccessfully() {
        String productName = TestData.get("productName");

        ExtentReportManager.logInfo("Step 1: Log in with suite-level registered customer");
        HomePage homePage = loginAsSuiteUser();

        ExtentReportManager.logInfo("Step 3: Open products and add the workbook product to cart: " + productName);
        ProductsPage productsPage = homePage.header().openProducts();
        CartPage cartPage = productsPage.addProductToCartAndViewCart(productName);
        Assert.assertTrue(cartPage.hasProduct(productName), productName + " should be visible in the cart");

        ExtentReportManager.logInfo("Step 4: Proceed to checkout and verify address/order page");
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        Assert.assertTrue(checkoutPage.isDeliveryAddressDisplayed(), "Delivery address should be displayed on checkout page");

        ExtentReportManager.logInfo("Step 5: Add order comment and continue to payment");
        PaymentPage paymentPage = checkoutPage.enterCommentAndPlaceOrder(TestData.get("orderComment"));

        ExtentReportManager.logInfo("Step 6: Enter valid card details and confirm order");
        OrderConfirmationPage confirmationPage = paymentPage.pay(
                TestData.get("paymentName"),
                TestData.get("cardNumber"),
                TestData.get("cvc"),
                TestData.get("expiryMonth"),
                TestData.get("expiryYear"));
        Assert.assertTrue(confirmationPage.isOrderConfirmed(), "Order confirmation should be displayed");
        attachScreenshot("TC_PURCHASE_SUCCESS_order_confirmation");

        ExtentReportManager.logInfo("Step 7: Suite account remains available for the rest of the regression run");
    }
}
