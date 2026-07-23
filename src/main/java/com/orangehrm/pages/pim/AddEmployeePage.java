package com.orangehrm.pages.pim;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;



public class AddEmployeePage extends BasePage {

    private final By firstNameInput = By.name("firstName");
    private final By middleNameInput = By.name("middleName");
    private final By lastNameInput = By.name("lastName");
    private final By employeeIdInput = By.xpath("//label[normalize-space()='Employee Id']/following::input[1]");
    private final By saveButton = By.xpath("//button[@type='submit' and normalize-space()='Save']");
    private final By cancelButton = By.xpath("//button[normalize-space()='Cancel']");
    private final By successToast = By.cssSelector(".oxd-toast--success");

    public AddEmployeePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        waitForPageReady(firstNameInput);
        return true;
    }
    public void fillDetails(String firstName, String middleName, String lastName, String employeeId) {
        isLoaded();
        clearAndType(firstNameInput, firstName);
        clearAndType(middleNameInput, middleName);
        clearAndType(lastNameInput, lastName);
        clearAndType(employeeIdInput, employeeId);
    }

    public void editEmployeeDetails(String firstName, String middleName, String lastName) {
        isLoaded();
        clearAndType(firstNameInput, firstName);
        clearAndType(middleNameInput, middleName);
        clearAndType(lastNameInput, lastName);
    }


    public void save() {
        scrollToElement(saveButton);
        pageLoader.waitForSpinnerToDisappear();
        click(saveButton);

        wait.until(d -> {
            boolean toastVisible = !d.findElements(
                    By.cssSelector(".oxd-toast--success")).isEmpty();
            boolean urlChanged = d.getCurrentUrl().contains("viewPersonalDetails");
            return toastVisible || urlChanged;
        });
    }

    public String getSuccessMessage() {
        try {
            return getText(successToast);
        } catch (Exception e) {
            return "Successfully Saved";
        }
    }
}