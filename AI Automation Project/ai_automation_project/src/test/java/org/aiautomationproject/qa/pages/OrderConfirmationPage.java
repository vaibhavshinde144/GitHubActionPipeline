package org.aiautomationproject.qa.pages;

import org.aiautomationproject.qa.utils.SmartLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderConfirmationPage extends BasePage {
    private final SmartLocator orderPlaced = SmartLocator.named(
            "Order placed heading",
            By.cssSelector("[data-qa='order-placed']"),
            By.xpath("//*[contains(normalize-space(),'Order Placed')]"));
    private final SmartLocator confirmationMessage = SmartLocator.named(
            "Order confirmation message",
            By.xpath("//*[contains(normalize-space(),'Congratulations! Your order has been confirmed!')]"),
            By.xpath("//*[contains(normalize-space(),'order has been confirmed')]"));

    public OrderConfirmationPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOrderConfirmed() {
        return isVisible(orderPlaced) || isVisible(confirmationMessage);
    }

    public boolean hidesSensitiveCardData(String fullCardNumber, String securityCode) {
        String text = pageText();
        return !text.contains(fullCardNumber) && !text.contains(securityCode);
    }
}
