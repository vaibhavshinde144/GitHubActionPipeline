# SauceDemo AI Automation Project Workflow

This document explains how the SauceDemo automation project works end to end, from configuration and test data to browser execution, reports, and debugging.

## 1. Project Purpose

This project automates regression testing for the SauceDemo application:

```text
https://www.saucedemo.com/
```

It is built with:

- Java 17
- Selenium WebDriver
- TestNG
- Maven Surefire
- Apache POI for Excel-driven test data
- Extent Reports for HTML reporting
- WebDriverManager for browser driver setup

The framework supports full regression execution, priority regression execution, and individual test class execution.

## 2. Project Structure

```text
SauceDemoAIAutomation
|-- pom.xml
|-- testng-regression.xml
|-- testng-regression-priority.xml
|-- PROJECT_WORKFLOW.md
|-- src
|   |-- main
|   |   |-- java
|   |   |   |-- base
|   |   |   |   |-- BasePage.java
|   |   |   |   |-- BaseTest.java
|   |   |   |-- pages
|   |   |   |   |-- LoginPage.java
|   |   |   |   |-- ProductsPage.java
|   |   |   |   |-- CartPage.java
|   |   |   |   |-- CheckoutPage.java
|   |   |   |   |-- OrderConfirmationPage.java
|   |   |   |-- utils
|   |   |       |-- browser
|   |   |       |-- config
|   |   |       |-- excel
|   |   |       |-- listener
|   |   |       |-- report
|   |   |       |-- screenshot
|   |-- test
|       |-- java
|       |   |-- com
|       |       |-- saucedemo
|       |           |-- ai
|       |               |-- tests
|       |                   |-- LoginRegressionTest.java
|       |                   |-- ProductCatalogRegressionTest.java
|       |                   |-- CartRegressionTest.java
|       |                   |-- CheckoutRegressionTest.java
|       |                   |-- NavigationSecurityRegressionTest.java
|       |                   |-- UserInterfaceRegressionTest.java
|       |                   |-- SauceDemoTestBase.java
|       |                   |-- TestCaseDataUtil.java
|       |                   |-- TestInputDataUtil.java
|       |-- resources
|           |-- application.properties
|           |-- TestData.xlsx
|           |-- TestInputData.xlsx
|-- test-output
|   |-- ExtentReport.html
|   |-- screenshots
```

## 3. Configuration Flow

Main configuration is stored in:

```text
src/test/resources/application.properties
```

Current supported properties:

```properties
app.url=https://www.saucedemo.com/
browser=chrome
supported.browsers=chrome,edge,firefox
headless=true
explicit.wait.seconds=15
page.load.timeout.seconds=30
window.width=1920
window.height=1080
```

`ConfigReader.java` loads this file from the test classpath. It also allows Maven command-line override using system properties.

Example:

```powershell
mvn test '-Dtest=LoginRegressionTest' '-Dbrowser=edge'
```

The browser value is passed to `DriverManager.java`, which supports:

- `chrome`
- `edge`
- `firefox`

If `headless=true`, tests run without opening a visible browser window.

## 4. Test Data Flow

The framework uses two Excel files.

### 4.1 TestData.xlsx

Location:

```text
src/test/resources/TestData.xlsx
```

Purpose:

- Stores detailed manual/automation test cases.
- Stores regression priority mapping.
- Drives TestNG data providers.

Important sheets:

- `Detailed Test Cases`
- `Regression Priority`

Expected key columns include:

- `Scenario ID`
- `Scenario Description`
- `Test Case ID`
- `Test Case Description`
- `Priority`
- `Type`
- `Test Steps`
- `Test Data`
- `Expected Result`

`TestCaseDataUtil.java` reads this workbook and filters rows by scenario or priority.

### 4.2 TestInputData.xlsx

Location:

```text
src/test/resources/TestInputData.xlsx
```

Purpose:

- Stores reusable runtime input values.
- Avoids hard-coded usernames, passwords, products, and checkout details in tests.

