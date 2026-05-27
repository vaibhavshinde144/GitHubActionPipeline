package com.saucedemo.ai.tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.SkipException;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.OrderConfirmationPage;
import pages.ProductsPage;

import java.util.List;
import java.util.Map;

abstract class SauceDemoTestBase extends BaseTest {
    private static final String TEST_CASE_ID = "Test Case ID";

    protected void executeTestCase(Map<String, String> testCase) {
        String testCaseId = testCase.get(TEST_CASE_ID);
        switch (testCaseId) {
            case "TC_001" -> loginWithCredentials(defaultUsername(), defaultPassword(), true, "");
            case "TC_002" -> loginWithCredentials(invalidPasswordUsername(), invalidPassword(), false, "Username and password do not match");
            case "TC_003" -> loginWithCredentials("", "", false, "Username is required");
            case "TC_004" -> loginWithCredentials(defaultUsername(), "", false, "Password is required");
            case "TC_005" -> loginWithCredentials(lockedUsername(), lockedPassword(), false, "locked out");
            case "TC_006" -> Assert.assertEquals(loginAndOpenProducts().getPageTitleText(), "Products");
            case "TC_007" -> Assert.assertTrue(loginAndOpenProducts().areProductCardsComplete(), "Product cards should show complete content.");
            case "TC_008" -> Assert.assertTrue(loginAndOpenProducts().isCartIconVisible(), "Cart icon should be visible.");
            case "TC_009" -> Assert.assertTrue(loginAndOpenProducts().arePricesVisibleAndFormatted(), "All prices should use currency format.");
            case "TC_010" -> addProductsAndAssertCartCount(1);
            case "TC_011" -> addProductsAndAssertCartCount(3);
            case "TC_012" -> removeProductFromProductsPage();
            case "TC_013" -> openCartAfterAddingProduct();
            case "TC_014" -> removeProductFromCartPage();
            case "TC_015" -> continueShoppingFromCart();
            case "TC_016" -> proceedToCheckoutWithProduct();
            case "TC_017" -> checkoutWithCustomerDetails(testCase, true, "");
            case "TC_018" -> checkoutWithCustomerDetails(testCase, false, "First Name is required");
            case "TC_019" -> checkoutWithCustomerDetails(testCase, false, "Last Name is required");
            case "TC_020" -> checkoutWithCustomerDetails(testCase, false, "Postal Code is required");
            case "TC_021" -> cancelCheckoutInformationPage();
            case "TC_022" -> Assert.assertTrue(openCheckoutOverview(testCase).areOverviewDetailsVisible(), "Checkout overview details should be visible.");
            case "TC_023" -> completeOrderSuccessfully(testCase, false);
            case "TC_024" -> completeOrderSuccessfully(testCase, true);
            case "TC_025" -> assertSort("Name (A to Z)", SortExpectation.NAME_ASC);
            case "TC_026" -> assertSort("Name (Z to A)", SortExpectation.NAME_DESC);
            case "TC_027" -> assertSort("Price (low to high)", SortExpectation.PRICE_ASC);
            case "TC_028" -> assertSort("Price (high to low)", SortExpectation.PRICE_DESC);
            case "TC_029" -> checkoutWithEmptyCart();
            case "TC_030" -> removeAllProductsFromCart();
            case "TC_031" -> refreshAfterAddingProduct();
            case "TC_032" -> browserBackAfterCheckoutStep();
            case "TC_033" -> addAndRemoveSameProductRepeatedly();
            case "TC_034" -> openProductDetailPage();
            case "TC_035" -> Assert.assertTrue(openProductDetail().isProductDetailContentVisible(), "Product detail content should be visible.");
            case "TC_036" -> addProductFromDetailPage();
            case "TC_037" -> backToProductsFromDetailPage();
            case "TC_038" -> Assert.assertTrue(openMenu().isSideMenuOpen(), "Side menu should open.");
            case "TC_039" -> resetAppStateFromMenu();
            case "TC_040" -> logoutFromApplication();
            case "TC_041" -> navigateBetweenProductsAndCartPages();
            case "TC_042" -> refreshAfterLogin();
            case "TC_043" -> directProductsUrlAfterLogout();
            case "TC_044" -> cartStateAfterRefreshOnCartPage();
            case "TC_045" -> assertDirectUrlRequiresLogin("inventory.html");
            case "TC_046" -> assertDirectUrlRequiresLogin("cart.html");
            case "TC_047" -> assertDirectUrlRequiresLogin("checkout-step-one.html");
            case "TC_048" -> assertPasswordMasking();
            case "TC_049" -> Assert.assertTrue(new LoginPage(driver).isLogoVisible(), "Login logo should be visible.");
            case "TC_050" -> Assert.assertTrue(new LoginPage(driver).isLoginButtonAlignedWithForm(), "Login button should align with form controls.");
            case "TC_051" -> assertLoginErrorStyle();
            case "TC_052" -> assertResponsiveLayout();
            case "TC_053" -> Assert.assertTrue(loginAndOpenProducts().areProductImagesAligned(), "Product images should be aligned.");
            case "TC_054" -> Assert.assertTrue(loginAndOpenProducts().areButtonTextsConsistent(), "Button text should be consistent.");
            default -> throw new SkipException("No automation mapping found for " + testCaseId);
        }
    }

