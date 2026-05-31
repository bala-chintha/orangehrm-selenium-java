package com.orangehrm.pages.admin;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddUserPage extends BasePage {

    private final By employeeNameInput = By.cssSelector(".oxd-autocomplete-text-input input");

    private final By usernameInput = By.xpath("//label[normalize-space()='Username']" +
            "/ancestor::div[contains(@class,'oxd-input-group')]" + "//input");

    private final By passwordInput = By.xpath("//label[normalize-space()='Password']" +
            "/ancestor::div[contains(@class,'oxd-input-group')]" + "//input");

    private final By confirmPasswordInput = By.xpath("//label[normalize-space()='Confirm Password']" +
            "/ancestor::div[contains(@class,'oxd-input-group')]" + "//input");

    private final By saveButton = By.xpath("//button[@type='submit' and normalize-space()='Save']");
    private final By successToast = By.cssSelector(".oxd-toast--success");

    public AddUserPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        waitForPageReady(employeeNameInput);
        return true;
    }


    public void selectUserRole(String role) {
        selectFromDropdown(0, role);
    }


    public void selectStatus(String status) {
        selectFromDropdown(1, status);
    }

    public void fillEmployeeName(String name) {
        typeInAutocomplete(employeeNameInput, name, name);
    }

    public void fillUsername(String username) {

        waitForPageReady(usernameInput);
        type(usernameInput, username);
    }

    public void fillPassword(String password) {
        type(passwordInput, password);
        type(confirmPasswordInput, password);
    }

    public void save() {
        click(saveButton);
        waitForVisible(successToast);
    }

    public String getSuccessMessage() {
        return getText(successToast);
    }
}