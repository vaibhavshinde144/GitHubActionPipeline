package org.aiautomationproject.qa.pages;

import org.aiautomationproject.qa.utils.SmartLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends BasePage {
    private final SmartLocator proceedToCheckout = SmartLocator.named(
            "Proceed to checkout",
            By.cssSelector(".check_out"),
            By.xpath("//a[contains(normalize-space(),'Proceed To Checkout')]"));

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean hasProduct(String productName) {
        SmartLocator productRow = SmartLocator.named(
                "Cart product row: " + productName,
                By.xpath("//tr[.//a[normalize-space()='" + productName + "']]"),
                By.xpath("//*[contains(@class,'cart_description')]//*[normalize-space()='" + productName + "']"));
        return isVisible(productRow);
    }

    public CheckoutPage proceedToCheckout() {
        click(proceedToCheckout);
        return new CheckoutPage(driver);
    }

    public boolean isEmpty() {
        return pageText().contains("Cart is empty");
    }

    public int productCount() {
        List<WebElement> rows = driver.findElements(By.cssSelector("tbody tr"));
        return (int) rows.stream().filter(WebElement::isDisplayed).count();
    }

    public int quantityFor(String productName) {
        SmartLocator quantity = SmartLocator.named(
                "Cart quantity: " + productName,
                By.xpath("//tr[.//a[normalize-space()='" + productName + "']]//*[contains(@class,'cart_quantity')]//button"),
                By.xpath("//tr[.//*[normalize-space()='" + productName + "']]//*[contains(@class,'cart_quantity')]//*"));
        return Integer.parseInt(textOf(quantity).replaceAll("[^0-9]", ""));
    }

    public String priceFor(String productName) {
        SmartLocator price = SmartLocator.named(
                "Cart price: " + productName,
                By.xpath("//tr[.//a[normalize-space()='" + productName + "']]//*[contains(@class,'cart_price')]"),
                By.xpath("//tr[.//*[normalize-space()='" + productName + "']]//*[contains(text(),'Rs.')]"));
        return textOf(price);
    }

    public String totalFor(String productName) {
        SmartLocator total = SmartLocator.named(
                "Cart total: " + productName,
                By.xpath("//tr[.//a[normalize-space()='" + productName + "']]//*[contains(@class,'cart_total_price')]"),
                By.xpath("//tr[.//*[normalize-space()='" + productName + "']]//*[contains(@class,'cart_total')]"));
        return textOf(total);
    }

    public CartPage removeProduct(String productName) {
        By productRow = By.xpath("//tr[.//a[normalize-space()='" + productName + "']]");
        SmartLocator remove = SmartLocator.named(
                "Remove cart product: " + productName,
                By.xpath("//tr[.//a[normalize-space()='" + productName + "']]//a[contains(@class,'cart_quantity_delete')]"),
                By.xpath("//tr[.//*[normalize-space()='" + productName + "']]//a[contains(@href,'delete_cart')]"));
        click(remove);
        org.aiautomationproject.qa.utils.WaitUtils.waitFor(driver)
                .until(ExpectedConditions.or(
                        ExpectedConditions.invisibilityOfElementLocated(productRow),
                        ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Cart is empty")));
        return this;
    }

    public CartPage refresh() {
        driver.navigate().refresh();
        return this;
    }
}