    private void loginWithCredentials(String username, String password, boolean valid, String expectedMessage) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
        if (valid) {
            Assert.assertTrue(loginPage.isProductPageDisplayed(), "Expected Products page after successful login.");
        } else {
            Assert.assertTrue(loginPage.isLoginErrorMessageDisplayed(), "Expected login validation message.");
            Assert.assertTrue(loginPage.getLoginErrorMessage().contains(expectedMessage), "Unexpected login validation message.");
        }
    }

    private ProductsPage loginAndOpenProducts() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(defaultUsername(), defaultPassword());
        Assert.assertTrue(loginPage.isProductPageDisplayed(), "Login must succeed before Products page checks.");
        return new ProductsPage(driver);
    }

    private void addProductsAndAssertCartCount(int productCount) {
        ProductsPage productsPage = loginAndOpenProducts();
        List<String> products = configuredProducts();
        for (int i = 0; i < productCount; i++) {
            productsPage.addProductToCart(products.get(i));
        }
        Assert.assertEquals(productsPage.getCartBadgeCount(), String.valueOf(productCount));
    }

    private void removeProductFromProductsPage() {
        ProductsPage productsPage = loginAndOpenProducts();
        String product = firstProduct(productsPage);
        productsPage.addProductToCart(product);
        productsPage.removeProductFromProductsPage(product);
        Assert.assertEquals(productsPage.getCartBadgeCount(), "0");
    }

    private void openCartAfterAddingProduct() {
        ProductsPage productsPage = loginAndOpenProducts();
        String product = firstProduct(productsPage);
        productsPage.addProductToCart(product);
        productsPage.openCart();
        Assert.assertTrue(new CartPage(driver).isProductInCart(product));
    }

    private void removeProductFromCartPage() {
        ProductsPage productsPage = loginAndOpenProducts();
        String product = firstProduct(productsPage);
        productsPage.addProductToCart(product);
        productsPage.openCart();
        CartPage cartPage = new CartPage(driver);
        cartPage.removeProduct(product);
        Assert.assertEquals(cartPage.getCartItemCount(), 0);
    }

    private void continueShoppingFromCart() {
        ProductsPage productsPage = loginAndOpenProducts();
        productsPage.openCart();
        new CartPage(driver).continueShopping();
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
    }

    private void proceedToCheckoutWithProduct() {
        ProductsPage productsPage = loginAndOpenProducts();
        productsPage.addProductToCart(firstProduct(productsPage));
        productsPage.openCart();
        new CartPage(driver).clickCheckout();
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one"));
    }

    private void checkoutWithCustomerDetails(Map<String, String> testCase, boolean valid, String expectedMessage) {
        CheckoutPage checkoutPage = openCheckoutInformationPage();
        Customer customer = customerFor(testCase);
        checkoutPage.enterCustomerInformation(customer.firstName(), customer.lastName(), customer.postalCode());
        if (valid) {
            Assert.assertTrue(checkoutPage.isOverviewDisplayed(), "Checkout overview should be displayed.");
        } else {
            Assert.assertTrue(checkoutPage.getCheckoutErrorMessage().contains(expectedMessage), "Unexpected checkout validation message.");
        }
    }

    private void cancelCheckoutInformationPage() {
        CheckoutPage checkoutPage = openCheckoutInformationPage();
        checkoutPage.cancel();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"));
    }

    private CheckoutPage openCheckoutInformationPage() {
        ProductsPage productsPage = loginAndOpenProducts();
        productsPage.addProductToCart(firstProduct(productsPage));
        productsPage.openCart();
        new CartPage(driver).clickCheckout();
        return new CheckoutPage(driver);
    }

    private CheckoutPage openCheckoutOverview(Map<String, String> testCase) {
        CheckoutPage checkoutPage = openCheckoutInformationPage();
        Customer customer = customerFor(testCase);
        checkoutPage.enterCustomerInformation(customer.firstName(), customer.lastName(), customer.postalCode());
        Assert.assertTrue(checkoutPage.isOverviewDisplayed(), "Checkout overview should be displayed.");
        return checkoutPage;
    }

    private void completeOrderSuccessfully(Map<String, String> testCase, boolean backHome) {
        CheckoutPage checkoutPage = openCheckoutOverview(testCase);
        checkoutPage.finishOrder();
        OrderConfirmationPage confirmationPage = new OrderConfirmationPage(driver);
        Assert.assertEquals(confirmationPage.getOrderSuccessMessage(), "Thank you for your order!");
        if (backHome) {
            confirmationPage.clickBackHome();
            Assert.assertEquals(new ProductsPage(driver).getPageTitleText(), "Products");
        }
    }

    private void assertSort(String visibleText, SortExpectation expectation) {
        ProductsPage productsPage = loginAndOpenProducts();
        productsPage.sortProductsByVisibleText(visibleText);
        boolean sorted = switch (expectation) {
            case NAME_ASC -> productsPage.areNamesSortedAscending();
            case NAME_DESC -> productsPage.areNamesSortedDescending();
            case PRICE_ASC -> productsPage.arePricesSortedAscending();
            case PRICE_DESC -> productsPage.arePricesSortedDescending();
        };
        Assert.assertTrue(sorted, "Products should be sorted by " + visibleText);
    }

    private void checkoutWithEmptyCart() {
        ProductsPage productsPage = loginAndOpenProducts();
        productsPage.openCart();
        Assert.assertEquals(new CartPage(driver).getCartItemCount(), 0);
    }

    private void removeAllProductsFromCart() {
        ProductsPage productsPage = loginAndOpenProducts();
        List<String> products = productsPage.getProductNames().stream().limit(3).toList();
        products.forEach(productsPage::addProductToCart);
        productsPage.openCart();
        CartPage cartPage = new CartPage(driver);
        products.forEach(cartPage::removeProduct);
        Assert.assertEquals(cartPage.getCartItemCount(), 0);
    }

    private void refreshAfterAddingProduct() {
        ProductsPage productsPage = loginAndOpenProducts();
        productsPage.addProductToCart(firstProduct(productsPage));
        driver.navigate().refresh();
        Assert.assertEquals(new ProductsPage(driver).getCartBadgeCount(), "1");
    }

    private void browserBackAfterCheckoutStep() {
        openCheckoutInformationPage();
        driver.navigate().back();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"));
    }

    private void addAndRemoveSameProductRepeatedly() {
        ProductsPage productsPage = loginAndOpenProducts();
        String product = firstProduct(productsPage);
        for (int i = 0; i < 3; i++) {
            productsPage.addProductToCart(product);
            Assert.assertEquals(productsPage.getCartBadgeCount(), "1");
            productsPage.removeProductFromProductsPage(product);
            Assert.assertEquals(productsPage.getCartBadgeCount(), "0");
        }
    }

    private void openProductDetailPage() {
        ProductsPage productsPage = openProductDetail();
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory-item"));
        Assert.assertTrue(productsPage.isProductDetailContentVisible());
    }

    private ProductsPage openProductDetail() {
        ProductsPage productsPage = loginAndOpenProducts();
        productsPage.openProductDetail(firstProduct(productsPage));
        return productsPage;
    }

    private void addProductFromDetailPage() {
        ProductsPage productsPage = openProductDetail();
        productsPage.addProductFromDetailPage();
        Assert.assertEquals(productsPage.getCartBadgeCount(), "1");
    }

    private void backToProductsFromDetailPage() {
        ProductsPage productsPage = openProductDetail();
        productsPage.backToProducts();
        Assert.assertEquals(productsPage.getPageTitleText(), "Products");
    }

    private ProductsPage openMenu() {
        ProductsPage productsPage = loginAndOpenProducts();
        productsPage.openMenu();
        return productsPage;
    }

    private void resetAppStateFromMenu() {
        ProductsPage productsPage = loginAndOpenProducts();
        productsPage.addProductToCart(firstProduct(productsPage));
        productsPage.resetAppState();
        driver.navigate().refresh();
        Assert.assertEquals(new ProductsPage(driver).getCartBadgeCount(), "0");
    }

    private void logoutFromApplication() {
        ProductsPage productsPage = loginAndOpenProducts();
        productsPage.logout();
        Assert.assertTrue(new LoginPage(driver).isLoginPageDisplayed());
    }

    private void navigateBetweenProductsAndCartPages() {
        ProductsPage productsPage = loginAndOpenProducts();
        productsPage.openCart();
        new CartPage(driver).continueShopping();
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
    }

    private void refreshAfterLogin() {
        loginAndOpenProducts();
        driver.navigate().refresh();
        Assert.assertEquals(new ProductsPage(driver).getPageTitleText(), "Products");
    }

    private void directProductsUrlAfterLogout() {
        ProductsPage productsPage = loginAndOpenProducts();
        productsPage.logout();
        driver.get(appUrl("inventory.html"));
        Assert.assertTrue(new LoginPage(driver).isLoginPageDisplayed());
    }

    private void cartStateAfterRefreshOnCartPage() {
        ProductsPage productsPage = loginAndOpenProducts();
        String product = firstProduct(productsPage);
        productsPage.addProductToCart(product);
        productsPage.openCart();
        driver.navigate().refresh();
        Assert.assertTrue(new CartPage(driver).isProductInCart(product));
    }

    private void assertDirectUrlRequiresLogin(String path) {
        driver.get(appUrl(path));
        Assert.assertTrue(new LoginPage(driver).isLoginPageDisplayed(), "Direct URL should require login: " + path);
    }

    private void assertPasswordMasking() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.typePassword(defaultPassword());
        Assert.assertTrue(loginPage.isPasswordMasked(), "Password field should be masked.");
    }

    private void assertLoginErrorStyle() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(invalidPasswordUsername(), invalidPassword());
        Assert.assertTrue(loginPage.isLoginErrorMessageDisplayed());
        Assert.assertFalse(loginPage.getErrorMessageColor().isBlank(), "Error message should expose styling.");
    }

    private void assertResponsiveLayout() {
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(390, 844));
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLogoVisible());
        loginPage.login(defaultUsername(), defaultPassword());
        Assert.assertTrue(new ProductsPage(driver).isCartIconVisible());
    }

    private Customer customerFor(Map<String, String> testCase) {
        Customer validCustomer = parseCustomer(testCase.get("Test Data"));
        return switch (testCase.get(TEST_CASE_ID)) {
            case "TC_018" -> new Customer("", validCustomer.lastName(), validCustomer.postalCode());
            case "TC_019" -> new Customer(validCustomer.firstName(), "", validCustomer.postalCode());
            case "TC_020" -> new Customer(validCustomer.firstName(), validCustomer.lastName(), "");
            default -> validCustomer;
        };
    }

    private Customer parseCustomer(String testData) {
        String[] parts = testData == null ? new String[0] : testData.split("/");
        if (parts.length >= 3) {
            return new Customer(parts[0].trim(), parts[1].trim(), parts[2].trim());
        }
        return new Customer(
                TestInputDataUtil.customer("valid").firstName(),
                TestInputDataUtil.customer("valid").lastName(),
                TestInputDataUtil.customer("valid").pincode()
        );
    }

    private String firstProduct(ProductsPage productsPage) {
        return TestInputDataUtil.productName("primary");
    }

    private List<String> configuredProducts() {
        return TestInputDataUtil.productNames();
    }

    private String defaultUsername() {
        return TestInputDataUtil.username("standard");
    }

    private String defaultPassword() {
        return TestInputDataUtil.password("standard");
    }

    private String invalidPasswordUsername() {
        return TestInputDataUtil.username("invalidPassword");
    }

    private String invalidPassword() {
        return TestInputDataUtil.password("invalidPassword");
    }

    private String lockedUsername() {
        return TestInputDataUtil.username("locked");
    }

    private String lockedPassword() {
        return TestInputDataUtil.password("locked");
    }

    private String appUrl(String path) {
        return utils.config.ConfigReader.get("app.url") + path;
    }

    private record Customer(String firstName, String lastName, String postalCode) {
    }

    private enum SortExpectation {
        NAME_ASC,
        NAME_DESC,
        PRICE_ASC,
        PRICE_DESC
    }
}
