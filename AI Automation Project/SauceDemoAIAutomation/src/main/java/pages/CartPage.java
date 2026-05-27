package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {
    private final By cartItemRows = By.cssSelector("div.cart_item");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutButton));
    }

    public int getCartItemCount() {
        return driver.findElements(cartItemRows).size();
    }

    public void clickCheckout() {
        WebElement checkout = wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkout);
        wait.until(ExpectedConditions.urlContains("checkout-step-one"));
    }

    public void removeProduct(String productName) {
        By removeButton = By.xpath("//div[@class='cart_item']//div[text()='" + productName + "']/ancestor::div[@class='cart_item']//button[text()='Remove']");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(removeButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='cart_item']//div[text()='" + productName + "']")));
    }

    public void continueShopping() {
        WebElement continueShopping = wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueShopping);
        wait.until(ExpectedConditions.urlContains("inventory"));
    }

    public boolean isProductInCart(String productName) {
        return !driver.findElements(By.xpath("//div[@class='cart_item']//div[text()='" + productName + "']")).isEmpty();
    }
}
