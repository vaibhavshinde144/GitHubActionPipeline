package org.aiautomationproject.qa.pages;

import org.aiautomationproject.qa.utils.SmartLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {
    private final SmartLocator addressSection = SmartLocator.named(
            "Delivery address section",
            By.id("address_delivery"),
            By.xpath("//*[contains(normalize-space(),'Your delivery address')]"));
    private final SmartLocator comment = SmartLocator.named(
            "Checkout comment",
            By.name("message"),
            By.cssSelector("textarea"));
    private final SmartLocator placeOrder = SmartLocator.named(
            "Place order",
            By.cssSelector("a[href='/payment'].check_out"),
            By.xpath("//a[contains(normalize-space(),'Place Order')]"));

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDeliveryAddressDisplayed() {
        return isVisible(addressSection);
    }

    public boolean hasProduct(String productName) {
        SmartLocator product = SmartLocator.named(
                "Checkout product: " + productName,
                By.xpath("//tr[.//a[normalize-space()='" + productName + "']]"),
                By.xpath("//*[contains(@class,'cart_description')]//*[normalize-space()='" + productName + "']"));
        return isVisible(product);
    }

    public boolean commentPayloadIsNotRendered(String payload) {
        return !pageText().contains(payload);
    }

    public PaymentPage enterCommentAndPlaceOrder(String orderComment) {
        type(comment, orderComment);
        click(placeOrder);
        return new PaymentPage(driver);
    }
}
