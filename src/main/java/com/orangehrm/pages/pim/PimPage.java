package com.orangehrm.pages.pim;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PimPage extends BasePage {

    private static final String PAGE_PATH = "/web/index.php/pim/viewEmployeeList";

    private final By addButton = By.xpath("//button[normalize-space()='Add']");
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");
    private final By resetButton = By.xpath("//button[normalize-space()='Reset']");
    private final By employeeNameInput = By.cssSelector(".oxd-autocomplete-text-input input");
    private final By tableRows = By.cssSelector(".oxd-table-row--clickable");
    private final By noRecordsText = By.xpath("//span[normalize-space()='No Records Found']");
    private final By deleteConfirmButton = By.xpath("//button[normalize-space()='Yes, Delete']");

    public PimPage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        navigate(PAGE_PATH);
        waitForPageReady(addButton);
    }

    public void clickAdd() {
        click(addButton);
        waitForUrlContains("addEmployee");
    }

    public void searchByName(String name) {
        typeInPlainInput(employeeNameInput, name);
        click(searchButton);
        waitForSearchResults();
    }

    public void resetSearch() {
        click(resetButton);
        waitForSearchResults();
    }

    private void waitForSearchResults() {

        waitForEither(tableRows, noRecordsText);
    }

    public int getRowCount() {
        waitForSearchResults();
        return countElements(tableRows);
    }

    public boolean isNoRecordsFound() {
        return isVisible(noRecordsText);
    }

    public void clickEditOnRow(int index) {
        By editBtn = By.xpath("(//div[contains(@class,'oxd-table-row--clickable')])"
                + "[" + (index + 1) + "]//button[1]");
        click(editBtn);
        waitForUrlContains("viewPersonalDetails");
    }

    public void clickDeleteOnRow(int index) {
        By deleteBtn = By.xpath("(//div[contains(@class,'oxd-table-row--clickable')])"
                + "[" + (index + 1) + "]//button[2]");
        click(deleteBtn);
        waitForVisible(deleteConfirmButton);
    }

    public void confirmDelete() {
        click(deleteConfirmButton);
        waitForVisible(noRecordsText);
    }
}
