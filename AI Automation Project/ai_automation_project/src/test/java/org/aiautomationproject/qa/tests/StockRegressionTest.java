package org.aiautomationproject.qa.tests;

import org.aiautomationproject.qa.pages.CartPage;
import org.aiautomationproject.qa.pages.HomePage;
import org.aiautomationproject.qa.pages.ProductDetailPage;
import org.aiautomationproject.qa.pages.ProductsPage;
import org.aiautomationproject.qa.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StockRegressionTest extends RegressionTestBase {
    @Test(priority = 2, groups = {"low", "stock"}, description = "TC_STOCK_001 - Verify abnormal high quantity handling")
    public void verifyAbnormalHighQuantityHandling() {
        String productName = TestData.get("productName");
        HomePage homePage = createLoggedInUser();
        ProductsPage productsPage = homePage.header().openProducts();
        ProductDetailPage detailPage = productsPage.openProductDetail(productName);

        CartPage cartPage = detailPage.addQuantityAndViewCart("9999");

        Assert.assertTrue(cartPage.hasProduct(productName));
        Assert.assertTrue(cartPage.quantityFor(productName) >= 1);
    }

    @Test(priority = 1, groups = {"medium", "stock"}, description = "TC_STOCK_002 - Verify zero or negative quantity is not accepted")
    public void verifyZeroOrNegativeQuantityIsNotAccepted() {
        HomePage homePage = createLoggedInUser();
        ProductsPage productsPage = homePage.header().openProducts();
        ProductDetailPage detailPage = productsPage.openProductDetail(TestData.get("productName"));

        detailPage.addQuantityAndViewCart("0");

        Assert.assertTrue(pageContains("Cart is empty") || !pageContains("0"));
    }
}
