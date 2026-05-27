package org.aiautomationproject.qa.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class SmartLocator {
    private final String name;
    private final List<By> locators;

    private SmartLocator(String name, List<By> locators) {
        this.name = name;
        this.locators = locators;
    }

    public static SmartLocator named(String name, By primary, By... fallbacks) {
        List<By> allLocators = new java.util.ArrayList<>();
        allLocators.add(primary);
        allLocators.addAll(Arrays.asList(fallbacks));
        return new SmartLocator(name, allLocators);
    }

    public WebElement findVisible(WebDriver driver) {
        RuntimeException lastFailure = null;
        for (int index = 0; index < locators.size(); index++) {
            By locator = locators.get(index);
            try {
                WebElement element = driver.findElement(locator);
                if (element.isDisplayed()) {
                    if (index > 0) {
                        ExtentReportManager.logInfo("AI locator suggestion used for '" + name + "': " + locator);
                    }
                    return element;
                }
            } catch (RuntimeException e) {
                lastFailure = e;
            }
        }
        throw new NoSuchElementException("Unable to locate visible element '" + name + "' using " + locators, lastFailure);
    }
}
