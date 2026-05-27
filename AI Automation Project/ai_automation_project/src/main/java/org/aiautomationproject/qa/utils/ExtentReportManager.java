package org.aiautomationproject.qa.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Files;
import java.nio.file.Path;

public final class ExtentReportManager {
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    private ExtentReportManager() {
    }

    public static synchronized ExtentReports getReports() {
        if (extentReports == null) {
            try {
                Path reportDir = Path.of("target", "extent-report");
                Files.createDirectories(reportDir);
                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportDir.resolve("AutomationExerciseReport.html").toString());
                sparkReporter.config().setReportName("Automation Exercise Regression Report");
                sparkReporter.config().setDocumentTitle("Purchase Product Regression");
                extentReports = new ExtentReports();
                extentReports.attachReporter(sparkReporter);
                extentReports.setSystemInfo("Application", "Automation Exercise");
                extentReports.setSystemInfo("Framework", "Selenium + TestNG + POM");
            } catch (Exception e) {
                throw new IllegalStateException("Unable to initialize Extent report", e);
            }
        }
        return extentReports;
    }

    public static void createTest(String testName) {
        TEST.set(getReports().createTest(testName));
    }

    public static ExtentTest getTest() {
        return TEST.get();
    }

    public static void logInfo(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }

    public static void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
