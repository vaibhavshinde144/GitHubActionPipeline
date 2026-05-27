package org.aiautomationproject.qa.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class WaitUtils {
    private WaitUtils() {
    }

    public static WebDriverWait waitFor(WebDriver driver) {
        int configuredWait = ConfigReader.getInt("explicitWaitSeconds");
        return new WebDriverWait(driver, Duration.ofSeconds(Math.min(configuredWait, 5)));
    }

    public static WebElement visible(WebDriver driver, SmartLocator locator) {
        return waitFor(driver).until(d -> locator.findVisible(d));
    }

    public static WebElement clickable(WebDriver driver, SmartLocator locator) {
        return waitFor(driver).until(d -> ExpectedConditions.elementToBeClickable(locator.findVisible(d)).apply(d));
    }

    public static void scrollIntoView(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }
}
