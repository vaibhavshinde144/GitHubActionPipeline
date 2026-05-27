package org.aiautomationproject.qa.pages;

import org.aiautomationproject.qa.utils.SmartLocator;
import org.aiautomationproject.qa.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private final SmartLocator loginEmail = SmartLocator.named(
            "Login email",
            By.cssSelector("[data-qa='login-email']"),
            By.name("email"),
            By.xpath("//input[@placeholder='Email Address']"));
    private final SmartLocator loginPassword = SmartLocator.named(
            "Login password",
            By.cssSelector("[data-qa='login-password']"),
            By.name("password"),
            By.xpath("//input[@placeholder='Password']"));
    private final SmartLocator loginButton = SmartLocator.named(
            "Login button",
            By.cssSelector("[data-qa='login-button']"),
            By.xpath("//button[normalize-space()='Login']"));
    private final SmartLocator loginError = SmartLocator.named(
            "Login error",
            By.xpath("//p[contains(normalize-space(),'Your email or password is incorrect')]"),
            By.cssSelector(".login-form p"));
    private final SmartLocator signupName = SmartLocator.named(
            "Signup name",
            By.cssSelector("[data-qa='signup-name']"),
            By.name("name"),
            By.xpath("//input[@placeholder='Name']"));
    private final SmartLocator signupEmail = SmartLocator.named(
            "Signup email",
            By.cssSelector("[data-qa='signup-email']"),
            By.xpath("(//input[@placeholder='Email Address'])[last()]"));
    private final SmartLocator signupButton = SmartLocator.named(
            "Signup button",
            By.cssSelector("[data-qa='signup-button']"),
            By.xpath("//button[normalize-space()='Signup']"));

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public HomePage login(String email, String password) {
        type(loginEmail, email);
        type(loginPassword, password);
        click(loginButton);
        return new HomePage(driver);
    }

    public LoginPage attemptLogin(String email, String password) {
        type(loginEmail, email);
        type(loginPassword, password);
        click(loginButton);
        return this;
    }

    public String loginErrorText() {
        return textOf(loginError);
    }

    public String loginEmailValidationMessage() {
        click(loginButton);
        return WaitUtils.visible(driver, loginEmail).getAttribute("validationMessage");
    }

    public String loginPasswordValidationMessage(String email) {
        type(loginEmail, email);
        click(loginButton);
        return WaitUtils.visible(driver, loginPassword).getAttribute("validationMessage");
    }

    public boolean loginWasBlockedOrRejected() {
        String emailValidation = WaitUtils.visible(driver, loginEmail).getAttribute("validationMessage");
        if (emailValidation != null && !emailValidation.isBlank()) {
            return true;
        }
        return isVisible(loginError) && loginErrorText().contains("incorrect");
    }

    public AccountInformationPage startSignup(String name, String email) {
        type(signupName, name);
        type(signupEmail, email);
        click(signupButton);
        return new AccountInformationPage(driver);
    }
}
