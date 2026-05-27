package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.screenshot.ScreenshotUtil;

public class OrderConfirmationPage extends BasePage {
    private final By completeHeader = By.cssSelector("h2.complete-header");
    private final By backHomeButton = By.id("back-to-products");

    public OrderConfirmationPage(WebDriver driver) {
        super(driver);
    }

    public String getOrderSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeHeader)).getText();
    }

    public void captureOrderSuccessScreenshot() {
        ScreenshotUtil.captureScreenshot(driver, "Order_Completion");
    }

    public void clickBackHome() {
        WebElement backHome = wait.until(ExpectedConditions.elementToBeClickable(backHomeButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backHome);
        wait.until(ExpectedConditions.urlContains("inventory"));
    }
}
