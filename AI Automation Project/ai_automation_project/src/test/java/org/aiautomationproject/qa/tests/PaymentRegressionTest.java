package org.aiautomationproject.qa.tests;

import org.aiautomationproject.qa.pages.OrderConfirmationPage;
import org.aiautomationproject.qa.pages.PaymentPage;
import org.aiautomationproject.qa.utils.ExtentReportManager;
import org.aiautomationproject.qa.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PaymentRegressionTest extends RegressionTestBase {
    @Test(priority = 1, groups = {"critical", "payment"}, description = "TC_PAYMENT_001 - Verify successful payment using valid card details")
    public void verifySuccessfulPaymentUsingValidCardDetails() {
        OrderConfirmationPage confirmationPage = placeOrderWithValidCard();

        Assert.assertTrue(confirmationPage.isOrderConfirmed());
        attachScreenshot("TC_PAYMENT_001_successful_order");
    }

    @Test(priority = 2, groups = {"critical", "payment", "negative"}, description = "TC_PAYMENT_002 - Verify payment with invalid card number")
    public void verifyPaymentWithInvalidCardNumber() {
        PaymentPage paymentPage = openPaymentWithDefaultProduct();

        OrderConfirmationPage confirmationPage = paymentPage.payWithInvalidCard(
                TestData.get("paymentName"),
                TestData.get("invalidCardNumber"),
                TestData.get("cvc"),
                TestData.get("expiryMonth"),
                TestData.get("expiryYear"));

        boolean orderConfirmed = confirmationPage.isOrderConfirmed();
        if (orderConfirmed) {
            ExtentReportManager.logInfo("Defect evidence: invalid card number was accepted and order confirmation was displayed.");
            attachScreenshot("TC_PAYMENT_002_invalid_card_unexpected_order_confirmation");
        }

        Assert.assertFalse(orderConfirmed, "Invalid card should not place the order");
    }

    @Test(priority = 3, groups = {"high", "payment", "negative"}, description = "TC_PAYMENT_003 - Verify payment with blank mandatory payment fields")
    public void verifyPaymentWithBlankMandatoryPaymentFields() {
        PaymentPage paymentPage = openPaymentWithDefaultProduct();

        paymentPage.submitBlankPayment();

        Assert.assertFalse(paymentPage.nameOnCardValidationMessage().isBlank());
    }
}
