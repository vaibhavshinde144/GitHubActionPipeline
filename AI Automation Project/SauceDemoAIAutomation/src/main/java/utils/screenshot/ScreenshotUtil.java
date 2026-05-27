package utils.screenshot;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {
    private static final String SCREENSHOT_DIRECTORY = "test-output/screenshots";

    public static String captureScreenshot(WebDriver driver, String fileName) {
        if (driver == null) {
            return null;
        }
        try {
            Path screenshotDir = Paths.get(SCREENSHOT_DIRECTORY);
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
            }
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String screenshotName = fileName.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";
            Path screenshotPath = screenshotDir.resolve(screenshotName);
            File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(file, screenshotPath.toFile());
            return screenshotPath.toAbsolutePath().toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
