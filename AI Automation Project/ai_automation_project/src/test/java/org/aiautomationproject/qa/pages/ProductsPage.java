package org.aiautomationproject.qa.pages;

import org.aiautomationproject.qa.utils.SmartLocator;
import org.aiautomationproject.qa.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductsPage extends BasePage {
    private final SmartLocator searchInput = SmartLocator.named(
            "Search product input",
            By.id("search_product"),
            By.name("search"),
            By.cssSelector("input[placeholder='Search Product']"));
    private final SmartLocator searchButton = SmartLocator.named(
            "Search button",
            By.id("submit_search"),
            By.cssSelector("button[type='button']"));

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public CartPage addProductToCartAndViewCart(String productName) {
        addProductToCart(productName);
        SmartLocator viewCart = SmartLocator.named(
                "View cart from add modal",
                By.xpath("//u[normalize-space()='View Cart']/ancestor::a"),
                By.xpath("//a[contains(normalize-space(),'View Cart')]"));
        click(viewCart);
        return new CartPage(driver);
    }

    public ProductsPage addProductToCartAndContinue(String productName) {
        addProductToCart(productName);
        SmartLocator continueShopping = SmartLocator.named(
                "Continue shopping",
                By.xpath("//button[contains(normalize-space(),'Continue Shopping')]"),
                By.cssSelector(".btn-success.close-modal"));
        click(continueShopping);
        return this;
    }

    public ProductDetailPage openProductDetail(String productName) {
        SmartLocator viewProduct = SmartLocator.named(
                "View product: " + productName,
                By.xpath("//div[contains(@class,'product-image-wrapper')][.//p[normalize-space()='" + productName + "']]//a[contains(.,'View Product')]"),
                By.xpath("//p[normalize-space()='" + productName + "']/ancestor::div[contains(@class,'product-image-wrapper')]//a[contains(.,'View Product')]"));
        click(viewProduct);
        return new ProductDetailPage(driver);
    }

    public ProductsPage search(String value) {
        type(searchInput, value);
        click(searchButton);
        return this;
    }

    public boolean hasSearchResultsHeading() {
        return pageText().contains("Searched Products");
    }

    public boolean pageContainsUnsafeScriptText(String scriptText) {
        return pageText().contains(scriptText);
    }

    public boolean noScriptAlertIsOpen() {
        try {
            driver.switchTo().alert();
            return false;
        } catch (NoAlertPresentException e) {
            return true;
        }
    }

    private void addProductToCart(String productName) {
        SmartLocator addToCart = SmartLocator.named(
                "Add product to cart: " + productName,
                By.xpath("//div[contains(@class,'productinfo')][.//p[normalize-space()='" + productName + "']]//a[contains(@class,'add-to-cart')]"),
                By.xpath("//p[normalize-space()='" + productName + "']/following-sibling::a[contains(.,'Add to cart')]"),
                By.xpath("//div[contains(@class,'single-products')][.//*[normalize-space()='" + productName + "']]//a[contains(.,'Add to cart')]"));
        click(addToCart);
        WaitUtils.visible(driver, SmartLocator.named("Added modal", By.id("cartModal"), By.xpath("//*[contains(normalize-space(),'Added!')]")));
    }

    public int visibleProductCount() {
        List<WebElement> products = driver.findElements(By.cssSelector(".features_items .product-image-wrapper"));
        return (int) products.stream().filter(WebElement::isDisplayed).count();
    }
}
