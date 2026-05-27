package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class ProductsPage extends BasePage {
    private final By inventoryContainer = By.id("inventory_container");
    private final By pageTitle = By.cssSelector("span.title");
    private final By inventoryItems = By.cssSelector("div.inventory_item");
    private final By productNames = By.cssSelector("div.inventory_item_name");
    private final By productPrices = By.cssSelector("div.inventory_item_price");
    private final By sortDropdown = By.cssSelector("select.product_sort_container");
    private final By cartBadge = By.cssSelector("span.shopping_cart_badge");
    private final By cartIcon = By.cssSelector("a.shopping_cart_link");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By resetAppStateLink = By.id("reset_sidebar_link");
    private final By backToProductsButton = By.id("back-to-products");
    private final Pattern pricePattern = Pattern.compile("^\\$\\d+\\.\\d{2}$");

    public ProductsPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryContainer));
    }

    public void addProductToCart(String productName) {
        By addButton = By.xpath("//div[@class='inventory_item']//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(addButton, "Remove"));
    }

    public void removeProductFromProductsPage(String productName) {
        By removeButton = By.xpath("//div[@class='inventory_item']//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button[text()='Remove']");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(removeButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        By addButton = By.xpath("//div[@class='inventory_item']//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(addButton, "Add to cart"));
    }

    public String getProductButtonText(String productName) {
        By productButton = By.xpath("//div[@class='inventory_item']//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productButton)).getText();
    }

    public String getCartBadgeCount() {
        if (driver.findElements(cartBadge).isEmpty()) {
            return "0";
        }
        return driver.findElement(cartBadge).getText();
    }

    public void openCart() {
        WebElement cart = wait.until(ExpectedConditions.elementToBeClickable(cartIcon));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cart);
        wait.until(ExpectedConditions.urlContains("cart"));
    }

    public String getPageTitleText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public boolean isCartIconVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartIcon)).isDisplayed();
    }

    public int getProductCardCount() {
        return driver.findElements(inventoryItems).size();
    }

    public boolean areProductCardsComplete() {
        for (WebElement item : driver.findElements(inventoryItems)) {
            if (item.findElements(By.cssSelector(".inventory_item_name")).isEmpty()
                    || item.findElements(By.cssSelector(".inventory_item_img img")).isEmpty()
                    || item.findElements(By.cssSelector(".inventory_item_desc")).isEmpty()
                    || item.findElements(By.cssSelector(".inventory_item_price")).isEmpty()
                    || item.findElements(By.cssSelector("button")).isEmpty()) {
                return false;
            }
        }
        return getProductCardCount() > 0;
    }

    public boolean arePricesVisibleAndFormatted() {
        return driver.findElements(productPrices).stream()
                .map(WebElement::getText)
                .allMatch(price -> pricePattern.matcher(price).matches());
    }

    public List<String> getProductNames() {
        return driver.findElements(productNames).stream().map(WebElement::getText).toList();
    }

    public List<Double> getProductPrices() {
        return driver.findElements(productPrices).stream()
                .map(WebElement::getText)
                .map(price -> Double.parseDouble(price.replace("$", "")))
                .toList();
    }

    public void sortProductsByVisibleText(String visibleText) {
        new Select(wait.until(ExpectedConditions.elementToBeClickable(sortDropdown))).selectByVisibleText(visibleText);
    }

    public boolean areNamesSortedAscending() {
        return getProductNames().equals(getProductNames().stream().sorted().toList());
    }

    public boolean areNamesSortedDescending() {
        return getProductNames().equals(getProductNames().stream().sorted(Comparator.reverseOrder()).toList());
    }

    public boolean arePricesSortedAscending() {
        List<Double> prices = getProductPrices();
        return prices.equals(prices.stream().sorted().toList());
    }

    public boolean arePricesSortedDescending() {
        List<Double> prices = getProductPrices();
        return prices.equals(prices.stream().sorted(Comparator.reverseOrder()).toList());
    }

    public void openProductDetail(String productName) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='" + productName + "']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(backToProductsButton));
    }

    public boolean isProductDetailContentVisible() {
        return !driver.findElements(By.cssSelector(".inventory_details_name")).isEmpty()
                && !driver.findElements(By.cssSelector(".inventory_details_desc")).isEmpty()
                && !driver.findElements(By.cssSelector(".inventory_details_price")).isEmpty()
                && !driver.findElements(By.cssSelector(".inventory_details_img")).isEmpty()
                && !driver.findElements(By.cssSelector("button.btn_inventory")).isEmpty();
    }

    public void addProductFromDetailPage() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn_inventory")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("button.btn_inventory"), "Remove"));
    }

    public void backToProducts() {
        WebElement back = wait.until(ExpectedConditions.elementToBeClickable(backToProductsButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", back);
        wait.until(ExpectedConditions.urlContains("inventory"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryContainer));
    }

    public void openMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(logoutLink));
    }

    public boolean isSideMenuOpen() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(logoutLink)).isDisplayed();
    }

    public void logout() {
        openMenu();
        WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logout);
        wait.until(ExpectedConditions.urlToBe(utils.config.ConfigReader.get("app.url")));
    }

    public void resetAppState() {
        openMenu();
        WebElement reset = wait.until(ExpectedConditions.elementToBeClickable(resetAppStateLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", reset);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(cartBadge));
    }

    public boolean areProductImagesAligned() {
        List<WebElement> images = driver.findElements(By.cssSelector(".inventory_item_img"));
        if (images.isEmpty()) {
            return false;
        }
        return images.stream().allMatch(WebElement::isDisplayed);
    }

    public boolean areButtonTextsConsistent() {
        List<String> texts = new ArrayList<>();
        driver.findElements(By.cssSelector(".inventory_item button")).forEach(button -> texts.add(button.getText()));
        return texts.stream().allMatch(text -> text.equals("Add to cart") || text.equals("Remove"));
    }
}
