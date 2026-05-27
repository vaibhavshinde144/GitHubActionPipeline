package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage extends BasePage {
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By finishButton = By.id("finish");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");
    private final By summaryInfo = By.cssSelector(".summary_info");

    public CheckoutPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
    }

    public void enterCustomerInformation(String firstName, String lastName, String postalCode) {
        setCheckoutFieldValues(firstName, lastName, postalCode);
        clickContinue();
    }

    public void clickContinue() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    public String getCheckoutErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    public void cancel() {
        WebElement cancel = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cancel);
        wait.until(ExpectedConditions.urlContains("cart"));
    }

    public boolean isOverviewDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(summaryInfo)).isDisplayed();
    }

    public boolean areOverviewDetailsVisible() {
        return !driver.findElements(By.cssSelector(".cart_item")).isEmpty()
                && !driver.findElements(By.cssSelector(".summary_value_label")).isEmpty()
                && !driver.findElements(By.cssSelector(".summary_subtotal_label")).isEmpty()
                && !driver.findElements(By.cssSelector(".summary_tax_label")).isEmpty()
                && !driver.findElements(By.cssSelector(".summary_total_label")).isEmpty();
    }

    public void finishOrder() {
        WebElement finish = wait.until(ExpectedConditions.visibilityOfElementLocated(finishButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", finish);
    }

    private void setCheckoutFieldValues(String firstName, String lastName, String postalCode) {
        WebElement firstNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        WebElement lastNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField));
        WebElement postalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(postalCodeField));
        String script = """
                const setValue = (element, value) => {
                    const setter = Object.getOwnPropertyDescriptor(HTMLInputElement.prototype, 'value').set;
                    setter.call(element, value || '');
                    element.dispatchEvent(new Event('input', { bubbles: true }));
                    element.dispatchEvent(new Event('change', { bubbles: true }));
                };
                setValue(arguments[0], arguments[3]);
                setValue(arguments[1], arguments[4]);
                setValue(arguments[2], arguments[5]);
                """;
        ((JavascriptExecutor) driver).executeScript(script, firstNameElement, lastNameElement, postalElement, firstName, lastName, postalCode);
    }
}
