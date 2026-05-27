package org.aiautomationproject.qa.tests;

import org.aiautomationproject.qa.pages.CartPage;
import org.aiautomationproject.qa.pages.CheckoutPage;
import org.aiautomationproject.qa.pages.HeaderPage;
import org.aiautomationproject.qa.pages.PaymentPage;
import org.aiautomationproject.qa.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutRegressionTest extends RegressionTestBase {
    @Test(priority = 1, groups = {"critical", "checkout"}, description = "TC_CHECKOUT_001 - Verify checkout with products in cart for logged-in user")
    public void verifyCheckoutWithProductForLoggedInUser() {
        CheckoutPage checkoutPage = openCheckoutWithDefaultProduct();

        Assert.assertTrue(checkoutPage.isDeliveryAddressDisplayed());
        Assert.assertTrue(checkoutPage.hasProduct(TestData.get("productName")));
    }

    @Test(priority = 5, groups = {"medium", "checkout"}, description = "TC_CHECKOUT_004 - Verify comment section during checkout accepts valid text")
    public void verifyCheckoutCommentAcceptsValidText() {
        PaymentPage paymentPage = openCheckoutWithDefaultProduct()
                .enterCommentAndPlaceOrder(TestData.get("orderComment"));

        Assert.assertTrue(paymentPage.isPaymentFormDisplayed());
    }

    @Test(priority = 3, groups = {"high", "checkout"}, description = "TC_CHECKOUT_003 - Verify order summary calculation during checkout")
    public void verifyOrderSummaryDuringCheckout() {
        String productName = TestData.get("productName");
        CheckoutPage checkoutPage = openCheckoutWithDefaultProduct();

        Assert.assertTrue(checkoutPage.hasProduct(productName));
    }

    @Test(priority = 2, groups = {"critical", "checkout"}, description = "TC_CHECKOUT_002 - Verify delivery address is displayed during checkout")
    public void verifyDeliveryAddressDisplayedDuringCheckout() {
        CheckoutPage checkoutPage = openCheckoutWithDefaultProduct();

        Assert.assertTrue(checkoutPage.isDeliveryAddressDisplayed());
    }

    @Test(priority = 4, groups = {"high", "checkout"}, description = "TC_CHECKOUT_005 - Verify checkout with empty cart is not allowed")
    public void verifyCheckoutWithEmptyCartIsNotAllowed() {
        createLoggedInUser();
        CartPage cartPage = new HeaderPage(driver).openCart();

        Assert.assertTrue(cartPage.isEmpty());
    }

    @Test(priority = 6, groups = {"medium", "checkout"}, description = "TC_CHECKOUT_006 - Verify removing product from cart and then trying to place order")
    public void verifyRemovingProductBlocksOrderPlacement() {
        String productName = TestData.get("productName");
        CartPage cartPage = addDefaultProductToCart();

        cartPage.removeProduct(productName);

        Assert.assertTrue(cartPage.isEmpty());
    }
}
