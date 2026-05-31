package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void openLoginPage() {
        loginPage = pom().getLoginPage();
        loginPage.goTo();
    }

    @Test(groups = {"smoke", "login"}, description = "Valid credentials should redirect to dashboard")
    public void validCredentialsShouldRedirectToDashboard() {
        loginPage.login(config.getAdminUsername(), config.getAdminPassword());

        new org.openqa.selenium.support.ui.WebDriverWait(
                getDriver(), java.time.Duration.ofSeconds(config.getExplicitWait()))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("dashboard"));

        Assert.assertTrue(getDriver().getCurrentUrl().contains("dashboard"),
                "URL should contain 'dashboard' after login. Actual: "
                        + getDriver().getCurrentUrl());

        Assert.assertTrue(pom().getDashboardPage().isDashboardHeadingVisible(),
                "Dashboard heading should be visible after login");
    }

    @Test(groups = {"smoke", "login"}, description = "Invalid credentials should show error alert")
    public void invalidCredentialsShouldShowErrorAlert() {
        loginPage.login("wronguser", "wrongpass");

        String error = loginPage.getErrorAlertText();
        Assert.assertTrue(error.contains("Invalid credentials"),
                "Error message should say 'Invalid credentials', actual: " + error);
    }

    @Test(groups = {"smoke", "login"}, description = "Empty submit should show required field validation")
    public void emptySubmitShouldShowRequiredValidation() {
        loginPage.login("", "");

        String fieldError = loginPage.getFieldValidationErrorText();
        Assert.assertTrue(fieldError.contains("Required"),
                "Field validation should say 'Required', actual: " + fieldError);
    }
}
