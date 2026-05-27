package org.aiautomationproject.qa.pages;

import org.aiautomationproject.qa.utils.SmartLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentPage extends BasePage {
    private final SmartLocator nameOnCard = SmartLocator.named("Name on card", By.cssSelector("[data-qa='name-on-card']"), By.name("name_on_card"));
    private final SmartLocator cardNumber = SmartLocator.named("Card number", By.cssSelector("[data-qa='card-number']"), By.name("card_number"));
    private final SmartLocator cvc = SmartLocator.named("CVC", By.cssSelector("[data-qa='cvc']"), By.name("cvc"));
    private final SmartLocator expiryMonth = SmartLocator.named("Expiry month", By.cssSelector("[data-qa='expiry-month']"), By.name("expiry_month"));
    private final SmartLocator expiryYear = SmartLocator.named("Expiry year", By.cssSelector("[data-qa='expiry-year']"), By.name("expiry_year"));
    private final SmartLocator payAndConfirm = SmartLocator.named(
            "Pay and confirm order",
            By.cssSelector("[data-qa='pay-button']"),
            By.xpath("//button[contains(normalize-space(),'Pay and Confirm Order')]"));

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public OrderConfirmationPage pay(String name, String number, String securityCode, String month, String year) {
        type(nameOnCard, name);
        type(cardNumber, number);
        type(cvc, securityCode);
        type(expiryMonth, month);
        type(expiryYear, year);
        click(payAndConfirm);
        return new OrderConfirmationPage(driver);
    }

    public PaymentPage submitBlankPayment() {
        click(payAndConfirm);
        return this;
    }

    public String nameOnCardValidationMessage() {
        return org.aiautomationproject.qa.utils.WaitUtils.visible(driver, nameOnCard).getAttribute("validationMessage");
    }

    public OrderConfirmationPage payWithInvalidCard(String name, String number, String securityCode, String month, String year) {
        return pay(name, number, securityCode, month, year);
    }

    public boolean isPaymentFormDisplayed() {
        return isVisible(nameOnCard);
    }
}