Important sheets:

- `Credentials`
- `Products`
- `Checkout`

Example logical data:

```text
Credentials:
userType, username, password

Products:
dataSet, productName

Checkout:
dataSet, firstName, lastName, pincode
```

`TestInputDataUtil.java` reads this workbook and validates required values. If required data is missing, the test fails with a clear error.

## 5. Page Object Model

The framework uses the Page Object Model pattern.

### BasePage.java

Common parent for page classes.

Responsibilities:

- Stores `WebDriver`
- Creates `WebDriverWait`
- Reads explicit wait timeout from `application.properties`

### LoginPage.java

Handles login page actions and validations:

- Enter username
- Enter password
- Click login
- Validate login errors
- Validate login page layout

### ProductsPage.java

Handles inventory/product catalog features:

- Product listing checks
- Add/remove products
- Cart badge validation
- Product sorting
- Product detail navigation
- Menu actions
- Logout and reset app state

### CartPage.java

Handles cart page features:

- Validate products in cart
- Remove products
- Continue shopping
- Start checkout

### CheckoutPage.java

Handles checkout information and overview:

- Enter customer details
- Validate checkout errors
- Continue to overview
- Finish order
- Cancel checkout

### OrderConfirmationPage.java

Handles order confirmation:

- Validate success message
- Click Back Home

## 6. Test Class Design

All test classes are product-area based and live under:

```text
src/test/java/com/saucedemo/ai/tests
```

The package is:

```java
package com.saucedemo.ai.tests;
```

This folder/package alignment is important for VS Code and Java test discovery.

Current test classes:

- `LoginRegressionTest`
- `ProductCatalogRegressionTest`
- `CartRegressionTest`
- `CheckoutRegressionTest`
- `NavigationSecurityRegressionTest`
- `UserInterfaceRegressionTest`

Each class contains:

- One regression data provider
- One priority data provider
- One `@Test` method with group `regression`
- One `@Test` method with group `priority`

Example pattern:

```java
@Test(dataProvider = "loginRegressionCases", groups = {"regression"})
public void verifyLoginValidation(Map<String, String> testCase) {
    executeTestCase(testCase);
}

@Test(dataProvider = "loginPriorityCases", groups = {"priority"})
public void verifyPriorityLoginValidation(Map<String, String> testCase) {
    executeTestCase(testCase);
}
```

The actual test case execution is centralized in:

```text
SauceDemoTestBase.java
```

It reads `Test Case ID` from Excel and executes the matching automation flow.

## 7. Base Test Execution Flow

All test classes extend:

```text
BaseTest.java
```

For each test method, this flow happens:

1. `@BeforeSuite`
   - Initializes Extent Report.

2. `@BeforeMethod`
   - Creates browser using `DriverManager`.
   - Applies page load timeout.
   - Maximizes browser window.
   - Opens `app.url`.
   - Creates Extent test entry.
   - Logs Excel test case details:
     - Scenario
     - Priority
     - Type
     - Test steps
     - Test data
     - Expected result

3. Test method runs.
   - Data comes from Excel.
   - Test case ID is passed to `SauceDemoTestBase.executeTestCase`.
   - Page objects perform Selenium actions.
   - TestNG assertions validate expected behavior.

4. `TestListener`
   - Logs result as PASS, FAIL, or SKIP.
   - Captures screenshot on failure.
   - Attaches screenshot to Extent Report.

5. `@AfterMethod`
   - Quits browser.

6. `@AfterSuite`
   - Flushes Extent Report to HTML.

## 8. Regression Suite

Suite file:

```text
testng-regression.xml
```

Purpose:

- Runs all test cases marked with group `regression`.
- Includes all product-area test classes.

Command:

```powershell
mvn test '-Dsurefire.suiteXmlFiles=testng-regression.xml'
```

Expected result after latest verification:

```text
Tests run: 54, Failures: 0, Errors: 0, Skipped: 0
```

