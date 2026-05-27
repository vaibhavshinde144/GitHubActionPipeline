package utils.listener;

import base.BaseTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.report.ExtentReportManager;
import utils.screenshot.ScreenshotUtil;

public class TestListener implements ITestListener {
    @Override
    public void onTestStart(ITestResult result) {
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().info("Execution started");
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(Status.PASS, "Result: PASS");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Object currentClass = result.getInstance();
        if (currentClass instanceof BaseTest) {
            WebDriver driver = ((BaseTest) currentClass).getDriver();
            if (driver != null) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName() + "_FAIL");
                if (screenshotPath != null && ExtentReportManager.getTest() != null) {
                    ExtentReportManager.getTest().addScreenCaptureFromPath(screenshotPath);
                }
            }
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().log(Status.FAIL, "Result: FAIL");
                ExtentReportManager.getTest().fail(result.getThrowable());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(Status.SKIP, "Result: SKIP");
            ExtentReportManager.getTest().skip(result.getThrowable());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // no-op
    }

    @Override
    public void onStart(ITestContext context) {
        // no-op
    }

    @Override
    public void onFinish(ITestContext context) {
        // no-op
    }
}
