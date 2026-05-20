package pipelinetest1;

	import org.testng.annotations.AfterMethod;
	import org.testng.annotations.AfterSuite;
	import org.testng.annotations.BeforeMethod;
	import org.testng.annotations.BeforeSuite;
	import org.testng.annotations.Test;
	import org.testng.Assert;

	import java.time.Duration;

	import org.openqa.selenium.By;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.chrome.ChromeDriver;
	import org.openqa.selenium.support.ui.ExpectedConditions;
	import org.openqa.selenium.support.ui.WebDriverWait;

	public class Test1 {
		WebDriver driver;
		
		@BeforeSuite
		public void setup() {
			driver = new ChromeDriver();
			driver.get("https://practicetestautomation.com/practice-test-login/");
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		}
		
		@BeforeMethod
		public void beforeMethod() {
			System.out.println("*********TESTING STARTED*********");
		}
		
		@Test
		public void TC001_Login_Validation() {
			WebElement username = driver.findElement(By.xpath("//input[@name='username']"));
			username.sendKeys("student");
			WebElement password = driver.findElement(By.xpath("//input[@name='password']"));
			password.sendKeys("Password123");
			WebElement loginButton = driver.findElement(By.xpath("//button[@id='submit']"));
			loginButton.click();
			// Wait for navigation to complete - implicit wait does not wait for URL change
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.urlToBe("https://practicetestautomation.com/logged-in-successfully/"));
			String geturl = driver.getCurrentUrl();
			System.out.println(geturl);
			Assert.assertEquals(geturl, "https://practicetestautomation.com/logged-in-successfully/", "URL does not match expected value after login");
			
		}
		
		@AfterMethod
		public void afterMethod() {
			System.out.println("************TESTING COMPLETED*********");
		}
		
		@AfterSuite
		public void tearDown() {
		driver.quit();	
		}

		
	}
