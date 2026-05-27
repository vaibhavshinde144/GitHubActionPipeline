package org.aiautomationproject.qa.pages;

import org.aiautomationproject.qa.utils.SmartLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailPage extends BasePage {
    private final SmartLocator quantity = SmartLocator.named("Product quantity", By.id("quantity"), By.name("quantity"));
    private final SmartLocator addToCart = SmartLocator.named(
            "Add product from detail",
            By.cssSelector("button.cart"),
            By.xpath("//button[contains(normalize-space(),'Add to cart')]"));
    private final SmartLocator viewCart = SmartLocator.named(
            "View cart from add modal",
            By.xpath("//u[normalize-space()='View Cart']/ancestor::a"),
            By.xpath("//a[contains(normalize-space(),'View Cart')]"));

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public CartPage addQuantityAndViewCart(String quantityValue) {
        type(quantity, quantityValue);
        click(addToCart);
        click(viewCart);
        return new CartPage(driver);
    }

    public String quantityValue() {
        return org.aiautomationproject.qa.utils.WaitUtils.visible(driver, quantity).getAttribute("value");
    }
}
