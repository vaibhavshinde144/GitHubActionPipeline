package org.aiautomationproject.qa.pages;

import org.aiautomationproject.qa.utils.SmartLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class AccountInformationPage extends BasePage {
    private final SmartLocator title = SmartLocator.named("Mr title", By.id("id_gender1"));
    private final SmartLocator password = SmartLocator.named("Account password", By.id("password"));
    private final SmartLocator day = SmartLocator.named("Birth day", By.id("days"));
    private final SmartLocator month = SmartLocator.named("Birth month", By.id("months"));
    private final SmartLocator year = SmartLocator.named("Birth year", By.id("years"));
    private final SmartLocator firstName = SmartLocator.named("First name", By.id("first_name"));
    private final SmartLocator lastName = SmartLocator.named("Last name", By.id("last_name"));
    private final SmartLocator company = SmartLocator.named("Company", By.id("company"));
    private final SmartLocator address = SmartLocator.named("Address", By.id("address1"));
    private final SmartLocator country = SmartLocator.named("Country", By.id("country"));
    private final SmartLocator state = SmartLocator.named("State", By.id("state"));
    private final SmartLocator city = SmartLocator.named("City", By.id("city"));
    private final SmartLocator zipCode = SmartLocator.named("Zip code", By.id("zipcode"));
    private final SmartLocator mobile = SmartLocator.named("Mobile number", By.id("mobile_number"));
    private final SmartLocator createAccount = SmartLocator.named(
            "Create account",
            By.cssSelector("[data-qa='create-account']"),
            By.xpath("//button[normalize-space()='Create Account']"));
    private final SmartLocator continueButton = SmartLocator.named(
            "Continue after account created",
            By.cssSelector("[data-qa='continue-button']"),
            By.xpath("//a[normalize-space()='Continue']"));

    public AccountInformationPage(WebDriver driver) {
        super(driver);
    }

    public HomePage createAccount(String passwordValue) {
        click(title);
        type(password, passwordValue);
        new Select(org.aiautomationproject.qa.utils.WaitUtils.visible(driver, day)).selectByValue("10");
        new Select(org.aiautomationproject.qa.utils.WaitUtils.visible(driver, month)).selectByValue("5");
        new Select(org.aiautomationproject.qa.utils.WaitUtils.visible(driver, year)).selectByValue("1995");
        type(firstName, "Automation");
        type(lastName, "User");
        type(company, "QA Practice");
        type(address, "123 Test Street");
        new Select(org.aiautomationproject.qa.utils.WaitUtils.visible(driver, country)).selectByVisibleText("India");
        type(state, "Karnataka");
        type(city, "Bengaluru");
        type(zipCode, "560001");
        type(mobile, "9876543210");
        click(createAccount);
        click(continueButton);
        return new HomePage(driver);
    }
}
