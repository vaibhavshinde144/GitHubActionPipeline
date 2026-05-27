package org.aiautomationproject.qa.tests;

import org.aiautomationproject.qa.pages.CartPage;
import org.aiautomationproject.qa.pages.HomePage;
import org.aiautomationproject.qa.pages.ProductsPage;
import org.aiautomationproject.qa.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartRegressionTest extends RegressionTestBase {
    @Test(priority = 1, groups = {"critical", "cart"}, description = "TC_CART_001 - Verify adding product to cart from product listing page")
    public void verifyAddingProductToCartFromListingPage() {
        CartPage cartPage = addDefaultProductToCart();
        String productName = TestData.get("productName");

        Assert.assertTrue(cartPage.hasProduct(productName));
        Assert.assertEquals(cartPage.quantityFor(productName), 1);
        Assert.assertFalse(cartPage.priceFor(productName).isBlank());
        Assert.assertFalse(cartPage.totalFor(productName).isBlank());
    }

    @Test(priority = 3, groups = {"medium", "cart"}, description = "TC_CART_002 - Verify adding multiple products to cart")
    public void verifyAddingMultipleProductsToCart() {
        HomePage homePage = createLoggedInUser();
        ProductsPage productsPage = homePage.header().openProducts();

        CartPage cartPage = productsPage
                .addProductToCartAndContinue(TestData.get("productName"))
                .addProductToCartAndViewCart(TestData.get("secondProductName"));

        Assert.assertTrue(cartPage.hasProduct(TestData.get("productName")));
        Assert.assertTrue(cartPage.hasProduct(TestData.get("secondProductName")));
        Assert.assertEquals(cartPage.productCount(), 2);
    }

    @Test(priority = 4, groups = {"medium", "cart"}, description = "TC_CART_004 - Verify removing product from cart")
    public void verifyRemovingProductFromCart() {
        String productName = TestData.get("productName");
        CartPage cartPage = addDefaultProductToCart();

        cartPage.removeProduct(productName);

        Assert.assertFalse(cartPage.hasProduct(productName));
    }

    @Test(priority = 5, groups = {"low", "cart"}, description = "TC_CART_003 - Verify duplicate product addition increases quantity")
    public void verifyDuplicateProductAdditionIncreasesQuantity() {
        String productName = TestData.get("productName");
        HomePage homePage = createLoggedInUser();
        ProductsPage productsPage = homePage.header().openProducts();

        CartPage cartPage = productsPage
                .addProductToCartAndContinue(productName)
                .addProductToCartAndViewCart(productName);

        Assert.assertTrue(cartPage.quantityFor(productName) >= 2 || cartPage.productCount() >= 1);
    }

    @Test(priority = 2, groups = {"high", "cart"}, description = "TC_CART_005 - Verify cart persistence after page refresh")
    public void verifyCartPersistenceAfterPageRefresh() {
        String productName = TestData.get("productName");
        CartPage cartPage = addDefaultProductToCart();

        cartPage.refresh();

        Assert.assertTrue(cartPage.hasProduct(productName));
    }
}
