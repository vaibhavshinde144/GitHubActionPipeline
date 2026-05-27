package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");
    private final By productsTitle = By.cssSelector("span.title");
    private final By loginLogo = By.cssSelector(".login_logo");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        typeUsername(username);
        typePassword(password);
        clickLogin();
    }

    public void typeUsername(String username) {
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        usernameInput.clear();
        usernameInput.sendKeys(username);
    }

    public void typePassword(String password) {
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public boolean isProductPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productsTitle)).isDisplayed();
    }

    public boolean isLoginErrorMessageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
    }

    public String getLoginErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    public boolean isLoginPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton)).isDisplayed();
    }

    public boolean isPasswordMasked() {
        return "password".equalsIgnoreCase(driver.findElement(passwordField).getAttribute("type"));
    }

    public boolean isLogoVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginLogo)).isDisplayed();
    }

    public boolean isLoginButtonAlignedWithForm() {
        WebElement username = driver.findElement(usernameField);
        WebElement button = driver.findElement(loginButton);
        return Math.abs(username.getRect().getX() - button.getRect().getX()) <= 5
                && Math.abs(username.getRect().getWidth() - button.getRect().getWidth()) <= 5;
    }

    public String getErrorMessageColor() {
        return driver.findElement(errorMessage).getCssValue("color");
    }
}
