package com.orangehrm.pages.leave;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ApplyLeavePage extends BasePage {

    private final By leaveTypeLabel = By.xpath("//label[normalize-space()='Leave Type']");
    private final By fromDateInput = By.xpath("(//input[@placeholder='yyyy-dd-mm'])[1]");
    private final By toDateInput = By.xpath("(//input[@placeholder='yyyy-dd-mm'])[2]");
    private final By applyButton = By.xpath("//button[@type='submit'][normalize-space()='Apply']");
    private final By cancelButton = By.xpath("//button[normalize-space()='Cancel']");
    private final By successToast = By.cssSelector(".oxd-toast--success");

    public ApplyLeavePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        waitForPageReady(leaveTypeLabel); // Layer 3 clears oxd-form-loader here
        return true;
    }

    public void selectLeaveType(String leaveType) {
        isLoaded();
        selectFromDropdown(0, leaveType);
    }

    public void setFromDate(String date) {
        setDateField(fromDateInput, date);
    }

    public void setToDate(String date) {
        setDateField(toDateInput, date);
    }

    public void apply() {
        click(applyButton);
        waitForVisible(successToast);
    }

    public void cancel() {
        click(cancelButton);
        waitForUrlContains("viewLeaveList");
    }

    public String getSuccessMessage() {
        return getText(successToast);
    }

    private void setDateField(By locator, String date) {
        WebElement input = waitForVisible(locator);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(date);
        input.sendKeys(Keys.TAB);
        wait.until(d -> {
            String val = input.getAttribute("value");
            return val != null && !val.isEmpty() && !val.equals("__-__-____");
        });
    }
}