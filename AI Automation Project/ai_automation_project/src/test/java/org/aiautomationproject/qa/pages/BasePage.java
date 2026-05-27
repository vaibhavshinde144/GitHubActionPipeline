package org.aiautomationproject.qa.pages;

import org.aiautomationproject.qa.utils.ExtentReportManager;
import org.aiautomationproject.qa.utils.SmartLocator;
import org.aiautomationproject.qa.utils.WaitUtils;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public abstract class BasePage {
    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected void click(SmartLocator locator) {
        WebElement element = WaitUtils.clickable(driver, locator);
        WaitUtils.scrollIntoView(driver, element);
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            ExtentReportManager.logInfo("Click intercepted; using JavaScript click for " + locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected void type(SmartLocator locator, String value) {
        WebElement element = WaitUtils.visible(driver, locator);
        WaitUtils.scrollIntoView(driver, element);
        element.clear();
        element.sendKeys(value);
    }

    protected String textOf(SmartLocator locator) {
        return WaitUtils.visible(driver, locator).getText().trim();
    }

    protected boolean isVisible(SmartLocator locator) {
        try {
            return WaitUtils.visible(driver, locator).isDisplayed();
        } catch (RuntimeException e) {
            return false;
        }
    }

    protected String pageText() {
        try {
            return driver.findElement(org.openqa.selenium.By.tagName("body")).getText();
        } catch (StaleElementReferenceException e) {
            return driver.findElement(org.openqa.selenium.By.tagName("body")).getText();
        }
    }

    protected String currentUrl() {
        return driver.getCurrentUrl();
    }

    protected void waitForUrlContaining(String path) {
        WaitUtils.waitFor(driver).until((ExpectedCondition<Boolean>) d -> d != null && d.getCurrentUrl().contains(path));
    }
}
