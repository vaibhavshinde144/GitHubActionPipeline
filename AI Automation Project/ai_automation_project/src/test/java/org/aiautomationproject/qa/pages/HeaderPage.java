package org.aiautomationproject.qa.pages;

import org.aiautomationproject.qa.utils.ConfigReader;
import org.aiautomationproject.qa.utils.SmartLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderPage extends BasePage {
    private final SmartLocator signupLogin = SmartLocator.named(
            "Signup / Login navigation",
            By.cssSelector("a[href='/login']"),
            By.xpath("//a[contains(normalize-space(),'Signup') or contains(normalize-space(),'Login')]"));
    private final SmartLocator products = SmartLocator.named(
            "Products navigation",
            By.cssSelector("a[href='/products']"),
            By.xpath("//a[contains(normalize-space(),'Products')]"));
    private final SmartLocator logout = SmartLocator.named(
            "Logout navigation",
            By.cssSelector("a[href='/logout']"),
            By.xpath("//a[contains(normalize-space(),'Logout')]"));
    private final SmartLocator cart = SmartLocator.named(
            "Cart navigation",
            By.cssSelector("a[href='/view_cart']"),
            By.xpath("//a[contains(normalize-space(),'Cart')]"));
    private final SmartLocator deleteAccount = SmartLocator.named(
            "Delete Account navigation",
            By.cssSelector("a[href='/delete_account']"),
            By.xpath("//a[contains(normalize-space(),'Delete Account')]"));

    public HeaderPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage openLogin() {
        driver.get(ConfigReader.get("baseUrl") + "/login");
        return new LoginPage(driver);
    }

    public ProductsPage openProducts() {
        driver.get(ConfigReader.get("baseUrl") + "/products");
        return new ProductsPage(driver);
    }

    public LoginPage logout() {
        click(logout);
        return new LoginPage(driver);
    }

    public CartPage openCart() {
        driver.get(ConfigReader.get("baseUrl") + "/view_cart");
        return new CartPage(driver);
    }

    public void deleteAccountIfVisible() {
        if (isVisible(deleteAccount)) {
            click(deleteAccount);
        }
    }
}
