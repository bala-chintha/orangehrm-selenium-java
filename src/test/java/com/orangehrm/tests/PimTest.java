package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.pim.AddEmployeePage;
import com.orangehrm.pages.pim.PimPage;
import com.orangehrm.testdata.EmployeeData;
import com.orangehrm.testdata.EmployeeData.Employee;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PimTest extends BaseTest {

    private PimPage pimPage;
    private AddEmployeePage addEmployeePage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        loginAsAdmin();
        pimPage = pom().getPimPage();
        addEmployeePage = pom().getAddEmployeePage();
        pimPage.goTo();
    }

    // ─── Helper: create a fresh employee in-test ───────────────────────────────

    private Employee createEmployee() {
        Employee employee = EmployeeData.generateEmployee();
        pimPage.clickAdd();
        addEmployeePage.fillDetails(employee.firstName, employee.middleName, employee.lastName);
        addEmployeePage.save();
        return employee;
    }

    // ─── READ ─────────────────────────────────────────────────────────────────

    @Test(groups = {"smoke", "pim"}, description = "Employee list should load with at least one record")
    public void shouldLoadEmployeeListWithRecords() {
        int count = pimPage.getRowCount();
        Assert.assertTrue(count > 0,
                "Employee list should have at least one record, but found: " + count);
    }

    @Test(groups = {"smoke", "pim"}, description = "Clicking Add should navigate to the Add Employee form")
    public void shouldNavigateToAddEmployeePage() {
        pimPage.clickAdd();
        Assert.assertTrue(addEmployeePage.isLoaded(), "Add Employee form should be loaded and visible");
    }

    // ─── CREATE ───────────────────────────────────────────────────────────────

    @Test(groups = {"smoke", "pim"}, description = "Should add a new employee successfully and show success toast")
    public void shouldAddNewEmployeeSuccessfully() {
        createEmployee();
        String message = addEmployeePage.getSuccessMessage();
        Assert.assertTrue(message.contains("Successfully Saved"),
                "Success toast should contain 'Successfully Saved', actual: " + message);
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    @Test(groups = {"pim"}, description = "Should edit an employee's last name and show success toast")
    public void shouldEditEmployeeSuccessfully() {
        Employee employee = createEmployee();

        pimPage.goTo();
        pimPage.searchByName(employee.lastName);
        pimPage.clickEditOnRow(0);

        String updatedLastName = "Updated" + System.currentTimeMillis();
        addEmployeePage.fillDetails(employee.firstName, employee.middleName, updatedLastName);
        addEmployeePage.save();

        String message = addEmployeePage.getSuccessMessage();
        Assert.assertTrue(message.contains("Successfully Updated"),
                "Success toast should contain 'Successfully Updated', actual: " + message);
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    @Test(groups = {"pim"}, description = "Should delete an employee and confirm they no longer appear in search")
    public void shouldDeleteEmployeeSuccessfully() {
        Employee employee = createEmployee();

        pimPage.goTo();
        pimPage.searchByName(employee.lastName);
        pimPage.clickDeleteOnRow(0);
        pimPage.confirmDelete();

        pimPage.searchByName(employee.lastName);
        Assert.assertTrue(pimPage.isNoRecordsFound(),
                "Deleted employee should not appear in search results");
    }

    // ─── SEARCH ───────────────────────────────────────────────────────────────

    @Test(groups = {"smoke", "pim"}, description = "Searching by a known first name should return at least one result")
    public void shouldSearchEmployeeByNameAndFindResults() {
        pimPage.searchByName(EmployeeData.KNOWN_FIRST_NAME);
        int count = pimPage.getRowCount();
        Assert.assertTrue(count > 0,
                "Search for '" + EmployeeData.KNOWN_FIRST_NAME
                        + "' should return at least one result");
    }

    @Test(groups = {"smoke", "pim"}, description = "Searching for a non-existent employee should show 'No Records Found'")
    public void shouldShowNoRecordsForInvalidSearch() {
        pimPage.searchByName(EmployeeData.NONEXISTENT_NAME);
        Assert.assertTrue(pimPage.isNoRecordsFound(),
                "Searching for '" + EmployeeData.NONEXISTENT_NAME + "' should show 'No Records Found'");
    }

    @Test(groups = {"smoke", "pim"}, description = "Resetting search should restore the full employee list")
    public void shouldResetSearchAndShowAllEmployees() {
        pimPage.searchByName(EmployeeData.NONEXISTENT_NAME);
        pimPage.resetSearch();
        int count = pimPage.getRowCount();
        Assert.assertTrue(count > 0, "After reset, employee list should be restored with records");
    }
}