## 9. Priority Regression Suite

Suite file:

```text
testng-regression-priority.xml
```

Purpose:

- Runs only priority test cases.
- Uses group `priority`.
- Priority test case IDs come from the `Regression Priority` sheet in `TestData.xlsx`.

Command:

```powershell
mvn test '-Dsurefire.suiteXmlFiles=testng-regression-priority.xml'
```

Expected result after latest verification:

```text
Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
```

## 10. Individual Class Execution

You can run one Java test class from Maven.

Examples:

```powershell
mvn test '-Dtest=LoginRegressionTest'
mvn test '-Dtest=CartRegressionTest'
mvn test '-Dtest=CheckoutRegressionTest'
```

You can also run from VS Code using the Run/Test buttons on the Java test class.

Important:

- Test files must remain under `src/test/java/com/saucedemo/ai/tests`.
- Their package must remain `com.saucedemo.ai.tests`.
- This alignment helps VS Code discover and run individual classes correctly.

## 11. Browser Execution

Browser creation happens in:

```text
src/main/java/utils/browser/DriverManager.java
```

Execution flow:

1. Reads browser name from `application.properties`.
2. Validates it against `supported.browsers`.
3. Uses WebDriverManager to set up the driver.
4. Applies browser options.
5. Applies headless mode if enabled.
6. Returns the Selenium `WebDriver` instance.

To change browser:

```properties
browser=edge
```

or override from command line:

```powershell
mvn test '-Dtest=LoginRegressionTest' '-Dbrowser=firefox'
```

To run with visible browser:

```properties
headless=false
```

or:

```powershell
mvn test '-Dtest=LoginRegressionTest' '-Dheadless=false'
```

## 12. Reporting

Extent report output:

```text
test-output/ExtentReport.html
```

The report includes:

- Test case ID
- Test case description
- Scenario
- Priority
- Type
- Test steps
- Test data
- Expected result
- Execution started message
- Result: PASS, FAIL, or SKIP
- Failure stack trace when applicable
- Failure screenshot when applicable

Failure screenshots are saved in:

```text
test-output/screenshots
```

## 13. TestNG/Surefire Reports

Maven Surefire and TestNG reports are generated under:

```text
target/surefire-reports
```

Useful files:

- `testng-results.xml`
- `emailable-report.html`
- `index.html`
- `TEST-*.xml`

These are useful for CI/CD tools and debugging command-line execution.

## 14. How to Add a New Test Case

Follow this process when adding a new automation test case.

1. Add the test case in `TestData.xlsx`.
   - Add row in `Detailed Test Cases`.
   - Fill `Scenario ID`, `Test Case ID`, steps, data, expected result, and priority.

2. If it is a priority case, add its `Test Case ID` in the `Regression Priority` sheet.

3. If new runtime input is needed, add it in `TestInputData.xlsx`.
   - Credentials go in `Credentials`.
   - Products go in `Products`.
   - Checkout users go in `Checkout`.

4. Add or update page object methods if the UI action is new.

5. Add mapping in `SauceDemoTestBase.executeTestCase`.

Example:

```java
case "TC_055" -> yourNewAutomationMethod(testCase);
```

6. Make sure the correct product-area test class data provider includes the scenario or test case ID.

7. Run individual class first:

```powershell
mvn test '-Dtest=ProductCatalogRegressionTest'
```

8. Run full regression:

```powershell
mvn test '-Dsurefire.suiteXmlFiles=testng-regression.xml'
```

## 15. How to Debug Failures

Use this order:

1. Check Maven console summary.

```text
Tests run, Failures, Errors, Skipped
```

2. Open Extent Report.

```text
test-output/ExtentReport.html
```

3. Check failed test details.
   - Test case ID
   - Test steps
   - Test data
   - Expected result
   - Stack trace
   - Attached screenshot

4. Check screenshot.

```text
test-output/screenshots
```

5. Run only the failed class.

```powershell
mvn test '-Dtest=CheckoutRegressionTest'
```

