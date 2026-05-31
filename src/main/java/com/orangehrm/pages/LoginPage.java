package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final String PAGE_PATH = "/web/index.php/auth/login";

    private final By usernameInput = By.xpath("//input[@placeholder='Username']");
    private final By passwordInput = By.xpath("//input[@placeholder='Password']");
    private final By loginButton = By.xpath("//button[@type='submit']");
    private final By errorAlert = By.cssSelector(".oxd-alert-content-text");
    private final By fieldError = By.cssSelector(".oxd-input-field-error-message");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        navigate(PAGE_PATH);
        waitForPageReady(usernameInput);
    }

    public void login(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
    }

    public String getErrorAlertText() {
        return getText(errorAlert);
    }

    public String getFieldValidationErrorText() {
        return getText(fieldError);
    }

}
