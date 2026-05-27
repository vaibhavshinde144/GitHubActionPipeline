package base;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import utils.browser.DriverManager;
import utils.config.ConfigReader;
import utils.listener.TestListener;
import utils.report.ExtentReportManager;

import java.lang.reflect.Method;
import java.time.Duration;

@Listeners(TestListener.class)
public abstract class BaseTest {
    protected WebDriver driver;
    protected ExtentTest extentTest;

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() {
        ExtentReportManager.initReports();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        ExtentReportManager.flushReports();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method, Object[] testData) {
        driver = DriverManager.initDriver(ConfigReader.get("browser", "chrome"));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("page.load.timeout.seconds", 30)));
        driver.manage().window().maximize();
        driver.get(ConfigReader.get("app.url"));
        extentTest = ExtentReportManager.createTest(resolveTestName(method, testData));
        logWorkbookTestDetails(testData);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    private String resolveTestName(Method method, Object[] testData) {
        if (testData != null && testData.length > 0 && testData[0] instanceof java.util.Map<?, ?> testCase) {
            String id = mapValue(testCase, "Test Case ID");
            String description = mapValue(testCase, "Test Case Description");
            if (description.isBlank()) {
                description = method.getName();
            }
            return id + " - " + description;
        }
        return method.getName();
    }

    private void logWorkbookTestDetails(Object[] testData) {
        if (extentTest == null || testData == null || testData.length == 0 || !(testData[0] instanceof java.util.Map<?, ?> testCase)) {
            return;
        }
        extentTest.info("Scenario: " + mapValue(testCase, "Scenario Description"));
        extentTest.info("Priority: " + mapValue(testCase, "Priority"));
        extentTest.info("Type: " + mapValue(testCase, "Type"));
        extentTest.info("Test Steps:<br>" + mapValue(testCase, "Test Steps").replace("\n", "<br>"));
        extentTest.info("Test Data: " + mapValue(testCase, "Test Data"));
        extentTest.info("Expected Result: " + mapValue(testCase, "Expected Result"));
    }

    private String mapValue(java.util.Map<?, ?> map, String key) {
        Object value = map.get(key);
        return value == null ? "" : String.valueOf(value);
    }
}
