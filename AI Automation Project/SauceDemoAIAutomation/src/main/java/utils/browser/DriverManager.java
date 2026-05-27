package utils.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import utils.config.ConfigReader;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DriverManager {
    public static WebDriver initDriver(String browserName) {
        String browser = browserName == null || browserName.isBlank()
                ? ConfigReader.get("browser", "chrome")
                : browserName;
        browser = browser.toLowerCase(Locale.ROOT);
        Set<String> supportedBrowsers = ConfigReader.getCsvSet("supported.browsers");
        if (!supportedBrowsers.isEmpty() && !supportedBrowsers.contains(browser)) {
            throw new IllegalArgumentException("Unsupported browser: " + browser + ". Supported browsers: " + supportedBrowsers);
        }

        switch (browser) {
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments(commonWindowArguments());
                if (ConfigReader.getBoolean("headless", false)) {
                    edgeOptions.addArguments("--headless=new");
                }
                return new EdgeDriver(edgeOptions);
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (ConfigReader.getBoolean("headless", false)) {
                    firefoxOptions.addArguments("-headless");
                }
                return new FirefoxDriver(firefoxOptions);
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments(commonWindowArguments());
                if (ConfigReader.getBoolean("headless", false)) {
                    chromeOptions.addArguments("--headless=new");
                }
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_setting_values.notifications", 2);
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                chromeOptions.setExperimentalOption("prefs", prefs);
                return new ChromeDriver(chromeOptions);
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private static String[] commonWindowArguments() {
        return new String[]{
                "--disable-gpu",
                "--window-size=" + ConfigReader.getInt("window.width", 1920) + "," + ConfigReader.getInt("window.height", 1080),
                "--disable-notifications",
                "--disable-popup-blocking",
                "--disable-save-password-bubble",
                "--disable-password-manager-reauthentication",
                "--disable-translate",
                "--disable-infobars",
                "--disable-extensions"
        };
    }
}
