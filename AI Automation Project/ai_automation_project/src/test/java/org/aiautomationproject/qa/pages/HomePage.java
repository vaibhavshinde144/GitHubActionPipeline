package org.aiautomationproject.qa.pages;

import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {
    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HeaderPage header() {
        return new HeaderPage(driver);
    }
}