6. Run visible browser if needed.

```powershell
mvn test '-Dtest=CheckoutRegressionTest' '-Dheadless=false'
```

7. If failure is data related, verify:
   - `TestData.xlsx`
   - `TestInputData.xlsx`

8. If failure is browser related, verify:
   - `application.properties`
   - `DriverManager.java`
   - browser version
   - Selenium/WebDriverManager dependencies

## 16. VS Code Usage

Recommended steps:

1. Open the project root:

```text
D:\GitRepo\AI Automation Project\SauceDemoAIAutomation
```

2. Let VS Code import the Maven project.

3. Confirm Java extension recognizes test classes under:

```text
src/test/java/com/saucedemo/ai/tests
```

4. Run a single class using VS Code test runner, or run Maven commands in terminal.

5. For suite execution, prefer Maven terminal commands:

```powershell
mvn test '-Dsurefire.suiteXmlFiles=testng-regression.xml'
mvn test '-Dsurefire.suiteXmlFiles=testng-regression-priority.xml'
```

## 17. Common Issues and Fixes

### Issue: Individual class does not run in VS Code

Check that the file path matches the package:

```text
src/test/java/com/saucedemo/ai/tests/LoginRegressionTest.java
```

```java
package com.saucedemo.ai.tests;
```

### Issue: Browser does not start

Check:

- `browser` value is one of `chrome`, `edge`, or `firefox`.
- Browser is installed locally.
- WebDriverManager can download or resolve driver.

### Issue: Test data is blank or missing

Check:

- Correct sheet name.
- Correct column name.
- Required value is not empty.
- Excel file is saved and located in `src/test/resources`.

### Issue: Suite runs different tests than expected

Check the group in XML:

```xml
<include name="regression"/>
```

or:

```xml
<include name="priority"/>
```

Then check the `groups` value in the test annotation.

### Issue: Report not updated

Clean and rerun:

```powershell
mvn clean test '-Dsurefire.suiteXmlFiles=testng-regression.xml'
```

Then reopen:

```text
test-output/ExtentReport.html
```

## 18. Standard Execution Commands

Compile only:

```powershell
mvn clean test -DskipTests
```

Run full regression:

```powershell
mvn clean test '-Dsurefire.suiteXmlFiles=testng-regression.xml'
```

Run priority regression:

```powershell
mvn test '-Dsurefire.suiteXmlFiles=testng-regression-priority.xml'
```

Run one class:

```powershell
mvn test '-Dtest=LoginRegressionTest'
```

Run one class with visible browser:

```powershell
mvn test '-Dtest=LoginRegressionTest' '-Dheadless=false'
```

Run one class on Edge:

```powershell
mvn test '-Dtest=LoginRegressionTest' '-Dbrowser=edge'
```

## 19. End-to-End Execution Summary

This is the complete flow when a suite runs:

1. Maven starts Surefire.
2. Surefire loads TestNG.
3. TestNG reads the selected XML suite.
4. XML suite includes either `regression` or `priority` group.
5. TestNG loads all product-area test classes.
6. Data providers read test cases from Excel.
7. `BaseTest` starts Extent Report and browser.
8. Browser opens SauceDemo URL.
9. Test case ID is sent to `SauceDemoTestBase`.
10. `SauceDemoTestBase` executes the mapped business flow.
11. Page objects perform Selenium actions.
12. TestNG assertions validate actual result.
13. Listener logs PASS, FAIL, or SKIP.
14. Screenshot is captured for failures.
15. Browser closes after each test.
16. Extent Report is written after the suite completes.

## 20. Latest Verified Status

Latest verified commands:

```powershell
mvn clean test '-Dtest=LoginRegressionTest'
mvn test '-Dsurefire.suiteXmlFiles=testng-regression-priority.xml'
```

Verified results:

```text
LoginRegressionTest: Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
Priority Regression Suite: Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
```

The framework is ready for individual test class execution, priority regression execution, and full regression execution.
