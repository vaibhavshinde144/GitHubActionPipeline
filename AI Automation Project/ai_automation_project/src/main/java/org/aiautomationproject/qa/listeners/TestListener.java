package org.aiautomationproject.qa.listeners;

import com.aventstack.extentreports.Status;
import org.aiautomationproject.qa.utils.DriverFactory;
import org.aiautomationproject.qa.utils.ExtentReportManager;
import org.aiautomationproject.qa.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    @Override
    public void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();
        String testName = description == null || description.isBlank()
                ? result.getMethod().getMethodName()
                : description;
        ExtentReportManager.createTest(testName);
        ExtentReportManager.getTest().assignCategory(result.getTestClass().getRealClass().getSimpleName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable());
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            String screenshotPath = ScreenshotUtils.capture(driver, result.getMethod().getMethodName());
            ExtentReportManager.getTest().addScreenCaptureFromPath(screenshotPath);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flush();
    }
}
