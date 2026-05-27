package org.aiautomationproject.qa.base;

import org.aiautomationproject.qa.utils.ConfigReader;
import org.aiautomationproject.qa.utils.DriverFactory;
import org.aiautomationproject.qa.utils.ExtentReportManager;
import org.aiautomationproject.qa.utils.ScreenshotUtils;
import org.aiautomationproject.qa.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.Instant;

public abstract class BaseTest {
    private static String suiteEmail;
    private static String suiteSetupFailure;
    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void createSuiteUserOnce() {
        if (!ConfigReader.getBoolean("suiteSignupEnabled") || suiteEmail != null) {
            return;
        }
        WebDriver setupDriver = DriverFactory.createDriver();
        try {
            suiteEmail = uniqueEmail();
            setupDriver.get(ConfigReader.get("baseUrl") + "/login");
            setupDriver.findElement(By.cssSelector("[data-qa='signup-name']")).sendKeys(TestData.get("testUserName"));
            setupDriver.findElement(By.cssSelector("[data-qa='signup-email']")).sendKeys(suiteEmail);
            setupDriver.findElement(By.cssSelector("[data-qa='signup-button']")).click();
            setupDriver.findElement(By.id("id_gender1")).click();
            setupDriver.findElement(By.id("password")).sendKeys(TestData.get("testUserPassword"));
            setupDriver.findElement(By.id("first_name")).sendKeys("Automation");
            setupDriver.findElement(By.id("last_name")).sendKeys("User");
            setupDriver.findElement(By.id("address1")).sendKeys("123 Test Street");
            setupDriver.findElement(By.id("state")).sendKeys("Karnataka");
            setupDriver.findElement(By.id("city")).sendKeys("Bengaluru");
            setupDriver.findElement(By.id("zipcode")).sendKeys("560001");
            setupDriver.findElement(By.id("mobile_number")).sendKeys("9876543210");
            setupDriver.findElement(By.cssSelector("[data-qa='create-account']")).click();
            setupDriver.findElement(By.cssSelector("[data-qa='continue-button']")).click();
            setupDriver.get(ConfigReader.get("baseUrl") + "/logout");
        } catch (RuntimeException e) {
            suiteSetupFailure = e.getClass().getSimpleName() + ": " + e.getMessage();
            System.err.println("Suite signup failed; test methods will continue and report this where login is required. " + suiteSetupFailure);
        } finally {
            DriverFactory.quitDriver();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.createDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    protected String uniqueEmail() {
        return "automation.user." + Instant.now().toEpochMilli() + "@testmail.com";
    }

    protected String suiteEmail() {
        if (suiteEmail == null) {
            suiteEmail = uniqueEmail();
        }
        return suiteEmail;
    }

    protected String suiteSetupFailure() {
        return suiteSetupFailure;
    }

    protected boolean pageContains(String text) {
        return driver.findElement(By.tagName("body")).getText().contains(text);
    }

    protected String attachScreenshot(String name) {
        String screenshotPath = ScreenshotUtils.capture(driver, name);
        ExtentReportManager.getTest().addScreenCaptureFromPath(screenshotPath);
        ExtentReportManager.logInfo("Screenshot captured: " + screenshotPath);
        return screenshotPath;
    }
}
